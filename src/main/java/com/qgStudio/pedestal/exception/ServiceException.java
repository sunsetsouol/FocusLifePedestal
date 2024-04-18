package com.qgStudio.pedestal.exception;

import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
public class ServiceException extends RuntimeException{

    private Integer code;

    private String message;
    public ServiceException() {
    }

    public ServiceException(ResultStatusEnum resultStatusEnum) {
        this.code = resultStatusEnum.code();
        this.message = resultStatusEnum.message();
    }


    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }



    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
