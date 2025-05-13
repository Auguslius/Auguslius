package org.example.web.rest.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.UserEnum;
import org.example.domain.entity.UserLogin;
import org.example.domain.dto.LogoutDTO;
import org.example.domain.dto.PageDTO;
import org.example.domain.dto.UserDTO;
import org.example.domain.entity.User;
import org.example.domain.query.UserQuery;
import org.example.domain.vo.UserVO;
import org.example.exception.BusinessException;
import org.example.utils.JwtUtil;
import org.example.utils.Md5Util;
import org.example.utils.ThreadLocalUtil;
import org.example.web.rest.mapper.UserLoginMapper;
import org.example.web.rest.mapper.UserMapper;
import org.example.web.rest.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;  // MyBatis-Plus 自动注入 Mapper
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserLoginMapper userLoginMapper;

    /*
    * 本人管理操作
    * */
    @Override
    public String login(String username, String password) {
        // 检查用户名是否存在
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.eq("username", username);
        User loginUser = userMapper.selectOne(wrapper);
        if (loginUser == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.USER_NOT_EXIST.getMessage());
        }
        // 检查用户状态是否为锁定或未激活
        if ("locked".equals(loginUser.getStatus())) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.USER_LOCKED.getMessage());
        }
        // 验证密码
        if (!Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.LOGIN_FAIL.getMessage());
        }
        //密码加密和缓存
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginUser.getId());
        claims.put("username", loginUser.getUsername());
        String token = JwtUtil.generateToken(claims);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(token, token, 1, TimeUnit.HOURS);

        loginUser.setStatus("active");
        loginUser.setUpdateTime(new Date());
        userMapper.updateById(loginUser);

        UserLogin userLogin = new UserLogin();
        userLogin.setUserId(loginUser.getNumber());
        userLogin.setLoginTime(new Date());
        userLogin.setStatus("success");
        userLogin.setUserName(loginUser.getUsername());
        QueryWrapper<UserLogin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUser.getId());
        if (userLoginMapper.selectOne(queryWrapper) != null){
            userLoginMapper.update(userLogin, queryWrapper);
        }else{
            userLoginMapper.insert(userLogin);
        }
        return token;
    }

    @Override
    @Transactional
    public String logout(LogoutDTO logoutDTO) {
        // 更新用户状态为未激活
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("number", logoutDTO.getNumber()); // 使用 number 字段查询
        User user = userMapper.selectOne(userQueryWrapper);
        if (user != null) {
            user.setStatus("inactive");
            user.setUpdateTime(new Date());
            userMapper.updateById(user);
            // 记录登出信息

            UserLogin userLogin = new UserLogin();
            userLogin.setLogoutTime(new Date());
            userLogin.setStatus("failure");
            UpdateWrapper<UserLogin> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", logoutDTO.getNumber()); // 根据 UserId 更新
            userLoginMapper.update(userLogin, updateWrapper);
        }
        return null;
    }

    @Override
    public Boolean register(String username, String password) {
        long count = this.count(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (count > 0) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.REGISTER_FAIL.getMessage());
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(Md5Util.getMD5String(password));
        user.setStatus("inactive");
        user.setNumber(generateUniqueNumber());
        int result = userMapper.insert(user);
        return result>0;
    }

    @Override
    public UserVO getUserInfoByToken(String token) {
        // 解析 Token
        Map<String, Object> map = JwtUtil.parseToken(token);
        Integer userId = (Integer) map.get("id");
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), CommonEnum.FAIL.getMessage());
        }
        // 转换为 VO
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public Boolean updateUser(UserDTO userDTO) {
        // 根据 number 字段查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNumber, userDTO.getNumber()); // 根据 number 查询
        User existingUser = Optional.ofNullable(userMapper.selectOne(queryWrapper))
                .orElseThrow(() -> new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.UPDATE_FAIL.getMessage()));

        // 校验用户名是否已存在（不允许同名）
        LambdaQueryWrapper<User> usernameQueryWrapper = new LambdaQueryWrapper<>();
        usernameQueryWrapper.eq(User::getUsername, userDTO.getUsername());
        User existingUsernameUser = userMapper.selectOne(usernameQueryWrapper);

        if (existingUsernameUser != null && !existingUsernameUser.getNumber().equals(userDTO.getNumber())) {
            // 如果用户名已存在且不是当前用户，返回失败
            throw new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.UPDATE_NAME_HAS_EXISTED.getMessage());
        }

        // 更新用户信息
        BeanUtils.copyProperties(userDTO, existingUser);
        existingUser.setIsAuthenticated(1);//设置为已认证
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("number", userDTO.getNumber()); // 根据 number 更新
        int result = userMapper.update(existingUser, updateWrapper);
        return result > 0;
    }
    /*
    * 用户管理操作
    * */
    @Override
    public Boolean saveUser(UserDTO userDTO) {
        // 将 UserDTO 转换为 User 实体
        User user = BeanUtil.copyProperties(userDTO, User.class);
        // 检查用户名是否已存在
        if (existsByUsername(user.getUsername())) {
            throw new BusinessException(UserEnum.UPDATE_NAME_HAS_EXISTED.getCode(), UserEnum.UPDATE_NAME_HAS_EXISTED.getMessage());
        }
        // 设置默认密码（MD5 加密）
        user.setPassword(Md5Util.getMD5String("123456")); // 默认密码为 "123456" 的 MD5 值
        // 设置用户状态为未激活
        user.setStatus("inactive");
        // 插入用户记录
        int result = userMapper.insert(user);
        if (result <= 0) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), UserEnum.ADD_FAIL.getMessage());
        }
        return true;
    }

    @Override
    public PageDTO<UserVO> queryUsersPage(UserQuery query) {
        String username = query.getUsername();
        String number = query.getNumber();
        String institution = query.getInstitution();
        String position = query.getPosition();
        //构建分页查询条件
        Page<User> page = query.toMpPageDefaultSortByUpdateTime();
        //PageSize和PageNo从query中获取

        Page<User> p = lambdaQuery()//加入了页数和总页数
                .like(username != null, User::getUsername, username)
                .like(number != null, User::getNumber, number)
                .like(institution != null, User::getInstitution, institution)
                .like(position != null, User::getPosition, position)
                .page(page);

        System.out.println("pages:"+p.getPages());
        System.out.println("total:"+p.getTotal());
        return PageDTO.of(p,user -> {
            // 1.拷贝基础属性
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            // 2.处理特殊逻辑
            vo.setUsername(vo.getUsername());
            return vo;
        });

    }

    /**
     * 获取对象中值为 null 的属性名
     */
    private String[] getNullPropertyNames(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        Set<String> nullPropertyNames = new HashSet<>();
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (src.getPropertyValue(pd.getName()) == null) {
                nullPropertyNames.add(pd.getName());
            }
        }
        return nullPropertyNames.toArray(new String[0]);
    }
    @Override
    public boolean existsByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        int count = (int) count(queryWrapper);
        return count > 0; // 如果查询到记录，则表示用户已存在
    }

    @Override
    public Boolean updateAvatar(String avatarUrl) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
            avatarUrl = "https://s2.loli.net/2023/07/04/yq5VYfYJJKX0lXL.png";
        }
        // 构建更新对象
        User user = new User();
        user.setUserPic(avatarUrl);
        // 更新条件
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id); // 依据用户ID更新
        // 执行更新操作
        int rows = userMapper.update(user, updateWrapper);
        return rows > 0;
    }

    private int generateUniqueNumber() {
        int min = 100000; // 设置最小值，避免过小
        int max = 999999; // 设置最大值，6位随机数

        int number;
        boolean exists;
        do {
            number = ThreadLocalRandom.current().nextInt(min, max);
            exists = this.count(new LambdaQueryWrapper<User>().eq(User::getNumber, number)) > 0;
        } while (exists); // 如果数据库中已存在，则重新生成

        return number;
    }

}




