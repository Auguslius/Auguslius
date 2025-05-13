package org.example.common.enums;

import lombok.Getter;

@Getter
public enum InstitutionEnum {

    Delete_Institution_Success(2, "删除成功"),
    Delete_Institution_Fail(3, "删除失败"),
    Add_Institution_Success(4, "添加成功"),
    Add_Institution_Fail(5, "添加失败"),
    Update_Institution_Success(6, "更新成功"),
    Update_Institution_Fail(7, "更新失败"),
    Get_Institution_Success(8, "查询成功"),
    Get_Institution_Fail(9, "查询失败"),
    Get_Institution_Category_Success(10, "查询成功"),
    Add_Institution_Duplicate_Name(11, "机构名称重复"),
    Add_Category_Not_Exist(12, "机构种类不存在"),
    Add_Level_Not_Exist(13, "机构级别不存在"),
    Update_Institution_Not_Exist(14, "机构不存在"),
    Update_Institution_Duplicate_Name(15, "机构名称重复"),
    Update_Institution_Category_Not_Exist(16, "机构种类不存在"),
    Update_Institution_Level_Not_Exist(17, "机构级别不存在"),
    Add_Institution_Name_Exist(18,"机构名已存在"),
    Query_Institution_Success(19,"分页查询成功"),
    Query_Institution_Fail(20,"分页查询失败");


    private  Integer code;
    private String message;

    InstitutionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
