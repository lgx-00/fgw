package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.pxxy.entity.pojo.Dispatch;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddDispatchVO;
import com.pxxy.entity.vo.Page;
import com.pxxy.entity.vo.QueryDispatchVO;
import com.pxxy.entity.vo.UpdateDispatchVO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface DispatchService extends IService<Dispatch> {

    /**
     * 分页获取所有调度记录
     *
     * @param page 分页信息
     * @param proId 调度所属的项目的编号
     * @return 响应结果
     */
    PageInfo<QueryDispatchVO> getAllDispatch(Page page, Integer proId);


    /**
     * 根据编号获取一条调度记录
     *
     * @param disId 调度编号
     * @param proId 调度所属的项目的编号
     * @return 响应结果，成功包含一条调度记录，失败则包含失败信息
     */
    QueryDispatchVO get(Integer disId, Integer proId);

    /**
     * 下载附件
     *
     * @param disId 调度的编号
     * @param proId 调度所属的项目的编号
     * @return 响应结果
     */
    ResponseEntity<InputStreamResource> download(Integer disId, Integer proId);

    /**
     * 新增调度
     *
     * @param vo 新增的调度数据，包含项目的编号，不包含调度的编号
     * @return 响应结果
     */
    boolean add(AddDispatchVO vo);

    /**
     * 更新调度
     *
     * @param vo 新的调度信息，包含调度的编号和项目的编号
     * @return 响应结果
     */
    boolean update(UpdateDispatchVO vo);

    /**
     * 锁定调度
     *
     * @param disId 待锁定的调度的编号
     * @param proId 调度所属的项目的编号
     * @return 响应结果
     */
    boolean lock(Integer disId, Integer proId);

    /**
     * 解除锁定调度
     *
     * @param disId 待解锁的调度的编号
     * @param proId 调度所属的项目的编号
     * @return 响应结果
     */
    boolean unlock(Integer disId, Integer proId);

    /**
     * 删除调度
     *
     * @param disId 待删除的调度的编号
     * @param proId 调度所属的项目的编号
     * @return 响应结果
     */
    boolean del(Integer disId, Integer proId);

    /**
     * 上传附件
     *
     * @param disAppendix 附件
     * @param proId 项目编号
     * @param disId 上传到的调度的编号
     * @return 响应结果
     */
    boolean upload(MultipartFile disAppendix, Integer proId, Integer disId);

    /**
     * 上传附件
     *
     * @param disAppendix 附件
     * @return 文件名
     */
    String upload(MultipartFile disAppendix);
}
