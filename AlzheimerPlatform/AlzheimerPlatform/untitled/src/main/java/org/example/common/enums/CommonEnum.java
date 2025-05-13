package org.example.common.enums;

import lombok.Getter;


@Getter
public enum CommonEnum{

       SUCCESS(0,"成功"),
       FAIL(1,"失败"),
       ERROR(2,"系统异常");

       private Integer code;

       private  String message;

       CommonEnum(Integer code,String message){
              this.code=code;
              this.message=message;
       }


}
