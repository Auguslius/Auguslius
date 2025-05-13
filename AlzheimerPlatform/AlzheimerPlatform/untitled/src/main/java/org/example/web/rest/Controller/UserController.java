package org.example.web.rest.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.UserEnum;
import org.example.common.result.Result;
import org.example.domain.dto.LogoutDTO;
import org.example.domain.dto.PageDTO;
import org.example.domain.dto.UserDTO;
import org.example.domain.group.QueryGroup;
import org.example.domain.group.UpdateGroup;
import org.example.domain.vo.UserVO;
import org.example.domain.query.UserQuery;
import org.example.web.rest.service.UserService;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/user")
@Validated
@Tag(name="用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /*
    * 个人用户操作
    *
    * */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Operation(summary = "用户登录")
    public Result<String> login(
            @Parameter(description = "用户名", required = true) @Pattern(regexp = "^\\S{5,16}$") String username,
            @Parameter(description = "密码", required = true) @Pattern(regexp = "^\\S{5,16}$") String password) {
        String result = userService.login(username, password);
        return Result.success(CommonEnum.SUCCESS.getCode(), UserEnum.LOGIN_SUCCESS.getMessage(), result);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @Operation(summary = "用户退出登录")
    public Result<String> logout(@RequestBody @Validated(QueryGroup.class) LogoutDTO logoutDTO) {
        String result = userService.logout(logoutDTO);
        return Result.success(CommonEnum.SUCCESS.getCode(), UserEnum.LOGOUT_SUCCESS.getMessage(), result);
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Operation(summary ="用户注册")
    public Result<Boolean> register(
            @Parameter(description = "用户名", required = true) @Pattern(regexp = "^\\S{5,16}$") String username,
            @Parameter(description = "密码", required = true) @Pattern(regexp = "^\\S{5,16}$") String password) {
        Boolean result = userService.register(username, password);
        return Result.success(CommonEnum.SUCCESS.getCode(), UserEnum.REGISTER_SUCCESS.getMessage(), result);
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    @Operation(summary = "获取当前用户信息")
    public Result<UserVO> userInfo(@RequestHeader(name = "Authorization") String token) {
        UserVO result = userService.getUserInfoByToken(token);
        return new Result<UserVO>(CommonEnum.SUCCESS.getCode(), CommonEnum.SUCCESS.getMessage(), result);
    }

    /*
    * 用户管理操作
    * */
    @RequestMapping(value = "/save", method = RequestMethod.PUT)
    @Operation(summary = "用户管理-新增用户接口")
    public Result<Boolean> saveUser(@RequestBody UserDTO userDTO){
        Boolean result = userService.saveUser(userDTO);
        return Result.success(CommonEnum.SUCCESS.getCode(),UserEnum.ADD_SUCCESS.getMessage(),result);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    @Operation(summary = "用户信息更新")
    public Result<Boolean> updateUser(@RequestBody @Validated(UpdateGroup.class) UserDTO userDTO) {
        Boolean result = userService.updateUser(userDTO);
        return Result.success(CommonEnum.SUCCESS.getCode(), UserEnum.UPDATE_SUCCESS.getMessage(), result);
    }

    //删除用户接口不需要操作所以直接在Controller判断
    @Operation(summary = "用户管理-删除用户接口")
    @DeleteMapping("{id}")
    public Result<Boolean> deleteUserById(@Parameter(description = "用户id") @PathVariable("id") Integer id){
            userService.removeById(id);
            return Result.success(CommonEnum.SUCCESS.getCode(),UserEnum.DELETE_SUCCESS.getMessage(),null);
    }


    
    @Operation(summary = "根据条件分页查询用户接口")
    @GetMapping("/page")
    public Result<PageDTO<UserVO>> queryUsersPage(UserQuery query) {
        PageDTO<UserVO> pageDTO = userService.queryUsersPage(query);
        return Result.success(pageDTO);  
    }

    @Operation(summary = "更新用户头像")
    @PatchMapping("updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        Boolean result = userService.updateAvatar(avatarUrl);
        return Result.success(CommonEnum.SUCCESS.getCode(), UserEnum.UPDATE_SUCCESS.getMessage(), result);
    }

    
    //    @Operation(summary = "根据id批量查询用户接口")
//    @GetMapping
//    public List<UserVO> queryUserByIds(@Parameter(description = "用户id集合") @RequestParam("ids") List<Long> ids){
//        List<User> users = userService.listByIds(ids);
//        return BeanUtil.copyToList(users, UserVO.class);
//    }
}
