package com.company.web.springdemo.helpers;

import com.company.web.springdemo.models.RegisterDto;
import com.company.web.springdemo.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User fromDto(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        return user;
    }

}
