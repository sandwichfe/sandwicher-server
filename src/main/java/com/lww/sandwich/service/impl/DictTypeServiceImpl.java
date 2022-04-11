package com.lww.sandwich.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.sandwich.entity.DictType;
import com.lww.sandwich.mapper.DictTypeMapper;
import com.lww.sandwich.pojo.Vo.PageVo;
import com.lww.sandwich.service.DictTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public List<DictType> getPage(PageVo pageVo) {
        //设置分页的条件，从第2的开始查询5条记录
        Page<DictType> page = new Page<>(pageVo.getPageNum(),pageVo.getPageSize());
        //分页，默认查询所有的记录
        Page<DictType> accountPage = dictTypeMapper.selectPage(page, null);
        List<DictType> records = accountPage.getRecords();
        return  records;
    }
}
