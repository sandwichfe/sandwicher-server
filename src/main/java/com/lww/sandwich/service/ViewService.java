package com.lww.sandwich.service;

import com.lww.sandwich.entity.View;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.sandwich.pojo.Vo.PageDataVo;
import com.lww.sandwich.pojo.Vo.PageVo;

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
     * @param ipAddress
     * @return
     */
    void addViewRecord(String ipAddress);

    /**
     * 访问列表
     * @author lww
     * @since 2022/5/26 13:22
     * @param pageVo
     * @return
     */
    PageDataVo<List<View>> getViewList(PageVo pageVo);
}
