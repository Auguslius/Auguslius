package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.util.Date;

@Data
@TableName("mmse_questions")
@Schema(description = "MMSE评估问题实体")
public class MMSEQuestion {
    @TableId(type = IdType.AUTO)
    @Schema(description = "问题ID", example = "1")
    private Integer id;

    @NotBlank(message = "所属大项不能为空")
    @Size(max = 20, message = "所属大项长度不能超过20")
    @Schema(description = "所属大项（定向力/记忆力等）", example = "定向力")
    private String section;

    @NotBlank(message = "题目文本不能为空")
    @Size(max = 100, message = "题目文本长度不能超过100")
    @Schema(description = "题目内容", example = "现在是哪一年？")
    private String questionText;

    @NotBlank(message = "题目类型不能为空")
    @Schema(description = "题目类型", example = "单选题")
    private String questionType;

    @NotNull(message = "最高得分不能为空")
    @Min(value = 0, message = "得分不能小于0")
    @Schema(description = "本题最高得分", example = "1")
    private Integer maxScore;

    @NotNull(message = "题目顺序不能为空")
    @Min(value = 1, message = "顺序号最小为1")
    @Schema(description = "题目显示顺序", example = "1")
    private Integer position;

    @NotBlank(message = "验证方法不能为空")
    @Size(max = 20, message = "验证方法长度不能超过20")
    @Schema(description = "验证方法（如：选择题验证、文字验证等）", example = "选择题验证")
    private String validationMethod;

    @Schema(description = "URL种类",example = "image,video...")
    private String urlCategory;

    @Schema(description = "url或比对字段")
    private String urlIndex;

    @Schema(description = "期望的答案", example = "是")
    private String expectedAnswer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;
}
