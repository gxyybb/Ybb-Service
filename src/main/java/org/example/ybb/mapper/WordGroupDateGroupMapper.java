package org.example.ybb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.ybb.domain.WordGroupDateGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 14847
* @description 针对表【word_group_date_group】的数据库操作Mapper
* @createDate 2025-03-06 10:18:15
* @Entity generator.domain.WordGroupDateGroup
*/
@Mapper
public interface WordGroupDateGroupMapper extends BaseMapper<WordGroupDateGroup> {
    @Select("""
    SELECT 
        wd.word_group_id AS wordGroupId,
        dg.date AS dateGroupId,
        wd.subject_id AS subjectId,
        wd.user_id AS userId,
        wd.completion_status AS completionStatus
    FROM 
        word_date wd
    JOIN 
        date_group dg ON wd.date_group_id = dg.id
    WHERE 
        wd.subject_id = #{subjectId}
        AND wd.user_id = #{userId}
    ORDER BY 
        dg.date ASC
""")
    List<WordGroupDateGroup> studyPlan(@Param("subjectId") Integer subjectId, @Param("userId") Integer userId);

}




