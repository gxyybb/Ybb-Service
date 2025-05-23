package org.example.ybb.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.validation.constraints.Null;
import org.example.ybb.api.Plan;
import org.example.ybb.api.PlanEntity;
import org.example.ybb.common.utils.common.utils.EbbinghausStudyPlan;
import org.example.ybb.common.utils.common.utils.TokenUtil;
import org.example.ybb.common.vo.ResultVO;
import org.example.ybb.domain.*;
import org.example.ybb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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



    @DeleteMapping("/deleteStudyPlan")
    public ResultVO<Word>deleteStudyPlan(String token,Integer subjectId){
        Integer userIdFromToken = TokenUtil.getUserIdFromToken(token);
        QueryWrapper<DateGroup> dateGroupqueryWrapper = new QueryWrapper<>();
        dateGroupqueryWrapper.eq("user_id",userIdFromToken);
        dateGroupqueryWrapper.eq("subject_id",subjectId);
        dateGroupService.remove(dateGroupqueryWrapper);
        QueryWrapper<WordGroup> wordGroupqueryWrapper = new QueryWrapper<>();
        wordGroupqueryWrapper.eq("user_id",userIdFromToken);
        wordGroupqueryWrapper.eq("subject_id",subjectId);
        wordGroupService.remove(wordGroupqueryWrapper);
        QueryWrapper<WordGroupDateGroup> wordGroupDateGroupQueryWrapper = new QueryWrapper<>();
        wordGroupDateGroupQueryWrapper.eq("subject_id",subjectId);
        wordGroupDateGroupQueryWrapper.eq("user_id",userIdFromToken);
        wordGroupDateGroupService.remove(wordGroupDateGroupQueryWrapper);
        return ResultVO.success();

    }


    @PostMapping("/generateStudyPlan")
    public ResultVO<Word> generateStudyPlan(@RequestBody PlanEntity plan) {
        String token = plan.getToken();
        Integer subject = plan.getSubject();
        int countByDay = plan.getCountByDay();
        deleteStudyPlan(token,subject);
        Integer userId = TokenUtil.getUserIdFromToken(token);
        if (userId== null || userId == 0) return ResultVO.error("用户信息认证失败");
        List<Word> wordsBySubject = subjectService.getWordsBySubject(subject);
        List<Integer> wordGroupIds = new ArrayList<>();
        List<List<Word>> lists = partitionList(wordsBySubject, countByDay);
        for (int i = 0; i < lists.size(); i++) {
            List<Word> words = lists.get(i);
            for (Word word : words) {
                WordGroup wordGroup = new WordGroup();
                wordGroup.setWordId(word.getId());
                wordGroup.setUserId(userId);
                wordGroup.setSubjectId(subject);
                wordGroup.setNumber(i+1);
                wordGroupService.save(wordGroup);
                wordGroupIds.add(i+1);
            }
        }
        Set<Integer> uniqueSet = new HashSet<>(wordGroupIds);

        wordGroupIds = new ArrayList<>(uniqueSet);
        System.out.println(wordGroupIds);
        List<List<Integer>> planForDays = EbbinghausStudyPlan.generateStudyPlan(wordGroupIds);
        int days = planForDays.size();
        List<Integer> daysIds = generateDays(days, userId,subject);
        for (int i = 0; i < planForDays.size(); i++) {
            List<Integer> list = planForDays.get(i);
            for (Integer wordGroupId : list ) {
                WordGroupDateGroup wordGroupDateGroup = new WordGroupDateGroup();
                wordGroupDateGroup.setWordGroupId(wordGroupId);
                wordGroupDateGroup.setDateGroupId(daysIds.get(i));
                wordGroupDateGroup.setSubjectId(subject);
                wordGroupDateGroup.setUserId(userId);
                wordGroupDateGroup.setCompletionStatus(0);
                wordGroupDateGroupService.save(wordGroupDateGroup);
            }

        }

        return ResultVO.success();
    }
    //获取计划内容 1
    @GetMapping("/getDateGroupIdByUser")
    public ResultVO<List<DateGroup>> getPlanList(String token, Integer subjectId) {
        QueryWrapper<DateGroup> dateGroupQueryWrapper = new QueryWrapper<>();
        dateGroupQueryWrapper.eq("user_id",TokenUtil.getUserIdFromToken(token));
        dateGroupQueryWrapper.eq("subject_id",subjectId);
        List<DateGroup> list = dateGroupService.list(dateGroupQueryWrapper);
        list.sort(Comparator.comparingInt(DateGroup::getDate));
        //获取日期
        Optional<DateGroup> maxDateGroup = list.stream()
                .filter(dateGroup -> dateGroup.getCompletionStatus() == 1) // 过滤completionStatus = 1
                .max(Comparator.comparingInt(DateGroup::getDate)); // 按date从大到小排序并取最大值

        return ResultVO.success(list);
    }
    //2
    @GetMapping("/getWordDateByDateGroupId")
    public ResultVO<List<WordGroupDateGroup>> getWordDate(Integer dateGroupId) {
        QueryWrapper<WordGroupDateGroup> wordGroupDateGroupQueryWrapper = new QueryWrapper<>();
        wordGroupDateGroupQueryWrapper.eq("date_group_id",dateGroupId);
        List<WordGroupDateGroup> list = wordGroupDateGroupService.list(wordGroupDateGroupQueryWrapper);
        return ResultVO.success(list);
    }
    //3
    @GetMapping("/getWordByWordDate")
    public ResultVO<List<Word>> getWord(String token, Integer subjectId,Integer wordGroupId) {
        QueryWrapper<WordGroup> wordGroupQueryWrapper = new QueryWrapper<>();
        wordGroupQueryWrapper.eq("user_id",TokenUtil.getUserIdFromToken(token));
        wordGroupQueryWrapper.eq("subject_id",subjectId);
        wordGroupQueryWrapper.eq("number",wordGroupId);
        List<Word> list1 = wordGroupService.list(wordGroupQueryWrapper).stream().map(WordGroup::getWordId).map(wordId ->
                wordService.getById(wordId)
        ).toList();
        return ResultVO.success(list1);
    }


    @GetMapping("/getPlanList")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ResultVO<List<Plan>> getPlanList(String token) {
        Integer userIdFromToken = TokenUtil.getUserIdFromToken(token);

        // 查询 DateGroup 表，获取每个 subject_id 下的最大 date
        List<Map<String, Object>> latestDates = dateGroupService.listMaps(new QueryWrapper<DateGroup>()
                .eq("user_id", userIdFromToken)
                .select("subject_id, MAX(date) as maxDate")
                .groupBy("subject_id")
        );

        // 查询 DateGroup 表，获取 completion_status = 1 的最大 date
        List<Map<String, Object>> maxIncompleteDates = dateGroupService.listMaps(new QueryWrapper<DateGroup>()
                .eq("user_id", userIdFromToken)
                .eq("completion_status", 1)
                .select("subject_id, MAX(date) as maxIncompleteDate")
                .groupBy("subject_id")
        );

        // 获取所有 subject_id
        Set<Integer> subjectIds = latestDates.stream()
                .map(map -> (Integer) map.get("subject_id"))
                .collect(Collectors.toSet());
        if (subjectIds.isEmpty()) {
            return ResultVO.success(); // 直接返回空结果，不执行SQL
        }
        // 查询 Subject 表，获取 subject 详情
        List<Subject> subjects = subjectService.list(new QueryWrapper<Subject>().in("id", subjectIds));

        // 构建 subjectId -> Subject 的映射
        Map<Integer, Subject> subjectMap = subjects.stream()
                .collect(Collectors.toMap(Subject::getId, subject -> subject));

        // 构建 subjectId -> maxDate 的映射
        Map<Integer, Integer> maxDateMap = latestDates.stream()
                .collect(Collectors.toMap(
                        map -> (Integer) map.get("subject_id"),
                        map -> (Integer) map.get("maxDate")
                ));

        // 构建 subjectId -> maxIncompleteDate 的映射，默认值为 -1
        Map<Integer, Integer> maxIncompleteDateMap = new HashMap<>();
        for (Map<String, Object> map : maxIncompleteDates) {
            Integer subjectId = (Integer) map.get("subject_id");
            Integer maxIncompleteDate = (Integer) map.get("maxIncompleteDate");
            maxIncompleteDateMap.put(subjectId, maxIncompleteDate);
        }

        List<Map<String, Object>> wordGroupCounts = wordGroupService.listMaps(new QueryWrapper<WordGroup>()
                .select("subject_id", "number", "COUNT(*) as cnt")
                .eq("user_id", userIdFromToken)
                .groupBy("subject_id", "number")
        );

        // 构建 subjectId -> maxCountByDay 的映射
        Map<Integer, Integer> countByDayMap = new HashMap<>();
        for (Map<String, Object> row : wordGroupCounts) {
            Integer subjectId = (Integer) row.get("subject_id");
            Long cnt = (Long) row.get("cnt");

            countByDayMap.merge(subjectId, cnt.intValue(), Math::max); // 保留最大值
        }

        // 组装 Plan 数据
        List<Plan> planList = subjectIds.stream()
                .map(subjectId -> {
                    Plan plan = new Plan();
                    plan.setSubject(subjectMap.get(subjectId));
                    plan.setTotalDays(maxDateMap.getOrDefault(subjectId, 0));
                    plan.setCompleteDays(maxIncompleteDateMap.getOrDefault(subjectId, 0));
                    plan.setCountByDay(countByDayMap.getOrDefault(subjectId, 0)); // ⭐ 设置 countByDay
                    return plan;
                })
                .collect(Collectors.toList());

        return ResultVO.success(planList);
    }

    @GetMapping("/studyPlan")
    public ResultVO<List<WordGroupDateGroup>> studyPlan(Integer subjectId,String token) {
       return ResultVO.success(wordGroupDateGroupService.studyPlan(subjectId,TokenUtil.getUserIdFromToken(token)));
    }

    @PostMapping("/completeList")
    public ResultVO<Boolean> completeList(@RequestBody WordGroupDateGroup wordGroupDateGroup,@RequestParam(required = false)Boolean isAllComplete ) {
        // 设置完成状态为1
        wordGroupDateGroup.setCompletionStatus(1);

        // 构造更新条件
        UpdateWrapper<WordGroupDateGroup> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", wordGroupDateGroup.getUserId());
        updateWrapper.eq("subject_id", wordGroupDateGroup.getSubjectId());
        updateWrapper.eq("word_group_id", wordGroupDateGroup.getWordGroupId());
        updateWrapper.eq("date_group_id", wordGroupDateGroup.getDateGroupId());

        // 执行更新
        boolean updateSuccess = wordGroupDateGroupService.update(wordGroupDateGroup, updateWrapper);
        if (isAllComplete) {
            UpdateWrapper<DateGroup> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("id", wordGroupDateGroup.getDateGroupId());

            DateGroup dateGroup = new DateGroup();
            dateGroup.setCompletionStatus(1);

            dateGroupService.update(dateGroup, updateWrapper1);
        }


        // 返回统一封装结果
        return ResultVO.success(updateSuccess);
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
