package com.lww.sandwich.service.impl;

import com.lww.sandwich.entity.View;
import com.lww.sandwich.mapper.ViewMapper;
import com.lww.sandwich.service.ViewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.sandwich.utils.IpUtils;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *  服务实现类
 * @author lww
 * @since 2022-04-26 09:38:22
 */
@Service
public class ViewServiceImpl extends ServiceImpl<ViewMapper, View> implements ViewService {


    @Override
    public void addViewRecord(String ipAddress) {
        ipAddress = "122.224.233.68";
        String cityInfo = IpUtils.getCityInfo(ipAddress);
        View view = new View();
        view.setIp(ipAddress);
        view.setArea(cityInfo);
        view.setUserName("访客");
        view.setViewTime(LocalDateTime.now());
        System.out.println("record： "+view);
        this.save(view);
    }
}
