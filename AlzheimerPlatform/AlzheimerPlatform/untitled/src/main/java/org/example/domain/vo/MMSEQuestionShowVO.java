package org.example.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "MMSE题目展示视图")
public class MMSEQuestionShowVO {

    @Schema(description = "所属大项（定向力/记忆力等）", example = "定向力")
    private String section;

    @Schema(description = "题目内容", example = "现在是哪一年？")
    private String questionText;

    @Schema(description = "题目类型编码")
    private String questionType;

    @Schema(description = "本题最高得分", example = "1")
    private Integer maxScore;

    @Schema(description = "题目显示顺序", example = "1")
    private Integer position;

    @Schema(description = "验证方法", example = "选项验证")
    private String validationMethod;

    @Schema(description = "URL种类",example = "image,video...")
    private String urlCategory;

    @Schema(description = "题目图片URL", example = "https://www.baidu.com")
    private String urlIndex;

}
