package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.pxxy.enums.DispatchStatusEnum;
import com.pxxy.exception.FileException;
import com.pxxy.mapper.DispatchMapper;
import com.pxxy.pojo.Dispatch;
import com.pxxy.pojo.Stage;
import com.pxxy.service.DispatchService;
import com.pxxy.service.ProjectService;
import com.pxxy.service.StageService;
import com.pxxy.utils.PageUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import com.pxxy.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pxxy.constant.ResponseMessage.*;
import static com.pxxy.constant.SystemConstant.DELETED_STATUS;

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
    private StageService stageService;

    private Map<Integer, Stage> mapper;

    private final Function<Dispatch, QueryDispatchVO> mapDispatchToVO = d -> {
        QueryDispatchVO vo = new QueryDispatchVO();
        BeanUtil.copyProperties(d, vo);
        vo.setStage(Optional.ofNullable(mapper.get(d.getStageId()))
                .orElse(new Stage()).getStageName());
        return vo;
    };

    @Override
    public ResultResponse<PageInfo<QueryDispatchVO>> getAllDispatch(Page page, Integer proId) {
        // TODO: 2023/7/13 权限校验
        updateBaseData();
        return ResultResponse.ok(PageUtil.selectPage(page,
                () -> query().eq("pro_id", proId).ne("dis_status", DELETED_STATUS).list(), mapDispatchToVO));
    }

    @Override
    public ResultResponse<QueryDispatchVO> get(Integer disId, Integer proId) {
        Dispatch dis = getById(disId);
        if (dis == null || !dis.getProId().equals(proId)) {
            return ResultResponse.fail(FAIL_MSG);
        }
        // TODO: 2023/7/13 权限校验
        updateBaseData();
        return ResultResponse.ok(mapDispatchToVO.apply(dis));
    }

    @Override
    public ResponseEntity<InputStreamResource> download(Integer disId, Integer proId) {
        // TODO: 2023/7/13 权限校验
        Dispatch dispatch = getById(disId);
        if (dispatch == null) return responseFail(FAIL_MSG);

        String appendix = dispatch.getDisAppendix();
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
    public ResultResponse<?> add(AddDispatchVO vo) {
        // TODO: 2023/7/13 权限校验
        // TODO: 2023/7/13 更新项目中的冗余字段
        // TODO: 2023/7/13 数值校验
        Dispatch dis;
        try {
            dis = parsePojo(vo);
        } catch (FileException e) {
            return ResultResponse.fail(e.getMessage());
        }
        if (save(dis)) {
            return ResultResponse.ok();
        }
        return ResultResponse.fail(ADD_FAILED);
    }

    @Override
    public ResultResponse<?> update(UpdateDispatchVO vo) {
        // TODO: 2023/7/13 权限校验
        // TODO: 2023/7/13 更新项目中的冗余字段
        // TODO: 2023/7/13 数值校验
        try {
            Dispatch dispatch = parsePojo(vo);
            if (updateById(dispatch)) return ResultResponse.ok();
        } catch (FileException e) {
            return ResultResponse.fail(e.getMessage());
        }
        return ResultResponse.fail(UPDATE_FAILED);
    }

    @Override
    public ResultResponse<?> lock(Integer disId, Integer proId) {
        // TODO: 2023/7/13 权限校验
        return update()
                .eq("dis_id", disId).eq("dis_status", DispatchStatusEnum.NORMAL.val)
                .set("dis_status", DispatchStatusEnum.LOCKED.val).update()
                ? ResultResponse.ok() : ResultResponse.fail(LOCK_FAILED);
    }

    @Override
    public ResultResponse<?> unlock(Integer disId, Integer proId) {
        // TODO: 2023/7/13 权限校验
        return update()
                .eq("dis_id", disId).eq("dis_status", DispatchStatusEnum.LOCKED.val)
                .set("dis_status", DispatchStatusEnum.NORMAL.val).update()
                ? ResultResponse.ok() : ResultResponse.fail(UNLOCK_FAILED);
    }

    @Override
    public ResultResponse<?> del(Integer disId, Integer proId) {
        // TODO: 2023/7/13 权限校验
        // TODO: 2023/7/13 检查是否调度已被锁定
        return removeById(disId) ? ResultResponse.ok() : ResultResponse.fail(DELETE_FAILED);
    }

    private <VO extends DispatchVO> Dispatch parsePojo(VO vo) throws FileException {
        checkExists(true);
        String newFileName = null;
        MultipartFile appendix = vo.getDisAppendix();
        if (appendix != null && (newFileName = uploadFile(appendix)) == null) {
            throw new FileException(UPLOAD_FAILED);
        }
        Dispatch dis = new Dispatch();
        BeanUtil.copyProperties(vo, dis);
        dis.setUId(UserHolder.getUser().getUId());
        dis.setDisAppendix(newFileName);
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
        updateBaseData(false);
    }

    private void updateBaseData(boolean findDeleted) {
        QueryChainWrapper<Stage> query = stageService.query();
        mapper = (findDeleted ? query : query.ne("stage_status", DELETED_STATUS))
                .list().stream().collect(Collectors.toMap(Stage::getStageId, s -> s));
    }
}
