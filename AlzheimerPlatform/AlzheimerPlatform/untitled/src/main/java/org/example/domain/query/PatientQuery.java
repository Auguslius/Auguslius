package org.example.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户查询条件实体")
public class PatientQuery extends PageQuery{

    @Schema(description = "uuid")
    private String uuid;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "身份证号码")
    private String idCard;

    @Schema(description = "联系电话")
    private String phone;

}
