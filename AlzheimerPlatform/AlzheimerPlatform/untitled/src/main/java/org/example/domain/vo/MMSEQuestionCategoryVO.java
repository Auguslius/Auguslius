package org.example.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "MMSE题目大项视图")
public class MMSEQuestionCategoryVO {

    @Schema(description = "所属大项（定向力/记忆力等）", example = "定向力")
    private String section;

    @Schema(description = "大项题目数量", example = "1")
    private Integer count;

}
