package org.example.ybb.api.controller;

import org.example.ybb.common.vo.ResultVO;
import org.example.ybb.domain.Subject;
import org.example.ybb.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResultVO<Subject> addSubject(@RequestBody Subject subject) {
        boolean save = subjectService.save(subject);
        return save ? ResultVO.success() : ResultVO.error();
    }

    @DeleteMapping
    public ResultVO<Subject> deleteSubject(Integer id) {
        boolean b = subjectService.removeById(id);
        return b ? ResultVO.success() : ResultVO.error();
    }

    @PutMapping
    public ResultVO<Subject> updateSubject(@RequestBody Subject subject) {
        boolean b = subjectService.updateById(subject);
        return b ? ResultVO.success() : ResultVO.error();
    }

    @GetMapping
    public ResultVO<List<Subject>> getAllSubjects() {
        List<Subject> list = subjectService.list();
        return ResultVO.success(list);
    }


}
