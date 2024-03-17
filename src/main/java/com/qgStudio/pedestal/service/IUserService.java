package com.qgStudio.pedestal.service;

import com.qgStudio.pedestal.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qgStudio.pedestal.entity.vo.LoginUserVo;
import com.qgStudio.pedestal.entity.vo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
public interface IUserService extends IService<User> {

    Result login(LoginUserVo loginUserVo);

    Result register(LoginUserVo loginUserVo);

    Result getCode(String email);
}
