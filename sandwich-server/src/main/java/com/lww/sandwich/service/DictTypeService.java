package com.lww.sandwich.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.common.web.vo.PageDataVo;
import com.lww.common.web.vo.PageVo;
import com.lww.sandwich.entity.DictType;
import com.lww.common.web.response.ResponseResult;

import java.util.List;

/**
 * 字典类型表 服务类
 * @author lww
 * @since 2022-04-07 16:00:02
 */
public interface DictTypeService extends IService<DictType> {

    /**
     * 分页返回
     * @param pageVo 分页参数
     * @return PageDataVo<List<DictType>>
     */
    ResponseResult<PageDataVo<List<DictType>>> getPage(PageVo pageVo);
}
