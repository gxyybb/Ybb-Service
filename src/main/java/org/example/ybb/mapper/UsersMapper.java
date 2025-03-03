package org.example.ybb.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.ybb.domain.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 14847
* @description 针对表【users】的数据库操作Mapper
* @createDate 2025-03-01 20:02:35
* @Entity generator.domain.Users
*/
public interface UsersMapper extends BaseMapper<Users> {
    @Select("SELECT * FROM user WHERE username = #{username}")
    Users findByUsername(String username);

    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    int insert(Users user);
    // 根据 bookShareId 查询关联的 User 列表（已经在 BookShareMapper 中实现）
    @Select("SELECT u.* FROM user u " +
            "JOIN user_book_share ubs ON u.id = ubs.user_id " +
            "WHERE ubs.book_share_id = #{bookShareId}")
    List<Users> selectUsersByBookShareId(@Param("bookShareId") Long bookShareId);
}





