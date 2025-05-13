package org.example.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "MMSE题目查询条件实体")
public class MMSEQuestionQuery extends PageQuery{

    @Schema(description = "所属大项（定向力/记忆力等）", example = "定向力")
    private String section;

    @Schema(description = "题目类型")
    private String questionType;


    private String sortField;

    private String sortOrder;
}
