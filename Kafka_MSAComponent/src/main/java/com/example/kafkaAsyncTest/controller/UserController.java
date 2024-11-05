package com.example.kafkaAsyncTest.controller;

import com.example.kafkaAsyncTest.entity.User;
import com.example.kafkaAsyncTest.service.defaultService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    // 유저 정보 호출 (ID로 검색)
    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    // 유저 정보 호출 (이름으로 검색)
    @GetMapping("/username/{userName}")
    public User getUserByUsername(@PathVariable String userName) {
        return userService.getUserByUsername(userName);
    }

    // 유저 정보 생성
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // 유저 정보 업데이트
    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    // 유저 정보 삭제
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}