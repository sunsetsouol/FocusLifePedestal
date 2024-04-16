package com.qgStudio.pedestal.entity.vo;

import com.qgStudio.pedestal.entity.po.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户信息")
public class UserVo {
    /**
     * 昵称
     */
    @ApiModelProperty(name = "昵称")
    private String name;

    /**
     * 头像
     */
    @ApiModelProperty(name = "头像")
    private String headImage;

    /**
     * 邮箱
     */
    @ApiModelProperty(name = "邮箱")
    private String email;

    /**
     * uid
     */
    private String uid;

    public UserVo(User user) {
        this.name = user.getName();
        this.headImage = user.getHeadImage();
        this.email = user.getEmail();
        this.uid = user.getUid();
    }
}
