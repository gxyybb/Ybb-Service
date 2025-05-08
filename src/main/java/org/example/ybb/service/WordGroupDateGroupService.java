package org.example.ybb.service;

import org.apache.ibatis.annotations.Param;
import org.example.ybb.domain.WordGroupDateGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 14847
* @description 针对表【word_group_date_group】的数据库操作Service
* @createDate 2025-03-06 10:18:15
*/
@Service
public interface WordGroupDateGroupService extends IService<WordGroupDateGroup> {
    List<WordGroupDateGroup> studyPlan( Integer subjectId, Integer userId);
}
