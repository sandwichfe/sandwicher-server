package com.lww.auth.server.config;

import com.lww.auth.server.oauth2.password.PasswordAuthenticationConverter;
import com.lww.auth.server.oauth2.password.PasswordAuthenticationProvider;
import com.lww.auth.server.utils.SecurityUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.ObjectUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.stream.Collectors;


/**
 * EnableWebSecurity 注解有两个作用:
 *  * 1. 加载了WebSecurityConfiguration配置类, 配置安全认证策略。
 *  * 2. 加载了AuthenticationConfiguration, 配置了认证信息。
 * EnableMethodSecurity 注解用于启用Security 方法权限注解。
 *
 * @description: Security配置
 * @author: lww
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
class SecurityConfiguration {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Lazy
    @Resource
    private  OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;

    /**
     * OAuth2AuthorizationServer配置
     *
     * @author lww
     * @since 2024/11/26
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      AuthenticationManager authenticationManager,
                                                                      OAuth2AuthorizationService authorizationService,
                                                                      OAuth2TokenGenerator<?> tokenGenerator) throws Exception {
        //授权服务器的安全交给security的过滤器处理
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // 自定义配置
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 自定义授权页
                .authorizationEndpoint(auth -> auth.consentPage("/consent"))
                // 开启oidc
                .oidc(Customizer.withDefaults());

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 自定义授权模式转换器(Converter)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        .accessTokenRequestConverters(
                                authenticationConverters -> // <1>
                                        // 自定义授权模式转换器(Converter)
                                        authenticationConverters.addAll(
                                                List.of(
                                                        new OAuth2ClientCredentialsAuthenticationConverter(),
                                                        // 加入密码模式转换器
                                                        new PasswordAuthenticationConverter(),
                                                        new OAuth2AuthorizationCodeAuthenticationConverter(),
                                                        new OAuth2RefreshTokenAuthenticationConverter()
                                                )
                                        )
                        )
                        .authenticationProviders(
                                authenticationProviders -> // <2>
                                        // 自定义授权模式提供者(Provider)
                                        authenticationProviders.addAll(
                                                List.of(
                                                        new OAuth2ClientCredentialsAuthenticationProvider(authorizationService, tokenGenerator),
                                                        // 加入密码Provider
                                                        new PasswordAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator),
                                                        new OAuth2AuthorizationCodeAuthenticationProvider(authorizationService, tokenGenerator),
                                                        new OAuth2RefreshTokenAuthenticationProvider(authorizationService, tokenGenerator)
                                                )
                                        )
                        )
                        // .accessTokenResponseHandler(new MyAuthenticationSuccessHandler()) // 自定义成功响应
                        // .errorResponseHandler(new MyAuthenticationFailureHandler()) // 自定义失败响应
                );

        // 未认证的请求异常处理（/Login）    指向到login地址
        http.exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // 接受用户信息和/或客户端注册的访问令牌
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

    /**
     * security配置
     *
     * @author lww
     * @since 2024/11/26
     */
    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // 不拦截
                .requestMatchers(new String[]{"/assets/**", "/webjars/**", "/login","/logout","/oauth2/token/**"}).permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated());
        http.formLogin(form -> form
                        // 自定义登录页面
                        .loginPage("/login")
                // 处理登录请求接口
                // .loginProcessingUrl("/login").permitAll()
        );
        // 添加BearerTokenAuthenticationFilter，将认证服务当做一个资源服务，解析请求头中的token
        http.oauth2ResourceServer((resourceServer) -> resourceServer
                .jwt(Customizer.withDefaults())
                // 异常处理
                .accessDeniedHandler(SecurityUtils::exceptionHandler)
                // 认证失败处理
                .authenticationEntryPoint(SecurityUtils::exceptionHandler)
        );
        return http.build();
    }

    /**
     * springSecurity 的用户
     *
     * @author lww
     * @since 2024/11/26
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("lww")
                .password("123456")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }


    /**
     * 好像 是 自定义 jwt内容的
     *
     * @author lww
     * @since 2024/11/27
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return (context) -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                Authentication authentication = context.getPrincipal();
                Object userDetail = authentication.getPrincipal();
                if (!ObjectUtils.isEmpty(authentication)) {
                    Set<String> authorities = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet());
                    context.getClaims().claims(claim -> {
                        if (Objects.nonNull(userDetail) && userDetail instanceof UserDetails userDetails) {
                            claim.put("userName", userDetails.getUsername());
                        }
                        claim.put("authorities", authorities);
                    });
                }
            }
        };
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 客户端应用注册
     * http://localhost:9000/oauth2/authorize?client_id=client_lww&redirect_uri=http://www.baidu.com&scope=read&response_type=code
     * @author lww
     * @since 2024/11/26
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository());
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }


    @Bean
    OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(jwtCustomizer);

        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}