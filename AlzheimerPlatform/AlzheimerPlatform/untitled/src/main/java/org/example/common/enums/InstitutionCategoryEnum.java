package org.example.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstitutionCategoryEnum {

    Delete_Institution_Success(2, "删除机构种类成功"),
    Delete_Institution_Fail(3, "删除机构种类失败"),
    Update_Institution_Success(4, "更新机构种类成功"),
    Update_Institution_Fail(5, "更新机构种类失败"),
    Add_Institution_Success(6, "添加机构种类成功"),
    Add_Institution_Fail(7, "添加机构种类失败"),
    Get_Institution_Success(8, "获取机构种类成功"),
    Get_Institution_Fail(9, "获取机构种类失败"),
    Category_Name_Exists(10, "机构种类名称已存在"),
    Category_Alias_Exists(11, "机构种类别名已存在"),
    Update_Institution_Not_Found(12, "更新机构种类失败，找不到该机构种类");

    private Integer code;
    private String message;

}
