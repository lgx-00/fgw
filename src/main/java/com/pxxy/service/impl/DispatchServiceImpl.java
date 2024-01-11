package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.pxxy.entity.dto.ProjectCheckDTO;
import com.pxxy.entity.dto.UserDTO;
import com.pxxy.entity.pojo.Dispatch;
import com.pxxy.entity.pojo.Project;
import com.pxxy.entity.pojo.Stage;
import com.pxxy.entity.vo.*;
import com.pxxy.enums.ProjectStatusEnum;
import com.pxxy.exceptions.DBException;
import com.pxxy.exceptions.FileException;
import com.pxxy.exceptions.ForbiddenException;
import com.pxxy.mapper.DispatchMapper;
import com.pxxy.mapper.ProjectMapper;
import com.pxxy.service.DispatchService;
import com.pxxy.service.ProjectCategoryService;
import com.pxxy.service.ProjectService;
import com.pxxy.service.StageService;
import com.pxxy.utils.PageUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
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
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pxxy.constant.ResponseMessage.*;
import static com.pxxy.constant.SystemConstant.*;
import static com.pxxy.enums.DispatchStatusEnum.*;
import static com.pxxy.enums.ProjectStatusEnum.UNLOCKED;

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

    @Override
    @Transactional
    public PageInfo<QueryDispatchVO> getAllDispatch(Page page, Integer proId) {
        CheckType check = projectChecker.of(proId).check(CheckType.PERMISSION);
        if (check != null) throw new ForbiddenException(check.msg);

        updateBaseData();
        return PageUtil.selectPage(page, () -> query().eq("pro_id", proId)
                .ne("dis_status", DELETED_STATUS).orderByDesc("dis_id").list(), mapDispatchToVO);
    }

    @Override
    @Transactional
    public QueryDispatchVO get(Integer disId, Integer proId) {
        CheckType check = projectChecker.of(proId).check(CheckType.PERMISSION);
        if (check != null) throw new ForbiddenException(check.msg);

        Dispatch dis = getById(disId);
        if (dis == null || !dis.getProId().equals(proId)) throw new ForbiddenException(FAIL_MSG);

        updateBaseData();
        return mapDispatchToVO.apply(dis);
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
            return responseFile(appendix, dis.getDisAppendixName());
        } catch (FileException e) {
            return responseFail(e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("下载附件失败", e);
            return responseFail(APPENDIX_NOT_AVAILABLE);
        } catch (UnsupportedEncodingException e) {
            log.error("编码失败", e);
            return responseFail(FAIL_MSG);
        }

    }

    @Override
    @Transactional
    public boolean add(AddDispatchVO vo) throws ForbiddenException, DBException {
        CheckType check = projectChecker.of(vo)
                .check(CheckType.PERMISSION, CheckType.ADD_TIME, CheckType.NUMBER);
        if (check != null) throw new ForbiddenException(check.msg);

        Dispatch dis = parsePojo(vo);
        if (baseMapper.insert(dis) == 1 && updateProject(vo, dis)) {
            baseMapper.lockLastDispatch(dis.getDisId(), dis.getProId());
            UserHolder.removeData(USER_DATA$UPLOAD_FILE_NAME);
            UserHolder.removeData(USER_DATA$UPLOAD_ORIGINAL_FILE_NAME);
            return true;
        }
        throw new DBException(ADD_FAILED);
    }

    @Override
    @Transactional
    public boolean update(UpdateDispatchVO vo) throws ForbiddenException {
        CheckType check = projectChecker.of(vo).of(vo.getProId(), vo.getDisId())
                .check(CheckType.PERMISSION, CheckType.NUMBER);
        if (check != null) throw new ForbiddenException(check.msg);

        Dispatch dispatch = parsePojo(vo);
        return update0(dispatch);
    }

    @Override
    @Transactional
    public boolean lock(Integer disId, Integer proId) throws ForbiddenException {
        CheckType check = projectChecker.of(proId).check(CheckType.PERMISSION);
        if (check != null) throw new ForbiddenException(check.msg);

        if (update().eq("dis_id", disId).eq("dis_status", NORMAL.val)
                .set("dis_status", LOCKED.val).update()) {
            projectService.update().eq("pro_id", proId)
                    .set("pro_status", NORMAL.val).update();
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean unlock(Integer disId, Integer proId) throws ForbiddenException {
        CheckType check = projectChecker.of(proId, disId).check(CheckType.PERMISSION, CheckType.UNLOCK_LAST);
        if (check != null) throw new ForbiddenException(check.msg);

        if (update().eq("dis_id", disId).eq("dis_status", LOCKED.val)
                .set("dis_status", NORMAL.val).update()) {
            projectService.update().eq("pro_id", proId)
                    .set("pro_status", UNLOCKED.val).update();
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean del(Integer disId, Integer proId) throws ForbiddenException {
        CheckType check = projectChecker.of(proId).check(CheckType.PERMISSION);
        if (check != null) throw new ForbiddenException(check.msg);

        Dispatch dis = query().select("dis_id", "dis_status")
                .eq("dis_id", disId).eq("pro_id", proId).one();
        String valid = checkValid(dis, false);
        if (valid != null) throw new ForbiddenException(valid);

        dis.setDisStatus(DELETED.val);
        if (!updateById(dis)) {
            return false;
        }
        Dispatch lastDispatch = baseMapper.getLastDispatch(disId, proId);
        if (lastDispatch == null) {
            // 最后一条调度被删除，将项目中的冗余字段删除
            lastDispatch = new Dispatch();
            lastDispatch.setProId(proId);
            lastDispatch.setDisTotal(0);
            lastDispatch.setDisInvest(0);
            lastDispatch.setDisYear(0);
            lastDispatch.setDisTotalPercent(0);
            lastDispatch.setDisYearPercent(0);
            lastDispatch.setDisProgress("");
        }
        updateProject(lastDispatch, ProjectStatusEnum.NORMAL.val);
        return true;
    }

    @Override
    public boolean upload(MultipartFile disAppendix, Integer proId, Integer disId) throws ForbiddenException {
        CheckType check = projectChecker.of(proId, disId).check(CheckType.PERMISSION);
        if (check != null) throw new ForbiddenException(check.msg);

        Dispatch dis = query().select("dis_appendix", "dis_status")
                .eq("dis_id", disId).one();
        if (dis.getDisStatus().equals(LOCKED.val))
            throw new ForbiddenException(CANNOT_UPDATE_LOCKED_DISPATCH);

        String fileName = uploadFile(disAppendix);
        if (fileName == null) {
            return false;
        }

        if (update().eq("dis_id", disId).set("dis_appendix", fileName)
                .set("dis_appendix_name", disAppendix.getOriginalFilename()).update()) {
            if (!new File(filePath, fileName).delete()) {
                log.warn("文件 {}/{} 删除失败。", filePath, fileName);
            }
            String oldAppendix = dis.getDisAppendix();
            if (oldAppendix != null) if (!new File(filePath, oldAppendix).delete()) {
                log.warn("文件 {}/{} 删除失败。", filePath, fileName);
            }
            return true;
        }

        return false;

    }

    @Override
    public String upload(MultipartFile disAppendix) {
        String fileName = uploadFile(disAppendix);
        if (fileName == null) return null;
        String oldFileName = (String) UserHolder.putData(USER_DATA$UPLOAD_FILE_NAME, fileName);
        UserHolder.putData(USER_DATA$UPLOAD_ORIGINAL_FILE_NAME, disAppendix.getOriginalFilename());
        if (oldFileName != null && !new File(filePath, oldFileName).delete()) {
            log.warn("文件 {}/{} 删除失败。", filePath, oldFileName);
        }
        // TODO will this produce many unnecessary logout handlers?
        UserHolder.addLogoutHandler(user -> {
            String fileToDelete = (String) UserHolder.getData(USER_DATA$UPLOAD_FILE_NAME, user);
            if (fileToDelete != null) if (!new File(filePath, fileToDelete).delete()) {
                log.warn("文件 {}/{} 删除失败。", filePath, fileToDelete);
            }
        });
        return fileName;
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
                        && !status.equals(UNLOCKED.val)) {
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

                int t = dis.getDisTotal();
                int y = dis.getDisYear();
                if (y > t) {
                    // 年投资额不能超过总投资额
                    return false;
                }

                // 本次下达投资
                int i = dis.getDisInvest();

                if (i > y) {
                    // 本次下达投资不能超过今年总投资
                    return false;
                }

                Dispatch dis0 = pro.getOldDispatch();
                int pt = pro.getProPlan();
                int py = Optional.ofNullable(dis.getDisPlanYear()).orElse(pro.getProPlanYear());
                int tp = dis.getDisTotalPercent();
                int yp = dis.getDisYearPercent();

                if (Objects.nonNull(dis0)) {  // 是否存在上次投资
                    int t0 = Optional.ofNullable(dis0.getDisTotal()).orElse(0);
                    int y0 = Optional.ofNullable(dis0.getDisYear()).orElse(0);

                    if (!((t - t0 == i) && (y0 == 0 || y - y0 == i))) {
                        // 上次的投资额与本次的投资额的差必须与本次下达投资额相等
                        return false;
                    }
                }
                // 百分比值的计算必须正确
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

        final String msg;
        final String[] select;

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
                Dispatch dispatch = query().select("max(dis_id) as dis_id").eq("pro_id", proId)
                        .ne("dis_status", DELETED.val).one();
                dto.setLastDis(dispatch != null && Objects.equals(disId, dispatch.getDisId()));
            }
            List<String> selects = new ArrayList<>(8);
            for (CheckType checkType : checkTypes) {
                selects.addAll(Arrays.asList(checkType.select));
                if (checkType.equals(CheckType.NUMBER)) {
                    Objects.requireNonNull(dispatchVO, "The Dispatch vo for" +
                            " number check must be not null!");
                    Dispatch lastDispatch = baseMapper.getLastDispatch(disId == 0 ? Integer.MAX_VALUE : disId, proId);
                    dto.setNewDispatch(dispatchVO);
                    dto.setOldDispatch(lastDispatch);
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

    private boolean update0(Dispatch dispatch) throws ForbiddenException, DBException {
        Dispatch dis = query().select("dis_appendix", "dis_status")
                .eq("dis_id", dispatch.getDisId()).one();
        String valid = checkValid(dis, true);
        if (valid != null) throw new ForbiddenException(valid);

        if (!updateById(dispatch)) {
            throw new DBException(UPDATE_FAILED);
        }
        UserHolder.removeData(USER_DATA$UPLOAD_FILE_NAME);
        UserHolder.removeData(USER_DATA$UPLOAD_ORIGINAL_FILE_NAME);

        updateProject(dispatch, null);

        // 删除之前上传的文件，判断条件：新的附件和旧的附件均不为空
        String oldAppendix = dis.getDisAppendix();
        String newAppendix = dispatch.getDisAppendix();
        if (newAppendix != null && oldAppendix != null && !new File(filePath, oldAppendix).delete()) {
            log.warn("文件 {}/{} 删除失败。", filePath, oldAppendix);
        }
        return true;
    }

    private String checkValid(Dispatch dis, boolean updating) {
        if (dis == null) {
            return updating ? UPDATE_FAILED : DELETE_FAILED;
        }
        if (dis.getDisStatus().equals(LOCKED.val)) {
            return updating ? CANNOT_UPDATE_LOCKED_DISPATCH : CANNOT_DELETE_LOCKED_DISPATCH;
        }
        if (dis.getDisStatus().equals(DELETED.val)) {
            return updating ? UPDATE_FAILED : DELETE_FAILED;
        }
        return null;
    }

    private void updateProject(Dispatch dispatch, Integer proStatus) {
        // 更新调度时，更新项目中的冗余字段
        Project project = new Project(dispatch.getProId(), dispatch.getDisTotal(),
                dispatch.getDisYear(), dispatch.getDisTotalPercent(),
                dispatch.getDisYearPercent(), dispatch.getDisProgress());
        project.setProStatus(proStatus);
        projectService.updateById(project);
    }

    private boolean updateProject(AddDispatchVO vo, Dispatch dis) {
        // 添加调度时，更新项目中的冗余字段
        Project project = new Project(vo.getProId(), dis.getDisTotal(), dis.getDisYear(),
                dis.getDisTotalPercent(), dis.getDisYearPercent(), dis.getDisProgress());
        project.setProStatus(UNLOCKED.val);
        // 下次调度提醒日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        int day = Math.min(calendar.getActualMaximum(Calendar.DAY_OF_MONTH), vo.getPrcPeriod() / 100);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        project.setProNextUpdate(calendar.getTime());
        return ((ProjectMapper) projectService.getBaseMapper()).updateDispatchData(project) == 1;
    }

    private <VO extends DispatchVO> Dispatch parsePojo(VO vo) throws FileException {
        Dispatch dis = new Dispatch();
        BeanUtil.copyProperties(vo, dis);
        dis.setUId(UserHolder.getUser().getUId());
        if (vo.getDisAppendix() != null) {
            // 未上传附件的情况
            String uploadFileName = (String) UserHolder.getData(USER_DATA$UPLOAD_FILE_NAME);
            if (uploadFileName == null || !uploadFileName.equals(vo.getDisAppendix())) {
                throw new FileException(APPENDIX_NOT_AVAILABLE);
            }
            dis.setDisAppendix(uploadFileName);
        }
        dis.setDisAppendixName((String) UserHolder.getData(USER_DATA$UPLOAD_ORIGINAL_FILE_NAME));
        return dis;
    }

    /**
     * 保存文件到本地
     *
     * @param file 待保存的文件
     * @return 保存的文件的新文件名，保存失败则返回 null
     */
    private String uploadFile(MultipartFile file) {
        checkExists(true);

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
                log.error("创建文件夹失败");
                if (strict) throw new FileException(CANNOT_MAKE_DIR);
            }
            if (!strict) throw new FileException(APPENDIX_NOT_AVAILABLE);
        }
    }

    private ResponseEntity<InputStreamResource> responseFile(String appendix, String appendixName)
            throws FileNotFoundException, UnsupportedEncodingException {
        File f = new File(filePath, appendix);
        FileInputStream fis = new FileInputStream(f);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +
                URLEncoder.encode(Optional.ofNullable(appendixName).orElse("附件"), "UTF-8"));
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(f.length()));
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(fis));
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
