package org.example.ybb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.ybb.domain.Word;
import org.example.ybb.service.WordService;
import org.example.ybb.mapper.WordMapper;
import org.springframework.stereotype.Service;

/**
* @author 14847
* @description 针对表【word】的数据库操作Service实现
* @createDate 2025-03-05 20:51:45
*/
@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word>
    implements WordService {

}




