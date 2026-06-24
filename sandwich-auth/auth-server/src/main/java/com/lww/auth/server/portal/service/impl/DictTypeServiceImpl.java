package com.lww.auth.server.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.portal.entity.DictItem;
import com.lww.auth.server.portal.entity.DictType;
import com.lww.auth.server.portal.mapper.DictItemMapper;
import com.lww.auth.server.portal.mapper.DictTypeMapper;
import com.lww.auth.server.portal.req.DictTypeReq;
import com.lww.auth.server.portal.service.DictTypeService;
import com.lww.auth.server.portal.vo.DictTypePageQuery;
import com.lww.auth.server.portal.vo.DictTypeVo;
import com.lww.common.utils.AssertUtils;
import com.lww.common.utils.CustomBeanUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    @Resource
    private DictItemMapper dictItemMapper;

    @Override
    public DictTypeVo createDictType(DictTypeReq req) {
        validateDictType(req, false);

        DictType dictType = new DictType();
        BeanUtils.copyProperties(req, dictType);
        this.save(dictType);
        return convertToVo(dictType);
    }

    @Override
    public DictTypeVo getDictTypeById(Long id) {
        return convertToVo(this.getById(id));
    }

    @Override
    public IPage<DictTypeVo> listDictType(DictTypePageQuery query) {
        Page<DictType> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<DictType> wrapper = buildListWrapper(query.getKeyword());
        return this.page(page, wrapper).convert(this::convertToVo);
    }

    @Override
    public List<DictTypeVo> listAllDictType() {
        return this.list(buildListWrapper(null))
                .stream()
                .map(this::convertToVo)
                .toList();
    }

    @Override
    public DictTypeVo updateDictType(DictTypeReq req) {
        validateDictType(req, true);

        DictType dictType = new DictType();
        BeanUtils.copyProperties(req, dictType);
        this.updateById(dictType);
        return convertToVo(this.getById(req.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteType(Long dictTypeId) {
        dictItemMapper.delete(new LambdaQueryWrapper<DictItem>().eq(DictItem::getDictTypeId, dictTypeId));
        this.removeById(dictTypeId);
    }

    /**
     * 校验字典类型基础字段，并限制类型编码重复。
     */
    private void validateDictType(DictTypeReq req, boolean checkId) {
        if (checkId) {
            AssertUtils.assertTrue(req.getId() == null, "ID不能为空");
        }
        AssertUtils.assertTrue(!StringUtils.hasText(req.getTypeCode()), "类型编码不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(req.getTypeName()), "类型名称不能为空");

        LambdaQueryWrapper<DictType> wrapper = new LambdaQueryWrapper<DictType>()
                .eq(DictType::getTypeCode, req.getTypeCode());
        if (checkId) {
            wrapper.ne(DictType::getId, req.getId());
        }
        boolean exists = this.count(wrapper) > 0;
        AssertUtils.assertTrue(exists, "类型编码已存在");
    }

    private LambdaQueryWrapper<DictType> buildListWrapper(String keyword) {
        LambdaQueryWrapper<DictType> wrapper = new LambdaQueryWrapper<DictType>()
                .orderByAsc(DictType::getSort)
                .orderByDesc(DictType::getId);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(DictType::getTypeCode, keyword)
                    .or()
                    .like(DictType::getTypeName, keyword));
        }
        return wrapper;
    }

    private DictTypeVo convertToVo(DictType dictType) {
        return CustomBeanUtils.copyProperties(dictType, DictTypeVo.class);
    }
}
