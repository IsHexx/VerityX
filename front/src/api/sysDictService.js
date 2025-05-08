import { http } from '@/utils/request'

export const SysDictApi = {
  // 获取字典类型分页列表
  getDictTypePage: (page, size, dictName, dictType, status) => {
    const params = { page, size };
    if (dictName) params.dictName = dictName;
    if (dictType) params.dictType = dictType;
    if (status !== undefined) params.status = status;
    
    return http.get('/api/sys/dict/types', { params });
  },
  
  // 获取所有字典类型列表
  getAllDictTypes: () => {
    return http.get('/api/sys/dict/types/all');
  },
  
  // 获取字典类型详情
  getDictTypeById: (id) => {
    return http.get(`/api/sys/dict/types/${id}`);
  },
  
  // 根据类型获取字典类型
  getDictTypeByType: (dictType) => {
    return http.get(`/api/sys/dict/types/type/${dictType}`);
  },
  
  // 创建字典类型
  createDictType: (data) => {
    return http.post('/api/sys/dict/types', data);
  },
  
  // 更新字典类型
  updateDictType: (data) => {
    return http.put('/api/sys/dict/types', data);
  },
  
  // 删除字典类型
  deleteDictType: (id) => {
    return http.delete(`/api/sys/dict/types/${id}`);
  },
  
  // 批量删除字典类型
  batchDeleteDictType: (ids) => {
    return http.delete('/api/sys/dict/types/batch', { data: ids });
  },
  
  // 获取字典数据分页列表
  getDictDataPage: (page, size, dictType, dictLabel, status) => {
    const params = { page, size };
    if (dictType) params.dictType = dictType;
    if (dictLabel) params.dictLabel = dictLabel;
    if (status !== undefined) params.status = status;
    
    return http.get('/api/sys/dict/data', { params });
  },
  
  // 获取字典数据详情
  getDictDataById: (id) => {
    return http.get(`/api/sys/dict/data/${id}`);
  },
  
  // 根据字典类型获取字典数据列表
  getDictDataByType: (dictType) => {
    return http.get(`/api/sys/dict/data/type/${dictType}`);
  },
  
  // 创建字典数据
  createDictData: (data) => {
    return http.post('/api/sys/dict/data', data);
  },
  
  // 更新字典数据
  updateDictData: (data) => {
    return http.put('/api/sys/dict/data', data);
  },
  
  // 删除字典数据
  deleteDictData: (id) => {
    return http.delete(`/api/sys/dict/data/${id}`);
  },
  
  // 批量删除字典数据
  batchDeleteDictData: (ids) => {
    return http.delete('/api/sys/dict/data/batch', { data: ids });
  }
};

export default SysDictApi; 