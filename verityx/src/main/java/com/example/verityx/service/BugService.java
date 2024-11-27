package com.example.verityx.service;

import com.example.verityx.entity.Bug;
import com.example.verityx.entity.TestCase;

import java.util.List;

public interface BugService {

    Bug createBug(Bug bug);

    Bug getBugById(int bugId);

    List<Bug> getAllBugs();

    List<Bug> getBugsWithPagination(int pageSize, int offset, String status);

    int getBugCount(String status);

    boolean updateBug(Bug bug);

    boolean deleteBug(int bugId);
}
