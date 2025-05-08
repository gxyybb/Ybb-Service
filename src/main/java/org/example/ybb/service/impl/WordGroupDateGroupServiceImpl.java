package org.example.ybb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.ybb.domain.WordGroupDateGroup;
import org.example.ybb.service.WordGroupDateGroupService;
import org.example.ybb.mapper.WordGroupDateGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 14847
* @description 针对表【word_group_date_group】的数据库操作Service实现
* @createDate 2025-03-06 10:18:15
*/
@Service
public class WordGroupDateGroupServiceImpl extends ServiceImpl<WordGroupDateGroupMapper, WordGroupDateGroup>
    implements WordGroupDateGroupService {

    @Autowired
    private WordGroupDateGroupMapper wordGroupDateGroupMapper;

    @Override
    public List<WordGroupDateGroup> studyPlan(Integer subjectId, Integer userId) {
        return wordGroupDateGroupMapper.studyPlan(subjectId,userId);
    }
}




