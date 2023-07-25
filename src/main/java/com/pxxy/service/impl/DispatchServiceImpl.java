package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.pxxy.dto.ProjectCheckDTO;
import com.pxxy.dto.UserDTO;
import com.pxxy.enums.ProjectStatusEnum;
import com.pxxy.exceptions.FileException;
import com.pxxy.mapper.DispatchMapper;
import com.pxxy.pojo.Dispatch;
import com.pxxy.pojo.Project;
import com.pxxy.pojo.Stage;
import com.pxxy.service.DispatchService;
import com.pxxy.service.ProjectCategoryService;
import com.pxxy.service.ProjectService;
import com.pxxy.service.StageService;
import com.pxxy.utils.PageUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import com.pxxy.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pxxy.constant.ResponseMessage.*;
import static com.pxxy.constant.SystemConstant.DELETED_STATUS;
import static com.pxxy.enums.DispatchStatusEnum.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Slf4j
@Service
public class DispatchServiceImpl extends ServiceImpl<DispatchMapper, Dispatch> implements DispatchService {

    @Value("${fgw.file-path}")
    private String filePath;

    @Resource
    private ProjectService projectService;

    @Resource
    private ProjectCategoryService prcService;

    @Resource
    private StageService stageService;

    private final ProjectChecker projectChecker = new ProjectChecker();

    private Map<Integer, Stage> stageMap;

    private final Function<Dispatch, QueryDispatchVO> mapDispatchToVO = d -> {
        QueryDispatchVO vo = new QueryDispatchVO();
        BeanUtil.copyProperties(d, vo);
        vo.setStage(Optional.ofNullable(stageMap.get(d.getStageId()))
                .orElse(new Stage()).getStageName());
        vo.setDisAppendix(d.getDisAppendix());
        vo.setDisStatus(d.getDisStatus());
        return vo;
    };

    private final Set<String> unusedFileName = new HashSet<>();

    @Override
    @Transactional
    public ResultResponse<PageInfo<QueryDispatchVO>> getAllDispatch(Page page, Integer proId) {
        CheckType check = projectChecker.of(proId).check(CheckType.PERMISSION);
        if (check != null) {
            return ResultResponse.fail(check.msg);
        }

        updateBaseData();
        return ResultResponse.ok(PageUtil.selectPage(page,
                () -> query().eq("pro_id", proId).ne("dis_status", DELETED_STATUS).list(), mapDispatchToVO));
    }

    @Override
    @Transactional
    public ResultResponse<QueryDispatchVO> get(Integer disId, Integer proId) {
        CheckType check = projectChecker.of(proId).check(CheckType.PERMISSION);
        if (check != null) {
            return ResultResponse.fail(check.msg);
        }

        Dispatch dis = getById(disId);
        if (dis == null || !dis.getProId().equals(proId)) {
            return ResultResponse.fail(FAIL_MSG);
        }

        updateBaseData();
        return ResultResponse.ok(mapDispatchToVO.apply(dis));
    }

