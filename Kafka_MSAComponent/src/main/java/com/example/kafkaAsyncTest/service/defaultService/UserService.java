package com.example.kafkaAsyncTest.service.defaultService;

import com.example.kafkaAsyncTest.entity.User;

public interface UserService {

    User getUser(Long userId);

    User getUserByUsername(String username);

    User createUser(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);


}