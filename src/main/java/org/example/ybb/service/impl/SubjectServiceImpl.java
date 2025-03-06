package org.example.ybb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.ybb.domain.Subject;
import org.example.ybb.domain.SubjectWord;
import org.example.ybb.domain.Word;
import org.example.ybb.mapper.SubjectWordMapper;
import org.example.ybb.mapper.WordMapper;
import org.example.ybb.service.SubjectService;
import org.example.ybb.mapper.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 14847
* @description 针对表【subject】的数据库操作Service实现
* @createDate 2025-03-05 20:51:38
*/
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject>
    implements SubjectService {

    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private WordMapper wordMapper;
    @Autowired
    private SubjectWordMapper subjectWordMapper;
    @Override
    public List<Word> getWordsBySubject(Integer subjectId) {
        QueryWrapper<SubjectWord> subjectWordQueryWrapper = new QueryWrapper<>();
        subjectWordQueryWrapper.eq("subject_id", subjectId);
        return subjectWordMapper.selectList(subjectWordQueryWrapper).stream().map(
                SubjectWord::getWordId
        ).map(
                wordMapper::selectById
        ).toList();
    }
}




