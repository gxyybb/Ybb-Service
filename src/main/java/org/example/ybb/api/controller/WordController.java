package org.example.ybb.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.constraints.Null;
import org.example.ybb.common.utils.common.utils.EbbinghausStudyPlan;
import org.example.ybb.common.utils.common.utils.TokenUtil;
import org.example.ybb.common.vo.ResultVO;
import org.example.ybb.domain.*;
import org.example.ybb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/word")
public class WordController {

    @Autowired
    private WordService wordService;
    @Autowired
    private WordGroupService wordGroupService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private WordGroupDateGroupService wordGroupDateGroupService;

    @Autowired
    private DateGroupService dateGroupService;

    @PostMapping
    public ResultVO<Null> addWord(@RequestBody Word word) {
        boolean save = wordService.save(word);
        return save? ResultVO.success(): ResultVO.error();
    }
    @DeleteMapping
    public ResultVO<Null> deleteWord(Integer id) {
        boolean b = wordService.removeById(id);
        return b? ResultVO.success(): ResultVO.error();
    }

    @PutMapping
    public ResultVO<Word> updateWord(@RequestBody Word word) {
        boolean b = wordService.updateById(word);
        return b? ResultVO.success(): ResultVO.error();
    }
    @GetMapping("/byGroup")
    public ResultVO<List<Word>> getWordByGroupId(Integer id, Integer subjectId) {
        QueryWrapper<WordGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("number", id);
        queryWrapper.eq("subject_id", subjectId);
        List<WordGroup> list = wordGroupService.list(queryWrapper);
        List<Word> list1 = list.stream().map(
                WordGroup::getWordId
        ).map(
                integer -> wordService.getById(integer)
        ).toList();
        return ResultVO.success(list1);
    }

    /**
     * 根据学科Id生成学习计划
     * @param token
     * @param subjectId
     * @param number
     * @return
     */

    @PostMapping("/generateStudyPlan")
    public ResultVO<Word> generateStudyPlan(String token,Integer subjectId,Integer number) {
        Integer userId = TokenUtil.getUserIdFromToken(token);
        if (userId== null || userId == 0) return ResultVO.error("用户信息认证失败");
        List<Word> wordsBySubject = subjectService.getWordsBySubject(subjectId);
        List<Integer> wordGroupIds = new ArrayList<>();
        List<List<Word>> lists = partitionList(wordsBySubject, number);
        for (int i = 0; i < lists.size(); i++) {
            List<Word> words = lists.get(i);
            for (Word word : words) {
                WordGroup wordGroup = new WordGroup();
                wordGroup.setWordId(word.getId());
                wordGroup.setUserId(userId);
                wordGroup.setSubjectId(subjectId);
                wordGroup.setNumber(i+1);
                wordGroupService.save(wordGroup);
                Integer wordGroupId = wordGroup.getId(); // 这里拿到保存后的 ID
                wordGroupIds.add(wordGroupId);
            }

        }
        List<List<Integer>> planForDays = EbbinghausStudyPlan.generateStudyPlan(wordGroupIds);
        int days = planForDays.size();
        List<Integer> daysIds = generateDays(days, userId,subjectId);
        for (int i = 0; i < planForDays.size(); i++) {
            List<Integer> list = planForDays.get(i);
            for (Integer wordGroupId : list ) {
                WordGroupDateGroup wordGroupDateGroup = new WordGroupDateGroup();
                wordGroupDateGroup.setWordGroupId(wordGroupId);
                wordGroupDateGroup.setDateGroupId(daysIds.get(i));
                wordGroupDateGroup.setCompletionStatus(0);
                wordGroupDateGroupService.save(wordGroupDateGroup);
            }

        }

        return ResultVO.success();
    }

    private List<Integer> generateDays(int days, Integer userId, Integer subjectId) {
        List<Integer> daysIds = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            DateGroup dateGroup = new DateGroup();
            dateGroup.setDate(i);
            dateGroup.setCompletionStatus(0);
            dateGroup.setSubjectId(subjectId);
            dateGroup.setUserId(userId);
            dateGroupService.save(dateGroup);
            daysIds.add(dateGroup.getId());
        }
        return daysIds;
    }


    private List<List<Word>> partitionList(List<Word> originalList, int number) {
        List<List<Word>> result = new ArrayList<>();
        for (int i = 0; i < originalList.size(); i += number) {
            int end = Math.min(i + number, originalList.size());
            result.add(originalList.subList(i, end));
        }
        return result;
    }



}
