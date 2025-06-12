package com.lww.sandwich.entity.server;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

/**
 * 內存相关信息
 *
 * @author valarchie
 */
@Data
public class MemoryInfo {

    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

    public double getTotal() {
        return NumberUtil.div(total, JvmInfo.GB, 2);
    }

    public double getUsed() {
        return NumberUtil.div(used, JvmInfo.GB, 2);
    }

    public double getFree() {
        return NumberUtil.div(free, JvmInfo.GB, 2);
    }

    public double getUsage() {
        return NumberUtil.div(used * 100, total, 2);
    }
}
