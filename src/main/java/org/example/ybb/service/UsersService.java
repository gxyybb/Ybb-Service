package org.example.ybb.service;

import org.example.ybb.domain.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 14847
* @description 针对表【users】的数据库操作Service
* @createDate 2025-03-01 20:02:35
*/
public interface UsersService extends IService<Users> {
    /**
     * 用户登录方法
     * @param username 用户名
     * @param password 密码
     * @return 是否登录成功
     */
    Integer login(String username, String password);
    String generateToken(Integer userId);
    boolean register(String username, String password) ;
}
