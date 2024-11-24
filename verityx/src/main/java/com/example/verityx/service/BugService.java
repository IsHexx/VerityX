package com.example.verityx.service;

import com.example.verityx.entity.Bug;

import java.util.List;

public interface BugService {

    Bug createBug(Bug bug);

    Bug getBugById(int bugId);

    List<Bug> getAllBugs();

    boolean updateBug(Bug bug);

    boolean deleteBug(int bugId);
}
