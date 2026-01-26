package com.gym.mapper;

import com.gym.dto.response.CurrentUserResponse;
import com.gym.dto.response.UserResponse;
import com.gym.domain.public_.entity.User;

public class UserMapper {

    public static UserResponse userToUserResponse(User user){
        UserResponse res = new UserResponse();
        res.setAvatar(user.getAvatar());
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        res.setEnabled(user.getEnabled());
        return res;
    }

    public static CurrentUserResponse userToCurrentUserResponse(User user){
        CurrentUserResponse res = new CurrentUserResponse();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        return res;
    }
}
