package com.lww.auth.server.portal.dict;

/**
 * 字典数据提交后刷新共享缓存的事件。
 *
 * @param dictTypeId    需要重建缓存的字典类型ID，删除类型时为空
 * @param staleTypeCode 需要移除的旧类型编码，类型编码修改或删除时使用
 * @author lww
 */
public record DictCacheRefreshEvent(Long dictTypeId, String staleTypeCode) {
}
