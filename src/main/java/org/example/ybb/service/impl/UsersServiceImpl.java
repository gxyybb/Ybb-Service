package org.example.ybb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.ybb.domain.Users;
import org.example.ybb.service.UsersService;
import org.example.ybb.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author 14847
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-03-01 20:02:35
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService {

}




