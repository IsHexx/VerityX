package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysDictDataDTO;
import com.example.verityx.dto.SysDictTypeDTO;
import com.example.verityx.dto.request.SysDictDataCreateRequest;
import com.example.verityx.dto.request.SysDictDataUpdateRequest;
import com.example.verityx.dto.request.SysDictTypeCreateRequest;
import com.example.verityx.dto.request.SysDictTypeUpdateRequest;
import com.example.verityx.entity.SysDictData;
import com.example.verityx.entity.SysDictType;
import com.example.verityx.mapper.SysDictDataMapper;
import com.example.verityx.mapper.SysDictTypeMapper;
import com.example.verityx.service.SysDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典管理服务实现类
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictTypeMapper dictTypeMapper;
    
    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Override
    public IPage<SysDictTypeDTO> getDictTypePage(int page, int size, String dictName, String dictType, Boolean status) {
        // 构建查询条件
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(dictName)) {
            queryWrapper.like(SysDictType::getDictName, dictName);
        }
        if (StringUtils.hasText(dictType)) {
            queryWrapper.like(SysDictType::getDictType, dictType);
        }
        if (status != null) {
            queryWrapper.eq(SysDictType::getStatus, status);
        }
        
        queryWrapper.orderByDesc(SysDictType::getCreatedAt);
        
        // 执行分页查询
        Page<SysDictType> typePage = new Page<>(page, size);
        Page<SysDictType> resultPage = dictTypeMapper.selectPage(typePage, queryWrapper);
        
        // 转换为DTO
        Page<SysDictTypeDTO> dtoPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<SysDictTypeDTO> dtoList = resultPage.getRecords().stream()
                .map(this::convertTypeToDTO)
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        
        return dtoPage;
    }

    @Override
    public SysDictTypeDTO getDictTypeById(Long id) {
        SysDictType dictType = dictTypeMapper.selectById(id);
        return dictType != null ? convertTypeToDTO(dictType) : null;
    }

    @Override
    public SysDictTypeDTO getDictTypeByType(String dictType) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getDictType, dictType);
        queryWrapper.eq(SysDictType::getStatus, true);
        
        SysDictType type = dictTypeMapper.selectOne(queryWrapper);
        return type != null ? convertTypeToDTO(type) : null;
    }

    @Override
    public List<SysDictTypeDTO> getAllDictTypes() {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getStatus, true);
        queryWrapper.orderByAsc(SysDictType::getDictName);
        
        List<SysDictType> typeList = dictTypeMapper.selectList(queryWrapper);
        
        return typeList.stream()
                .map(this::convertTypeToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long createDictType(SysDictTypeCreateRequest request, Long operator) {
        // 检查字典类型是否已存在
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getDictType, request.getDictType());
        if (dictTypeMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("字典类型已存在");
        }
        
        // 创建字典类型
        SysDictType dictType = new SysDictType();
        BeanUtils.copyProperties(request, dictType);
        dictType.setCreatedAt(LocalDateTime.now());
        dictType.setCreatedBy(operator);
        dictType.setUpdatedAt(LocalDateTime.now());
        dictType.setUpdatedBy(operator);
        
        dictTypeMapper.insert(dictType);
        
        return dictType.getId();
    }

    @Override
    @Transactional
    public boolean updateDictType(SysDictTypeUpdateRequest request, Long operator) {
        // 检查字典类型是否存在
        SysDictType existType = dictTypeMapper.selectById(request.getId());
        if (existType == null) {
            throw new RuntimeException("字典类型不存在");
        }
        
        // 如果修改了字典类型，检查新类型是否已存在
        if (!existType.getDictType().equals(request.getDictType())) {
            LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysDictType::getDictType, request.getDictType())
                     .ne(SysDictType::getId, request.getId());
            if (dictTypeMapper.selectCount(queryWrapper) > 0) {
                throw new RuntimeException("字典类型已存在");
            }
        }
        
        // 更新字典类型
        SysDictType dictType = new SysDictType();
        BeanUtils.copyProperties(request, dictType);
        dictType.setUpdatedAt(LocalDateTime.now());
        dictType.setUpdatedBy(operator);
        
        int rows = dictTypeMapper.updateById(dictType);
        
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean deleteDictType(Long id, Long operator) {
        // 检查字典类型是否存在
        if (dictTypeMapper.selectById(id) == null) {
            throw new RuntimeException("字典类型不存在");
        }
        
        // 删除字典类型
        int rows = dictTypeMapper.deleteById(id);
        
        // 删除对应的字典数据
        if (rows > 0) {
            SysDictType dictType = dictTypeMapper.selectById(id);
            if (dictType != null) {
                LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SysDictData::getDictType, dictType.getDictType());
                dictDataMapper.delete(queryWrapper);
            }
        }
        
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean batchDeleteDictType(List<Long> ids, Long operator) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // 删除字典类型
        int rows = dictTypeMapper.deleteBatchIds(ids);
        
        // 删除对应的字典数据
        if (rows > 0) {
            LambdaQueryWrapper<SysDictType> typeQuery = new LambdaQueryWrapper<>();
            typeQuery.in(SysDictType::getId, ids);
            List<SysDictType> typeList = dictTypeMapper.selectList(typeQuery);
            
            if (!typeList.isEmpty()) {
                List<String> dictTypes = typeList.stream()
                        .map(SysDictType::getDictType)
                        .collect(Collectors.toList());
                
                LambdaQueryWrapper<SysDictData> dataQuery = new LambdaQueryWrapper<>();
                dataQuery.in(SysDictData::getDictType, dictTypes);
                dictDataMapper.delete(dataQuery);
            }
        }
        
        return rows > 0;
    }

    @Override
    public IPage<SysDictDataDTO> getDictDataPage(int page, int size, String dictType, String dictLabel, Boolean status) {
        // 构建查询条件
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(dictType)) {
            queryWrapper.eq(SysDictData::getDictType, dictType);
        }
        if (StringUtils.hasText(dictLabel)) {
            queryWrapper.like(SysDictData::getDictLabel, dictLabel);
        }
        if (status != null) {
            queryWrapper.eq(SysDictData::getStatus, status);
        }
        
        queryWrapper.orderByAsc(SysDictData::getDictType);
        queryWrapper.orderByAsc(SysDictData::getDictSort);
        
        // 执行分页查询
        Page<SysDictData> dataPage = new Page<>(page, size);
        Page<SysDictData> resultPage = dictDataMapper.selectPage(dataPage, queryWrapper);
        
        // 转换为DTO
        Page<SysDictDataDTO> dtoPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<SysDictDataDTO> dtoList = resultPage.getRecords().stream()
                .map(this::convertDataToDTO)
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        
        return dtoPage;
    }

    @Override
    public SysDictDataDTO getDictDataById(Long id) {
        SysDictData dictData = dictDataMapper.selectById(id);
        return dictData != null ? convertDataToDTO(dictData) : null;
    }

    @Override
    public List<SysDictDataDTO> getDictDataByType(String dictType) {
        LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictData::getDictType, dictType);
        queryWrapper.eq(SysDictData::getStatus, true);
        queryWrapper.orderByAsc(SysDictData::getDictSort);
        
        List<SysDictData> dataList = dictDataMapper.selectList(queryWrapper);
        
        return dataList.stream()
                .map(this::convertDataToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long createDictData(SysDictDataCreateRequest request, Long operator) {
        // 检查字典类型是否存在
        LambdaQueryWrapper<SysDictType> typeQuery = new LambdaQueryWrapper<>();
        typeQuery.eq(SysDictType::getDictType, request.getDictType());
        if (dictTypeMapper.selectCount(typeQuery) == 0) {
            throw new RuntimeException("字典类型不存在");
        }
        
        // 创建字典数据
        SysDictData dictData = new SysDictData();
        BeanUtils.copyProperties(request, dictData);
        dictData.setCreatedAt(LocalDateTime.now());
        dictData.setCreatedBy(operator);
        dictData.setUpdatedAt(LocalDateTime.now());
        dictData.setUpdatedBy(operator);
        
        dictDataMapper.insert(dictData);
        
        return dictData.getId();
    }

    @Override
    @Transactional
    public boolean updateDictData(SysDictDataUpdateRequest request, Long operator) {
        // 检查字典数据是否存在
        if (dictDataMapper.selectById(request.getId()) == null) {
            throw new RuntimeException("字典数据不存在");
        }
        
        // 检查字典类型是否存在
        LambdaQueryWrapper<SysDictType> typeQuery = new LambdaQueryWrapper<>();
        typeQuery.eq(SysDictType::getDictType, request.getDictType());
        if (dictTypeMapper.selectCount(typeQuery) == 0) {
            throw new RuntimeException("字典类型不存在");
        }
        
        // 更新字典数据
        SysDictData dictData = new SysDictData();
        BeanUtils.copyProperties(request, dictData);
        dictData.setUpdatedAt(LocalDateTime.now());
        dictData.setUpdatedBy(operator);
        
        int rows = dictDataMapper.updateById(dictData);
        
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean deleteDictData(Long id, Long operator) {
        // 检查字典数据是否存在
        if (dictDataMapper.selectById(id) == null) {
            throw new RuntimeException("字典数据不存在");
        }
        
        int rows = dictDataMapper.deleteById(id);
        
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean batchDeleteDictData(List<Long> ids, Long operator) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        int rows = dictDataMapper.deleteBatchIds(ids);
        
        return rows > 0;
    }
    
    /**
     * 将字典类型实体转换为DTO
     */
    private SysDictTypeDTO convertTypeToDTO(SysDictType dictType) {
        if (dictType == null) {
            return null;
        }
        
        SysDictTypeDTO dto = new SysDictTypeDTO();
        BeanUtils.copyProperties(dictType, dto);
        
        // 处理特殊字段
        
        return dto;
    }
    
    /**
     * 将字典数据实体转换为DTO
     */
    private SysDictDataDTO convertDataToDTO(SysDictData dictData) {
        if (dictData == null) {
            return null;
        }
        
        SysDictDataDTO dto = new SysDictDataDTO();
        BeanUtils.copyProperties(dictData, dto);
        
        // 处理特殊字段
        
        return dto;
    }
} 