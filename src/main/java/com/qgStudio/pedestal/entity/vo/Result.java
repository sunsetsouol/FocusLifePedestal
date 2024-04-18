package com.qgStudio.pedestal.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "统一结果返回集")
public class Result<T> {
    @ApiModelProperty(value = "返回状态码", example = "200")
    private Integer code;

    @ApiModelProperty(value = "返回消息", example = "Success")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;


    public static <T> Result<T> success(){
        return new Result<T>(ResultStatusEnum.SUCCESS.code(), ResultStatusEnum.SUCCESS.message(),null);
    }

    public static <T> Result<T> success(ResultStatusEnum statusEnum){
        return new Result<T>(statusEnum.code(), statusEnum.message(), null);
    }

    public static <T> Result<T> success(ResultStatusEnum statusEnum, T data){
        return new Result<T>(statusEnum.code(), statusEnum.message(), data);
    }

    public static <T> Result<T> success(T data){
        return new Result<T>(ResultStatusEnum.SUCCESS.code(), ResultStatusEnum.SUCCESS.message(), data);
    }

    public static <T> Result<T> fail(ResultStatusEnum statusEnum){
        return new Result<T>(statusEnum.code(), statusEnum.message(), null);
    }

    public static <T> Result<T> fail(ResultStatusEnum statusEnum, String message){
        return new Result<T>(statusEnum.code(), message,null);
    }

    public static <T> Result<T> fail(Integer code, String message){
        return new Result<T>(code, message, null);
    }
}