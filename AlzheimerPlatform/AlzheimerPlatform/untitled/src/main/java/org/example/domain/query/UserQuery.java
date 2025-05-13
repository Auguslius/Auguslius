package org.example.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户查询条件实体")
public class UserQuery extends PageQuery {

    @Schema(description = "用户名关键字")
    private String username;

    @Schema(description = "工号关键字")
    private String number;

    @Schema(description = "机构关键字")
    private String institution;

    @Schema(description = "职位关键字")
    private String position;

}
