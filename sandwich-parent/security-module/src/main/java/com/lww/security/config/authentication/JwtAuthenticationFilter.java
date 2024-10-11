package com.lww.security.config.authentication;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.lww.security.entity.LoginUser;
import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.utils.ResponseOutUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *   JWT登录用户效验拦截器
 * @author lww
 * @since 2022/7/21 9:13
 */
@Slf4j
@Getter
@Setter
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstant.HEADER);
        if (StrUtil.isBlank(header)) {
            header = request.getParameter(SecurityConstant.HEADER);
        }
        Boolean notValid = StrUtil.isBlank(header) || (!header.startsWith(SecurityConstant.TOKEN_SPLIT));
        if (notValid) {
            chain.doFilter(request, response);
            return;
        }
        try {
            //UsernamePasswordAuthenticationToken 继承 AbstractAuthenticationToken 实现 Authentication
            //验证用户携带的token是否合法，并解析出用户信息，交给SpringSecurity，以便于后续的授权功能可以正常使用。
            UsernamePasswordAuthenticationToken authentication = getAuthentication(header, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error("",e);
        }
        // 放行
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String header, HttpServletResponse response) {
        //用户名
        String username = null;
        //权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        try {
            //解析token
            Claims claims = Jwts.parser().setSigningKey(SecurityConstant.JWT_SIGN_KEY).parseClaimsJws(header.replace(SecurityConstant.TOKEN_SPLIT, "")).getBody();
            logger.info("claims：" + claims);
            //获取用户名
            username = claims.getSubject();
            logger.info("username：" + username);
            //获取权限
            String authority = claims.get(SecurityConstant.AUTHORITIES).toString();
            logger.info("authority：" + authority);
            if (StringUtils.hasLength(authority)) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        } catch (ExpiredJwtException e) {
            ResponseOutUtil.out(response, ResultUtil.response(ResponseCode.UNAUTHORIZED, "登录已失效，请重新登录"));
        } catch (Exception e) {
            log.error(e.toString());
            ResponseOutUtil.out(response, ResultUtil.response(ResponseCode.INTERNAL_SERVER_ERROR, "解析token错误"));
        }

        if (StringUtils.hasLength(username)) {
            //踩坑提醒 此处password不能为null
            LoginUser principal = new LoginUser(username, "");
            return new UsernamePasswordAuthenticationToken(principal, null, authorities);
        }
        return null;
    }
    //    service  a  littleNote   check   hasAuth(security)    yes/no
    //                                                                         auth(login)
    //    service  b  server     check     hasAuth(security)    yes/no
    //    user info?
}
