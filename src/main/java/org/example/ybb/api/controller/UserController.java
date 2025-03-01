package org.example.ybb.api.controller;

import org.example.ybb.common.vo.ResultVO;
import org.example.ybb.domain.Users;
import org.example.ybb.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UsersService usersService;

    @GetMapping
    public ResultVO<List<Users>> all() {

        return ResultVO.success(usersService.list()) ;
    }

    @PostMapping
    public ResultVO<Users> add(@RequestBody Users user) {
        boolean save = usersService.save(user);
        return save? ResultVO.success(): ResultVO.error();
    }
}
