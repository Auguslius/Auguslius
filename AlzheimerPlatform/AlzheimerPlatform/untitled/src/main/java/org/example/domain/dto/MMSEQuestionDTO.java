package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "MMSE问题创建请求")
public class MMSEQuestionDTO {

    @Schema(description = "问题ID", example = "1")
    private Integer id;

    @NotBlank
    @Size(max = 20)
    @Schema(description = "所属大项（定向力/记忆力等）", requiredMode = Schema.RequiredMode.REQUIRED, example = "定向力")
    private String section;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "题目内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "现在是哪一年？")
    private String questionText;

    @NotNull
    @Schema(description = "题目类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String questionType;

    @NotNull
    @Min(0)
    @Schema(description = "本题最高得分", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer maxScore;

    @NotNull
    @Min(1)
    @Schema(description = "题目显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer position;

    @NotBlank(message = "验证方法不能为空")
    @Size(max = 20)
    @Schema(description = "验证方法", requiredMode = Schema.RequiredMode.REQUIRED, example = "选项验证")
    private String validationMethod;

    @Schema(description = "URL种类",example = "image,video...")
    private String urlCategory;

    @Schema(description = "url或比对字段")
    private String urlIndex;

    @Schema(description = "期望的答案", example = "是")
    private String expectedAnswer;

}