    @Override
    public ResponseEntity<InputStreamResource> download(Integer disId, Integer proId) {
        CheckType check = projectChecker.of(proId).check(CheckType.PERMISSION);
        if (check != null) {
            return responseFail(check.msg);
        }

        Dispatch dis = getById(disId);
        if (dis == null || !dis.getProId().equals(proId)) {
            return responseFail(FAIL_MSG);
        }

        String appendix = dis.getDisAppendix();
        if (appendix == null) return responseFail(NO_APPENDIX);

        try {
            checkExists(false);
            return responseFile(appendix);
        } catch (FileException e) {
            return responseFail(e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("下载附件失败！", e);
            return responseFail(APPENDIX_NOT_AVAILABLE);
        }

    }

    @Override
    @Transactional
    public ResultResponse<?> add(AddDispatchVO vo) {
        CheckType check = projectChecker.of(vo)
                .check(CheckType.PERMISSION, CheckType.ADD_TIME, CheckType.NUMBER);
        if (check != null) {
            return ResultResponse.fail(check.msg);
        }

        Dispatch dis;
        try {
            dis = parsePojo(vo);
        } catch (FileException e) {
            return ResultResponse.fail(e.getMessage());
        }
        if (baseMapper.insert(dis) == 1) {
            updateProject(vo, dis);
            baseMapper.lockLastDispatch(dis.getDisId(), dis.getProId());
            return ResultResponse.ok();
        }
        return ResultResponse.fail(ADD_FAILED);
    }

    @Override
    @Transactional
    public ResultResponse<?> update(UpdateDispatchVO vo) {
        CheckType check = projectChecker.of(vo).of(vo.getProId(), vo.getDisId())
                .check(CheckType.PERMISSION, CheckType.NUMBER);
        if (check != null) {
            return ResultResponse.fail(check.msg);
        }

        try {
            Dispatch dispatch = parsePojo(vo);
            return update0(dispatch);
        } catch (FileException e) {
            return ResultResponse.fail(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultResponse<?> lock(Integer disId, Integer proId) {
        CheckType check = projectChecker.of(proId).check(CheckType.PERMISSION);
        if (check != null) {
            return ResultResponse.fail(check.msg);
        }

        if (update().eq("dis_id", disId).eq("dis_status", NORMAL.val)
                .set("dis_status", LOCKED.val).update()) {
            projectService.update().eq("pro_id", proId)
                    .set("pro_status", ProjectStatusEnum.NORMAL.val).update();
            return ResultResponse.ok();
        }
        return ResultResponse.fail(LOCK_FAILED);
    }

    @Override
    @Transactional
    public ResultResponse<?> unlock(Integer disId, Integer proId) {
        CheckType check = projectChecker.of(proId, disId).check(CheckType.PERMISSION, CheckType.UNLOCK_LAST);
        if (check != null) {
            return ResultResponse.fail(check.msg);
        }

        if (update().eq("dis_id", disId).eq("dis_status", LOCKED.val)
                .set("dis_status", NORMAL.val).update()) {
            projectService.update().eq("pro_id", proId)
                    .set("pro_status", ProjectStatusEnum.UNLOCKED.val).update();
            return ResultResponse.ok();
        }

        return ResultResponse.fail(UNLOCK_FAILED);
    }

    @Override
    @Transactional
    public ResultResponse<?> del(Integer disId, Integer proId) {
        CheckType check = projectChecker.of(proId).check(CheckType.PERMISSION);
        if (check != null) {
            return ResultResponse.fail(check.msg);
        }

        Dispatch dis = query().select("dis_id", "dis_status")
                .eq("dis_id", disId).eq("pro_id", proId).one();
        if (dis == null) {
            return ResultResponse.fail(DELETE_FAILED);
        }
        if (dis.getDisStatus().equals(LOCKED.val)) {
            return ResultResponse.fail(CANNOT_DELETE_LOCKED_DISPATCH);
        }
        dis.setDisStatus(DELETED.val);
        if (updateById(dis)) {
            projectService.update().eq("pro_id", proId)
                    .set("pro_status", ProjectStatusEnum.NORMAL.val);
            return ResultResponse.ok();
        }
        return ResultResponse.fail(DELETE_FAILED);
    }

    @Override
    public ResultResponse<?> upload(MultipartFile disAppendix, Integer proId, Integer disId) {
        CheckType check = projectChecker.of(proId, disId).check(CheckType.PERMISSION);
        if (check != null) {
            return ResultResponse.fail(check.msg);
        }

        Dispatch dis = query().select("dis_appendix", "dis_status")
                .eq("dis_id", disId).one();
        if (dis.getDisStatus().equals(LOCKED.val)) {
            return ResultResponse.fail(CANNOT_UPDATE_LOCKED_DISPATCH);
        }

        String fileName = uploadFile(disAppendix);
        if (fileName == null) {
            return ResultResponse.fail(UPLOAD_FAILED);
        }

        if (update().eq("dis_id", disId).set("dis_appendix", fileName).update()) {
            if (dis.getDisAppendix() != null && !new File(filePath, dis.getDisAppendix()).delete()) {
                log.warn("文件 {}/{} 删除失败。", filePath, fileName);
            }
            return ResultResponse.ok();
        }

        if (!new File(filePath, fileName).delete()) {
            log.warn("文件 {}/{} 删除失败。", filePath, fileName);
        }
        return ResultResponse.fail(UPLOAD_FAILED);

    }

    // TODO: 2023/7/24 及时删除文件
    @Override
    public ResultResponse<String> upload(MultipartFile disAppendix) {
        String fileName = uploadFile(disAppendix);
        if (fileName != null) {
            unusedFileName.add(fileName);
            return ResultResponse.ok(fileName);
        }
        return ResultResponse.fail(UPLOAD_FAILED);
    }

    private enum CheckType {
        /**
         * 权限校验
         */
        PERMISSION(NO_PERMISSION, "u_id", "pro_status", "dep_id") {
            @Override
            boolean check(ProjectCheckDTO pro) {
                Integer status = pro.getProStatus();
                if (!status.equals(ProjectStatusEnum.NORMAL.val)
                        && !status.equals(ProjectStatusEnum.TO_BE_SCHEDULED.val)
                        && !status.equals(ProjectStatusEnum.UNLOCKED.val)) {
                    return false;
                }
                UserDTO user = UserHolder.getUser();
                if (user.getUId().equals(1)) {
                    log.info("管理员跳过调度相关操作的权限校验。");
                    return true;
                }
                if (!pro.getUId().equals(user.getUId())) {
                    return false;
                }
                return pro.getDepId().equals(user.getDepId());
            }
        },
        /**
         * 调度添加时间是否在规定时间内
         */
        ADD_TIME(ADD_OUT_OF_TIME, "prc_id") {
            @Override
            boolean check(ProjectCheckDTO pro) {
                int period = pro.getPrcPeriod();
                if (period == 0) return true;
                int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int a = period / 100, b = period % 100;
                return a <= b ? (dayOfMonth >= a && dayOfMonth <= b) : (dayOfMonth >= a || dayOfMonth <= b);
            }
        },
        /**
         * 数值校验，百分比计算是否正确
         */
        NUMBER(PARAMETER_CHECK_FAILED, "pro_plan_year", "pro_plan") {
            @Override
            boolean check(ProjectCheckDTO pro) {
                AddDispatchVO dis = pro.getNewDispatch();
                Dispatch dis0 = pro.getOldDispatch();
                int pt = pro.getProPlan();
                int py = Optional.ofNullable(dis.getDisPlanYear()).orElse(pro.getProPlanYear());
                int t = dis.getDisTotal();
                int y = dis.getDisYear();
                int i = dis.getDisInvest();
                int tp = dis.getDisTotalPercent();
                int yp = dis.getDisYearPercent();
                if (dis0 != null) {
                    int t0 = dis0.getDisTotal();
                    int y0 = Optional.ofNullable(dis0.getDisYear()).orElse(0);

                    if (!((t - t0 == i) && (y0 == 0 || y - y0 == i))) {
                        return false;
                    }
                }
                return (py == 0 || Math.abs(y * 100 / py) - yp <= 1)
                    && (pt == 0 || Math.abs(t * 100 / pt) - tp <= 1);
            }

        },

        UNLOCK_LAST(ONLY_UNLOCK_LAST_DISPATCH) {
            @Override
            boolean check(ProjectCheckDTO pro) {
                return pro.isLastDis();
            }
        },

        ERROR(FAIL_MSG) {
            @Override
            boolean check(ProjectCheckDTO pro) {
                return false;
            }
        };

        String msg;
        String[] select;

        CheckType(String msg, String ... select) {
            this.msg = msg;
            this.select = select;
        }

        abstract boolean check(ProjectCheckDTO pro);
    }

    private class ProjectChecker {

        private int proId;
        private int disId;
        private AddDispatchVO dispatchVO;

        ProjectChecker of(int proId) {
            this.proId = proId;
            return this;
        }

        ProjectChecker of(AddDispatchVO dis) {
            this.proId = dis.getProId();
            this.dispatchVO = dis;
            return this;
        }

        ProjectChecker of(UpdateDispatchVO dis) {
            this.proId = dis.getProId();
            this.dispatchVO = dis;
            return this;
        }

        ProjectChecker of(int proId, int disId) {
            this.proId = proId;
            this.disId = disId;
            return this;
        }

        private void clear() {
            proId = 0;
            disId = 0;
            dispatchVO = null;
        }

        CheckType check(CheckType ... checkTypes) {
            if (checkTypes.length == 0) return null;
            if (checkTypes.length > 4 || Arrays.stream(checkTypes).distinct().count() < checkTypes.length)
                throw new IllegalArgumentException("Checking type must be not duplicate!");

            ProjectCheckDTO dto = new ProjectCheckDTO();
            if (disId != 0) {
                // 调度编号不为 0 则查询是否为最后一次调度
                Dispatch dispatch = query().select("max(dis_id) as dis_id").eq("pro_id", proId).one();
                dto.setLastDis(dispatch != null && Objects.equals(disId, dispatch.getDisId()));
            }
            List<String> selects = new ArrayList<>(8);
            for (CheckType checkType : checkTypes) {
                selects.addAll(Arrays.asList(checkType.select));
                if (checkType.equals(CheckType.NUMBER)) {
                    Objects.requireNonNull(dispatchVO, "The Dispatch vo for" +
                            " number check must be not null!");
                    Dispatch lastDispatch = baseMapper.getLastDispatch(disId == 0 ? Integer.MAX_VALUE : disId, proId);
                    Dispatch dis = Optional.ofNullable(lastDispatch).orElse(new Dispatch());
                    dto.setNewDispatch(dispatchVO);
                    dto.setOldDispatch(dis);
                }
            }
            Project project = projectService.query().select(selects.toArray(new String[0])).eq("pro_id", proId).one();
            if (project == null) return CheckType.ERROR;

            BeanUtil.copyProperties(project, dto);
            if (project.getPrcId() != null) {
                Integer prcPeriod = prcService.getById(project.getPrcId()).getPrcPeriod();
                dispatchVO.setPrcPeriod(prcPeriod);
                dto.setPrcPeriod(prcPeriod);
            }

            for (CheckType checkType : checkTypes) {
                if (!checkType.check(dto)) {
                    clear();
                    return checkType;
                }
            }
            clear();
            return null;

        }
    }

    private ResultResponse<?> update0(Dispatch dispatch) {
        Dispatch dis = query().select("dis_appendix", "dis_status")
                .eq("dis_id", dispatch.getDisId()).one();
        if (dis == null) {
            return ResultResponse.fail(UPDATE_FAILED);
        }
        if (dis.getDisStatus().equals(LOCKED.val)) {
            return ResultResponse.fail(CANNOT_UPDATE_LOCKED_DISPATCH);
        }
        String appendix = dis.getDisAppendix();
        if (updateById(dispatch)) {
            updateProject(dispatch);
            unusedFileName.remove(dispatch.getDisAppendix());
            // 删除之前上传的文件，判断条件：新的附件和旧的附件均不为空
            if (dispatch.getDisAppendix() != null && appendix != null) {
                if (!new File(filePath, appendix).delete())
                log.warn("文件 {}/{} 删除失败。", filePath, appendix);
            }
            return ResultResponse.ok();
        }
        return ResultResponse.fail(UPDATE_FAILED);
    }

    private void updateProject(Dispatch dispatch) {
        // 更新项目中的冗余字段
        Project project = new Project(dispatch.getProId(), dispatch.getDisTotal(),
                dispatch.getDisYear(), dispatch.getDisTotalPercent(),
                dispatch.getDisYearPercent(), dispatch.getDisProgress());
        projectService.updateById(project);
    }

    private void updateProject(AddDispatchVO vo, Dispatch dis) {
        // 更新项目中的冗余字段
        Project project = new Project(vo.getProId(), dis.getDisTotal(), dis.getDisYear(),
                dis.getDisTotalPercent(), dis.getDisYearPercent(), dis.getDisProgress());
        project.setProStatus(ProjectStatusEnum.UNLOCKED.val);
        // 下次调度提醒日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        int day = Math.min(calendar.getActualMaximum(Calendar.DAY_OF_MONTH), vo.getPrcPeriod() / 100);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        project.setProNextUpdate(calendar.getTime());
        projectService.updateById(project);
    }

    private <VO extends DispatchVO> Dispatch parsePojo(VO vo) throws FileException {
        if (!unusedFileName.contains(vo.getDisAppendix())) {
            throw new FileException(APPENDIX_NOT_AVAILABLE);
        }
        Dispatch dis = new Dispatch();
        BeanUtil.copyProperties(vo, dis);
        dis.setUId(UserHolder.getUser().getUId());
        return dis;
    }

    /**
     * 保存文件到本地
     *
     * @param file 待保存的文件
     * @return 保存的文件的新文件名
     */
    private String uploadFile(MultipartFile file) {

        String newName = UUID.randomUUID().toString().replaceAll("-", "");
        String extName = "." + FileUtil.extName(file.getOriginalFilename());
        String name = newName + extName;
        File savePath = new File(filePath, name);

        try {
            file.transferTo(savePath);
            return name;
        } catch (IOException e) {
            log.error("保存文件失败!", e);
            return null;
        }

    }

    /**
     * 检查保存附件的路径是否存在，如果不存在则创建并输出日志。
     *
     * @param strict 是否为严格模式，如果是则在创建路径失败时抛出异常，否则在路径不存在时抛出异常。
     * @throws FileException 当创建文件夹失败或文件路径不存在时抛出异常。
     */
    private void checkExists(boolean strict) throws FileException {
        File parent = new File(filePath);
        if (!parent.exists()) {
            if (parent.mkdirs()) log.info("为上传附件创建了文件夹。");
            else {
                log.error("创建文件夹失败！");
                if (strict) throw new FileException(CANNOT_MAKE_DIR);
            }
            if (!strict) throw new FileException(APPENDIX_NOT_AVAILABLE);
        }
    }

    private ResponseEntity<InputStreamResource> responseFile(String appendix) throws FileNotFoundException {
        File f = new File(filePath, appendix);
        FileInputStream fis = new FileInputStream(f);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                String.format("attachment; filename=\"附件.%s\"", FileUtil.extName(appendix)));
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fis));
    }

    private ResponseEntity<InputStreamResource> responseFail(String msg) {
        byte[] bytes = JSONUtil.toJsonStr(ResultResponse.fail(msg)).getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return ResponseEntity
                .status(FAIL_CODE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new InputStreamResource(bais));
    }

    private void updateBaseData() {
        QueryChainWrapper<Stage> query = stageService.query();
        stageMap = query.ne("stage_status", DELETED_STATUS)
                .list().stream().collect(Collectors.toMap(Stage::getStageId, s -> s));
    }
}
