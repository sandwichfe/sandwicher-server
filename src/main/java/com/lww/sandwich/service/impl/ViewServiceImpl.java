package com.lww.sandwich.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.sandwich.config.security.SecurityConstant;
import com.lww.sandwich.entity.View;
import com.lww.sandwich.mapper.ViewMapper;
import com.lww.sandwich.pojo.Vo.PageDataVo;
import com.lww.sandwich.pojo.Vo.PageVo;
import com.lww.sandwich.service.ViewService;
import com.lww.sandwich.utils.IpUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

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
        String header = request.getHeader(SecurityConstant.HEADER);
        Claims claims = Jwts.parser().setSigningKey(SecurityConstant.JWT_SIGN_KEY).parseClaimsJws(header.replace(SecurityConstant.TOKEN_SPLIT, "")).getBody();
        String subject = claims.getSubject();
        String username = StringUtils.hasText(subject)?subject:"访客";

        String cityInfo = regionSearcher.getAddress(ipAddress);
        View view = new View();
        view.setIp(ipAddress);
        view.setArea(cityInfo);
        view.setUserName(username);
        view.setViewTime(LocalDateTime.now());
        log.info("本次保存：" + view);
        this.save(view);
    }

    @Override
    public PageDataVo<List<View>> getViewList(PageVo pageVo) {
        Page page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        QueryWrapper<View> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("view_time");
        Page<View> viewPage = baseMapper.selectPage(page, wrapper);
        PageDataVo<List<View>> pageDataVo = new PageDataVo<>(viewPage.getCurrent(), viewPage.getTotal(), viewPage.getRecords());
        return pageDataVo;
    }
}
