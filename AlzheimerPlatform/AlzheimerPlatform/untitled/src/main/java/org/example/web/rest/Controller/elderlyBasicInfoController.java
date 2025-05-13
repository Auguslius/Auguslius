package org.example.web.rest.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.common.result.Result;
import org.example.utils.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/elderlyBasicInfo")
public class elderlyBasicInfoController {

    @GetMapping("/list")
    public Result<String> list(@RequestHeader(name="Authorization") String token, HttpServletResponse response){
        //校验token
        try {
            Map<String,Object> claims = JwtUtil.parseToken(token);
            return Result.success("老人数据基础分布");
        } catch (Exception e) {

            response.setStatus(401);
            return Result.fail("token校验失败");
        }
    }
}
