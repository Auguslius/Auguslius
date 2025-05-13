package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;


@Data
@Getter
@Schema(description = "层级信息")
@TableName("category_level")
public class CategoryLevel {

    @TableField("level")
    @Schema(description = "层级")
    private Integer level;

    @TableField("level_name")
    @Schema(description = "层级名称")
    private String levelName;

}
