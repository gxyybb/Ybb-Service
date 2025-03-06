package org.example.ybb.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.ybb.common.utils.common.utils.TokenUtil;
import org.example.ybb.common.vo.ResultVO;
import org.example.ybb.domain.DateGroup;
import org.example.ybb.domain.WordGroupDateGroup;
import org.example.ybb.service.DateGroupService;
import org.example.ybb.service.WordGroupDateGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/date")
public class DateController {


    @Autowired
    private DateGroupService dateGroupService;
    @Autowired
    private WordGroupDateGroupService wordGroupDateGroupService;

    /**
     * 根据日期获取该日期下的groupId
     * @param token
     * @param date
     * @return
     */


    @GetMapping("/getWordGroupIdByDate")
    public ResultVO<List<Integer>> getByDate(@RequestParam("token") String token,@RequestParam("date") Integer date, @RequestParam("subjectId")Integer subjectId) {
        Integer userId= TokenUtil.getUserIdFromToken(token);
        QueryWrapper<DateGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("date", date);
        queryWrapper.eq("subject_id", subjectId);
        List<Integer> wordGroupIds = dateGroupService.list(queryWrapper).stream()
                .map(DateGroup::getId)
                .flatMap(dateGroupId -> {
                    QueryWrapper<WordGroupDateGroup> wordGroupDateGroupQueryWrapper = new QueryWrapper<>();
                    wordGroupDateGroupQueryWrapper.eq("date_group_id", dateGroupId);

                    return wordGroupDateGroupService.list(wordGroupDateGroupQueryWrapper).stream()
                            .map(WordGroupDateGroup::getWordGroupId);
                })
                .toList();
        return ResultVO.success(wordGroupIds);
    }


}
