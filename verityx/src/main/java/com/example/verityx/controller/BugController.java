package com.example.verityx.controller;

import com.example.verityx.entity.Bug;
import com.example.verityx.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs")
public class BugController {

    @Autowired
    private BugService bugService;

    // 创建缺陷
    @PostMapping
    public Bug createBug(@RequestBody Bug bug) {
        return bugService.createBug(bug);
    }

    // 查询单个缺陷
    @GetMapping("/{id}")
    public Bug getBugById(@PathVariable int id) {
        return bugService.getBugById(id);
    }

    // 查询所有缺陷
    @GetMapping
    public List<Bug> getAllBugs() {
        return bugService.getAllBugs();
    }

    // 更新缺陷
    @PutMapping("/{id}")
    public boolean updateBug(@PathVariable int id, @RequestBody Bug bug) {
        bug.setBugId(id);
        return bugService.updateBug(bug);
    }

    // 删除缺陷
    @DeleteMapping("/{id}")
    public boolean deleteBug(@PathVariable int id) {
        return bugService.deleteBug(id);
    }
}
