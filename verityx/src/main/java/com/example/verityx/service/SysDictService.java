package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysDictDataDTO;
import com.example.verityx.dto.SysDictTypeDTO;
import com.example.verityx.dto.request.SysDictDataCreateRequest;
import com.example.verityx.dto.request.SysDictDataUpdateRequest;
import com.example.verityx.dto.request.SysDictTypeCreateRequest;
import com.example.verityx.dto.request.SysDictTypeUpdateRequest;

import java.util.List;

/**
 * 字典管理服务接口
 */
public interface SysDictService {
    
    /**
     * 获取字典类型分页列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param dictName 字典名称
     * @param dictType 字典类型
     * @param status 状态
     * @return 分页结果
     */
    IPage<SysDictTypeDTO> getDictTypePage(int page, int size, String dictName, String dictType, Boolean status);
    
    /**
     * 根据ID获取字典类型
     * 
     * @param id 字典类型ID
     * @return 字典类型信息
     */
    SysDictTypeDTO getDictTypeById(Long id);
    
    /**
     * 根据类型获取字典类型
     * 
     * @param dictType 字典类型
     * @return 字典类型信息
     */
    SysDictTypeDTO getDictTypeByType(String dictType);
    
    /**
     * 获取所有字典类型列表
     * 
     * @return 字典类型列表
     */
    List<SysDictTypeDTO> getAllDictTypes();
    
    /**
     * 创建字典类型
     * 
     * @param request 字典类型信息
     * @param operator 操作人ID
     * @return 字典类型ID
     */
    Long createDictType(SysDictTypeCreateRequest request, Long operator);
    
    /**
     * 更新字典类型
     * 
     * @param request 字典类型信息
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean updateDictType(SysDictTypeUpdateRequest request, Long operator);
    
    /**
     * 删除字典类型
     * 
     * @param id 字典类型ID
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean deleteDictType(Long id, Long operator);
    
    /**
     * 批量删除字典类型
     * 
     * @param ids 字典类型ID列表
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteDictType(List<Long> ids, Long operator);
    
    /**
     * 获取字典数据分页列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @param status 状态
     * @return 分页结果
     */
    IPage<SysDictDataDTO> getDictDataPage(int page, int size, String dictType, String dictLabel, Boolean status);
    
    /**
     * 根据ID获取字典数据
     * 
     * @param id 字典数据ID
     * @return 字典数据信息
     */
    SysDictDataDTO getDictDataById(Long id);
    
    /**
     * 根据字典类型获取字典数据列表
     * 
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<SysDictDataDTO> getDictDataByType(String dictType);
    
    /**
     * 创建字典数据
     * 
     * @param request 字典数据信息
     * @param operator 操作人ID
     * @return 字典数据ID
     */
    Long createDictData(SysDictDataCreateRequest request, Long operator);
    
    /**
     * 更新字典数据
     * 
     * @param request 字典数据信息
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean updateDictData(SysDictDataUpdateRequest request, Long operator);
    
    /**
     * 删除字典数据
     * 
     * @param id 字典数据ID
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean deleteDictData(Long id, Long operator);
    
    /**
     * 批量删除字典数据
     * 
     * @param ids 字典数据ID列表
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteDictData(List<Long> ids, Long operator);
} 