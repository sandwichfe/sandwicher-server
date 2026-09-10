package com.lww.operationlog.config;

import com.lww.operationlog.OperationLogAspect;
import com.lww.operationlog.mapper.OperationLogMapper;
import com.lww.operationlog.service.OperationLogService;
import com.lww.operationlog.service.impl.OperationLogServiceImpl;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 操作日志自动配置，仅在 Servlet Web 应用中启用。
 *
 * @author lww
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "sandwich.operation-log", name = "enabled", havingValue = "true", matchIfMissing = true)
@MapperScan(basePackageClasses = OperationLogMapper.class)
public class OperationLogAutoConfiguration {

    /**
     * 提供默认的操作日志持久化服务，业务应用可声明同类型 Bean 覆盖。
     *
     * @return 操作日志服务
     */
    @Bean
    @ConditionalOnMissingBean(OperationLogService.class)
    public OperationLogService operationLogService() {
        return new OperationLogServiceImpl();
    }

    /**
     * 注册操作日志切面，统一采集带有 {@code Loggable} 注解的方法。
     *
     * @param regionSearcher       IP 归属地查询器
     * @param operationLogService 操作日志服务
     * @return 操作日志切面
     */
    @Bean
    @ConditionalOnMissingBean(OperationLogAspect.class)
    public OperationLogAspect operationLogAspect(Ip2regionSearcher regionSearcher,
                                                   OperationLogService operationLogService) {
        return new OperationLogAspect(regionSearcher, operationLogService);
    }
}
