package com.example.verityx.service;

import com.example.verityx.entity.ApiTestReport;
import com.example.verityx.mapper.ApiTestReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiTestReportServiceImpl implements ApiTestReportService {

    @Autowired
    private ApiTestReportMapper apiTestReportMapper;

    @Override
    public ApiTestReport createReport(ApiTestReport report) {
        apiTestReportMapper.insertReport(report);
        return report;
    }

    @Override
    public ApiTestReport getReportById(int reportId) {
        return apiTestReportMapper.selectReportById(reportId);
    }

    @Override
    public Map<String, Object> getReportsByPage(String keyword, String status, String projectId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        
        // 查询数据
        List<ApiTestReport> reports = apiTestReportMapper.selectReportsByPage(keyword, status, projectId, offset, pageSize);
        int total = apiTestReportMapper.countReports(keyword, status, projectId);
        
        result.put("data", reports);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        
        return result;
    }

    @Override
    public boolean updateReport(ApiTestReport report) {
        return apiTestReportMapper.updateReport(report) > 0;
    }

    @Override
    public boolean deleteReport(int reportId) {
        return apiTestReportMapper.deleteReport(reportId) > 0;
    }
    
    @Override
    public boolean batchDeleteReports(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return apiTestReportMapper.batchDeleteReports(ids) > 0;
    }
} 