package com.lww.sandwich.service;

import com.lww.sandwich.entity.View;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
