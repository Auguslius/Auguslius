package org.example.web.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;

import org.example.domain.dto.LogoutDTO;
import org.example.domain.dto.PageDTO;
import org.example.domain.dto.UserDTO;
import org.example.domain.entity.User;
import org.example.domain.query.UserQuery;
import org.example.domain.vo.UserVO;

public interface UserService extends IService<User> {

    String login(String username, String password);

    String logout(LogoutDTO logoutDTO);

    Boolean register(String username, String password);

    UserVO getUserInfoByToken(String token);

    Boolean updateUser(UserDTO userDTO);

    Boolean saveUser(UserDTO userDTO);

    //TODO 风格修改
    PageDTO<UserVO> queryUsersPage(UserQuery query);


    boolean existsByUsername(String username);


    Boolean updateAvatar(String avatarUrl);
}

