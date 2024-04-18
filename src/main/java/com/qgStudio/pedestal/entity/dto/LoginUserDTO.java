package com.qgStudio.pedestal.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("注册用户")
public class LoginUserDTO {
    @ApiModelProperty(name = "邮箱", required = true)
    @Email
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @ApiModelProperty(name = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
    @ApiModelProperty(name = "验证码", required = true)
//    @NotBlank(message = "验证码不能为空")
    private String code;
}
