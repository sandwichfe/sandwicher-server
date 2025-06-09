package com.lww.sandwich.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.common.web.vo.PageDataVo;
import com.lww.common.web.vo.PageVo;
import com.lww.sandwich.entity.DictType;
import com.lww.sandwich.mapper.DictTypeMapper;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.sandwich.service.DictTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 字典类型表 服务实现类
 * @author lww
 * @since 2022-04-07 16:00:02
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    @Resource
    private DictTypeMapper dictTypeMapper;

    @Override
    public ResponseResult<PageDataVo<List<DictType>>> getPage(PageVo pageVo) {
        // !!! pageSize设置为-1 即可查询为全部
        Page<DictType> page = new Page<>(pageVo.getPageNum(),pageVo.getPageSize());
        //分页，默认查询所有的记录
        Page<DictType> accountPage = dictTypeMapper.selectPage(page, null);
        List<DictType> records = accountPage.getRecords();
        PageDataVo<List<DictType>> pageDataVo = new PageDataVo<>(accountPage.getCurrent(),accountPage.getTotal(),records);
        return ResultUtil.success(pageDataVo);
    }
}
