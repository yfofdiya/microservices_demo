package com.simform.user.service;

import com.simform.user.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers();

    User getUserByUserId(String userId);
}
