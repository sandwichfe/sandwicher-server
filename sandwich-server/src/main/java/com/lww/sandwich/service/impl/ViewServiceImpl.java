package com.lww.sandwich.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.common.web.vo.PageDataVo;
import com.lww.common.web.vo.PageVo;
import com.lww.sandwich.entity.View;
import com.lww.sandwich.mapper.ViewMapper;
import com.lww.sandwich.service.ViewService;
import com.lww.sandwich.vo.ViewVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author lww
 * @since 2022-04-26 09:38:22
 */
@Slf4j
@Service
public class ViewServiceImpl extends ServiceImpl<ViewMapper, View> implements ViewService {

    @Resource(name = "ip2regionSearcher")
    private Ip2regionSearcher regionSearcher;

    @Override
    public void addViewRecord(String ipAddress,HttpServletRequest request) {
        // 登录用户名
        String cityInfo = regionSearcher.getAddress(ipAddress);
        View view = new View();
        view.setIp(ipAddress);
        view.setArea(cityInfo);
        // view.setUserName(username);
        view.setViewTime(LocalDateTime.now());
        log.info("本次保存：{}", view);
        this.save(view);
    }

    @Override
    public PageDataVo<List<ViewVO>> getViewList(PageVo pageVo) {
        Page<View> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        QueryWrapper<View> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("view_time");
        Page<View> viewPage = baseMapper.selectPage(page, wrapper);

        // 转化VO
        IPage<ViewVO> pageConvert = viewPage.convert(view -> {
            ViewVO viewVO = new ViewVO();
            BeanUtils.copyProperties(view,viewVO);
            viewVO.setTestDictType("1,2");
            return viewVO;
        });
        return new PageDataVo<>(pageConvert.getCurrent(), pageConvert.getTotal(), pageConvert.getRecords());
    }
}
