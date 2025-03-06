package org.example.ybb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.ybb.common.utils.common.utils.TokenUtil;
import org.example.ybb.domain.Users;
import org.example.ybb.service.UsersService;
import org.example.ybb.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
* @author 14847
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-03-01 20:02:35
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService {
    @Autowired
    private UsersMapper userMapper;

    @Override
    public Integer login(String username, String password) {
        Users user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Users>()
                        .eq(Users::getUsername, username)
        );
        if (user != null) {
            return user.getId();
        }
        return 0;
    }
    @Override
    public String generateToken(Integer userId) {
        // 这里简单返回一个模拟 token，实际可以用 JWT 或其他方式生成

        return  TokenUtil.generateToken(userId.toString());
    }

    public boolean register(String username, String password) {
        Users existingUser = userMapper.findByUsername(username);
        if (existingUser != null) {
            return false;
        }
        Users newUser = new Users();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userMapper.insert(newUser);
        return true;
    }
}




