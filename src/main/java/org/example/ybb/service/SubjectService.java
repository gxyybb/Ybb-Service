package org.example.ybb.service;

import org.example.ybb.domain.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.ybb.domain.Word;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* @author 14847
* @description 针对表【subject】的数据库操作Service
* @createDate 2025-03-05 20:51:38
*/
public interface SubjectService extends IService<Subject> {


    public List<Word> getWordsBySubject(Integer subjectId);
}
