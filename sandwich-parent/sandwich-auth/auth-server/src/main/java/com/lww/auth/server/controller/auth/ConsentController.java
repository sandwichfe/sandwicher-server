package com.lww.auth.server.controller.auth;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Set;

/**
 * 自定义授权页面
 */

@Controller
public class ConsentController {

    @Resource
    private RegisteredClientRepository registeredclientRepository;

    @GetMapping("/consent")
    public String consent(HttpServletRequest request, Principal principal, @RequestParam(OAuth2ParameterNames.CLIENT_ID)
    String clientId, @RequestParam(OAuth2ParameterNames.STATE) String state) {
        //获取认证用户的name
        String principalName = principal.getName();
        // 根据clientId获取客户端应用
        RegisteredClient registeredclient = registeredclientRepository.findByClientId(clientId);
        // 获取客户端应用名
        String clientName = registeredclient.getClientName();
        // 获取回调地址
        String redirecturi = registeredclient.getRedirectUris().iterator().next();
        // 获取 scopes
        Set<String> scopes = registeredclient.getScopes();
        //存储principal
        request.setAttribute("principalName",principalName);
        // 存储客户端应用名
        request.setAttribute("clientName",clientName);
        // 存储clientId
        request.setAttribute("clientId",clientId);
        //存储state
        request.setAttribute("state", state);
        // 存储scopes
        request.setAttribute("scopes",scopes);
        // 返回consent.html
        return "consent";
    }

}
