package com.lww.auth.server.portal.dict;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lww.auth.server.portal.entity.DictItem;
import com.lww.auth.server.portal.entity.DictType;
import com.lww.auth.server.portal.mapper.DictItemMapper;
import com.lww.auth.server.portal.mapper.DictTypeMapper;
import com.lww.core.dict.DictStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 将 auth-server 管理的字典数据同步到所有业务应用共享的 Redis。
 *
 * @author lww
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DictCacheSynchronizer implements ApplicationRunner {

    private final DictTypeMapper dictTypeMapper;

    private final DictItemMapper dictItemMapper;

    private final DictStore dictStore;

    @Override
    public void run(ApplicationArguments args) {
        List<DictType> dictTypes = dictTypeMapper.selectList(
                new LambdaQueryWrapper<DictType>().orderByAsc(DictType::getSort));
        for (DictType dictType : dictTypes) {
            refresh(dictType);
        }
        log.info("字典缓存初始化完成，共加载 {} 个字典类型", dictTypes.size());
    }

    /**
     * 数据库事务提交后再刷新缓存，避免事务回滚造成缓存与数据库不一致。
     *
     * @param event 字典缓存刷新事件
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void refreshAfterCommit(DictCacheRefreshEvent event) {
        try {
            if (event.staleTypeCode() != null) {
                dictStore.remove(event.staleTypeCode());
            }
            if (event.dictTypeId() == null) {
                return;
            }

            DictType dictType = dictTypeMapper.selectById(event.dictTypeId());
            if (dictType != null) {
                refresh(dictType);
            }
        } catch (RuntimeException e) {
            // 数据库事务已经提交，缓存异常只记录日志，避免接口返回失败造成重复操作。
            log.error("字典缓存刷新失败，dictTypeId={}", event.dictTypeId(), e);
        }
    }

    private void refresh(DictType dictType) {
        List<DictItem> items = dictItemMapper.selectList(new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getDictTypeId, dictType.getId())
                .orderByAsc(DictItem::getSort)
                .orderByAsc(DictItem::getId));
        Map<String, String> values = new LinkedHashMap<>(items.size());
        for (DictItem item : items) {
            values.put(item.getValue(), item.getLabel());
        }
        dictStore.replace(dictType.getTypeCode(), values);
    }
}
