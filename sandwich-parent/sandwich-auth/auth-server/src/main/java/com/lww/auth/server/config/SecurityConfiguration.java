package com.lww.auth.server.config;

import com.lww.auth.server.oauth2.handler.MyAuthenticationFailureHandler;
import com.lww.auth.server.oauth2.handler.MyAuthenticationSuccessHandler;
import com.lww.auth.server.oauth2.password.PasswordAuthenticationConverter;
import com.lww.auth.server.oauth2.password.PasswordAuthenticationProvider;
import com.lww.auth.server.utils.SecurityUtils;
import com.lww.redis.util.RedisUtil;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
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
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.cors.CorsConfiguration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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
     * jwk set缓存前缀
     */
    public static final String AUTHORIZATION_JWS_PREFIX_KEY = "authorization_jws";

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
                        .accessTokenResponseHandler(new MyAuthenticationSuccessHandler()) // 自定义成功响应
                        .errorResponseHandler(new MyAuthenticationFailureHandler()) // 自定义失败响应
                );

        // 未认证的请求异常处理（/Login）    指向到login地址
        http.exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new LoginAuthenticationJsonEntryPoint())
                )
                // 接受用户信息和/或客户端注册的访问令牌
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(Customizer.withDefaults())
                        // 异常处理
                        .accessDeniedHandler(SecurityUtils::exceptionHandler)
                        // 认证失败处理
                        .authenticationEntryPoint(SecurityUtils::exceptionHandler)
                );

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
        http
                // 关闭csrf 要写在requestMatchers之前
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.setAllowedOriginPatterns(List.of("*")); // 允许所有来源
                    config.addAllowedHeader("*"); // 允许所有头部
                    config.addAllowedMethod("*"); // 允许所有方法
                    return config;
                }))
                .authorizeHttpRequests(authorize -> authorize
                        // 不拦截
                        .requestMatchers(new String[]{"/assets/**", "/webjars/**", "/login", "/logout", "/oauth2/token/**"}).permitAll()
                        // 用户登录相关
                        .requestMatchers(new String[]{"/user/login","/user/slider/generate","/user/slider/verify","/user/qrCode/**"}).permitAll()
                        // swagger
                        .requestMatchers(new String[]{"/swagger-resources/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                // swagger-boostrap-ui
                                "/doc.html",}).permitAll()
                        // 其他请求需要认证
                        .anyRequest().authenticated());
        http.formLogin(form -> form
                        .disable()
                        // 自定义登录页面
                        // .loginPage("/login")
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
                        if (Objects.nonNull(userDetail) && userDetail instanceof SecurityUserDetails userDetails) {
                            claim.put("userId", userDetails.getUserId());
                            claim.put("userName", userDetails.getUsername());
                        }
                        claim.put("authorities", authorities);
                    });
                }
            }
        };
    }

    /**
     * 密码加密方式  DelegatingPasswordEncoder。这种编码器可以根据密码的前缀（如{bcrypt}）自动选择对应的编码器
     * 如果设置成 BCryptPasswordEncoder反而有问题  影响到了oauth客户端的？
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
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



    /**
     * 配置jwk源，使用非对称加密，公开用于检索匹配指定选择器的JWK的方法
     *
     * @return JWKSource
     */
    @Bean
    @SneakyThrows
    public JWKSource<SecurityContext> jwkSource(RedisUtil redisUtil) {
        // 先从redis获取 加密密钥    使用默认的生成策略 每次重启都会生成新的jws公钥私钥 那么之前的签发的token就会失效(授权服务器同时作为资源服务器才会这样) 用户就要重新登录 体验差 所以把jws用redis缓存起来
        String jwkSetCache = redisUtil.get(AUTHORIZATION_JWS_PREFIX_KEY);
        if (ObjectUtils.isEmpty(jwkSetCache)) {
            KeyPair keyPair = generateRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
            // 生成jws
            JWKSet jwkSet = new JWKSet(rsaKey);
            // 转为json字符串
            String jwkSetString = jwkSet.toString(Boolean.FALSE);
            // 存入redis
            redisUtil.set(AUTHORIZATION_JWS_PREFIX_KEY, jwkSetString);
            return new ImmutableJWKSet<>(jwkSet);
        }
        // 解析存储的jws
        JWKSet jwkSet = JWKSet.parse(jwkSetCache);
        return new ImmutableJWKSet<>(jwkSet);
    }



    public JWKSource<SecurityContext> jwkSourcedefault() throws Exception {
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


    /**
     * 生成RSA密钥对 默认的方式
     * @return
     */
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

    /**
     * springSecurity 的用户
     *
     * @author lww
     * @since 2024/11/26

     @Bean
     public UserDetailsService userDetailsService() {
     UserDetails userDetails = User.withDefaultPasswordEncoder()
     .username("lww")
     .password("123456")
     .roles("USER")
     .build();
     return new InMemoryUserDetailsManager(userDetails);
     } */

}