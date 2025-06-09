package com.lww.sandwich.service;

import com.lww.common.web.vo.PageDataVo;
import com.lww.common.web.vo.PageVo;
import com.lww.sandwich.vo.ViewVO;
import com.lww.sandwich.entity.View;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  服务类
 * @author lww
 * @since 2022-04-26 09:38:22
 */
public interface ViewService extends IService<View> {

    /** 
     * view 记录
     * @author lww
     * @since 2022/4/26 10:00
     * @param ipAddress ipAddress
     */
    void addViewRecord(String ipAddress, HttpServletRequest request);

    /**
     * 访问列表
     * @author lww
     * @since 2022/5/26 13:22
     * @param pageVo  pageVo
     * @return PageDataVo<List<ViewVO>>
     */
    PageDataVo<List<ViewVO>> getViewList(PageVo pageVo);
}
