package com.xxxx.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author jinlin
 * @since 2022-01-09
 */
@Getter
@Setter
@TableName("t_appraise")
@ApiModel(value = "Appraise对象", description = "")
public class Appraise implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("员工id")
    private Integer eid;

    @ApiModelProperty("考评日期")
    private LocalDate appDate;

    @ApiModelProperty("考评结果")
    private String appResult;

    @ApiModelProperty("考评内容")
    private String appContent;

    @ApiModelProperty("备注")
    private String remark;


}
