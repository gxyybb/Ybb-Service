package org.example.ybb.api.controller;

import org.example.ybb.api.LoginRequest;
import org.example.ybb.api.LoginResponse;
import org.example.ybb.api.RegisterRequest;
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
    private UsersService userService;

    @PostMapping("/login")
    public ResultVO<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        boolean isSuccess = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (isSuccess) {
            String token = userService.generateToken(loginRequest.getUsername());
            return ResultVO.success(new LoginResponse("Login successful", token));
        }
        return ResultVO.error("Invalid username or password");
    }
    @PostMapping("/register")
    public ResultVO<String> register(@RequestBody RegisterRequest registerRequest) {
        boolean isRegistered = userService.register(registerRequest.getUsername(), registerRequest.getPassword());
        if (isRegistered) {
            return ResultVO.success("Registration successful");
        }
        return ResultVO.error( "Username already exists");
    }
    // 通过用户ID查询用户信息
//    @GetMapping("/user/{id}")
//    public ApiResponse<UserInfoResponse> getUserInfo(@PathVariable Long id) {
//        User user = userService.getUserById(id);
//        if (user != null) {
//            UserInfoResponse response = new UserInfoResponse(user.getId(),user.getUsername(), user.getEmail(), user.getCreatedAt());
//            return ResultVO.success(response);
//        }
//        return ResultVO.error(404, "User not found");
//    }
}
