package com.lww.auth.server.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.portal.entity.DictItem;
import com.lww.auth.server.portal.mapper.DictItemMapper;
import com.lww.auth.server.portal.req.DictItemReq;
import com.lww.auth.server.portal.service.DictItemService;
import com.lww.auth.server.portal.vo.DictItemPageQuery;
import com.lww.auth.server.portal.vo.DictItemVo;
import com.lww.common.utils.AssertUtils;
import com.lww.common.utils.CustomBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {

    @Override
    public DictItemVo createDictItem(DictItemReq req) {
        validateDictItem(req, false);

        DictItem dictItem = new DictItem();
        BeanUtils.copyProperties(req, dictItem);
        this.save(dictItem);
        return convertToVo(dictItem);
    }

    @Override
    public DictItemVo getDictItemById(Long id) {
        return convertToVo(this.getById(id));
    }

    @Override
    public IPage<DictItemVo> listDictItem(DictItemPageQuery query) {
        AssertUtils.assertTrue(query.getDictTypeId() == null, "字典类型ID不能为空");

        Page<DictItem> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<DictItem> wrapper = new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getDictTypeId, query.getDictTypeId())
                .orderByAsc(DictItem::getSort)
                .orderByDesc(DictItem::getUpdateTime);
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(DictItem::getLabel, query.getKeyword())
                    .or()
                    .like(DictItem::getValue, query.getKeyword()));
        }
        return this.page(page, wrapper).convert(this::convertToVo);
    }

    @Override
    public List<DictItemVo> listAllByDictTypeId(Long dictTypeId) {
        return this.list(new LambdaQueryWrapper<DictItem>()
                        .eq(DictItem::getDictTypeId, dictTypeId)
                        .orderByAsc(DictItem::getSort)
                        .orderByDesc(DictItem::getId))
                .stream()
                .map(this::convertToVo)
                .toList();
    }

    @Override
    public DictItemVo updateDictItem(DictItemReq req) {
        validateDictItem(req, true);

        DictItem dictItem = new DictItem();
        BeanUtils.copyProperties(req, dictItem);
        this.updateById(dictItem);
        return convertToVo(this.getById(req.getId()));
    }

    @Override
    public void deleteDictItem(Long id) {
        this.removeById(id);
    }

    /**
     * 校验字典项基础字段，并限制同一字典类型下 value 重复。
     */
    private void validateDictItem(DictItemReq req, boolean checkId) {
        if (checkId) {
            AssertUtils.assertTrue(req.getId() == null, "ID不能为空");
        }
        AssertUtils.assertTrue(req.getDictTypeId() == null, "字典类型ID不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(req.getLabel()), "标签不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(req.getValue()), "值不能为空");

        LambdaQueryWrapper<DictItem> wrapper = new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getDictTypeId, req.getDictTypeId())
                .eq(DictItem::getValue, req.getValue());
        if (checkId) {
            wrapper.ne(DictItem::getId, req.getId());
        }
        boolean exists = this.count(wrapper) > 0;
        AssertUtils.assertTrue(exists, "同一类型下字典值已存在");
    }

    private DictItemVo convertToVo(DictItem dictItem) {
        return CustomBeanUtils.copyProperties(dictItem, DictItemVo.class);
    }
}
