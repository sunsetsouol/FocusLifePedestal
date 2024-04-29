package com.qgStudio.pedestal.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgStudio.pedestal.constant.Constants;
import com.qgStudio.pedestal.constant.RedisConstants;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.DealFriendApplyDTO;
import com.qgStudio.pedestal.entity.dto.IntegerDTO;
import com.qgStudio.pedestal.entity.dto.LoginUserDTO;
import com.qgStudio.pedestal.entity.node.UserNode;
import com.qgStudio.pedestal.entity.po.Friendship;
import com.qgStudio.pedestal.entity.po.User;
import com.qgStudio.pedestal.entity.po.WaterIntake;
import com.qgStudio.pedestal.entity.vo.*;
import com.qgStudio.pedestal.entity.vo.friend.FriendApplyVO;
import com.qgStudio.pedestal.mapper.FriendshipMapper;
import com.qgStudio.pedestal.mapper.UserMapper;
import com.qgStudio.pedestal.mapper.WaterIntakeMapper;
import com.qgStudio.pedestal.repository.node.UserNodeRepository;
import com.qgStudio.pedestal.repository.relation.FriendRelationRepository;
import com.qgStudio.pedestal.service.IUserService;
import com.qgStudio.pedestal.utils.JwtUtils;
import com.qgStudio.pedestal.utils.RedisCache;
import com.qgStudio.pedestal.utils.UUIDUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RedisCache redisCache;
    private final WaterIntakeMapper waterIntakeMapper;
    private final FriendshipMapper friendshipMapper;
    private final UserNodeRepository userNodeRepository;
    private final FriendRelationRepository friendRelationRepository;

    @Override
    public Result login(LoginUserDTO loginUserDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(), loginUserDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate == null) {
            return Result.fail(ResultStatusEnum.LOGIN_FAIL);
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getUser().getId());
        String token = JwtUtils.createJwt(claims);
        redisCache.setCacheObject(RedisConstants.USER_LOGIN + userDetails.getUser().getId(), JSON.toJSONString(userDetails),30, TimeUnit.DAYS);
        return Result.success(ResultStatusEnum.LOGIN_SUCCESS, token);
    }

    @Override
    public Result register(LoginUserDTO loginUserDTO) {
        User user = User.builder()
                .email(loginUserDTO.getEmail())
                .password(passwordEncoder.encode(loginUserDTO.getPassword()))
                .uid(UUIDUtils.getRandomNumStr(32))
                .build();
        try {
            userMapper.insert(user);
            UserNode userNode = new UserNode();
            userNode.setUserId(user.getId());
            userNode.setUid(user.getUid());
            userNodeRepository.save(userNode);
            return Result.success(ResultStatusEnum.REGISTER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(ResultStatusEnum.EMAIL_EXISTED);
        }
    }

    @Override
    public Result getCode(String email) {
        return null;
    }

    @Override
    public Result setIntake(IntegerDTO intake, Integer userId) {
        userMapper.setIntake(userId, intake.getNumber());
        WaterIntake waterIntake = new WaterIntake();
        waterIntake.setIntakeTarget(intake.getNumber());
        LambdaQueryWrapper<WaterIntake> waterIntakeLambdaQueryWrapper = new LambdaQueryWrapper<WaterIntake>()
                .eq(WaterIntake::getUserId, userId)
                .eq(WaterIntake::getIntakeDate, LocalDate.now());
        waterIntakeMapper.update(waterIntake,waterIntakeLambdaQueryWrapper);
        return Result.success();
    }

    @Override
    public Result setReminderInterval(IntegerDTO reminderInterval) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        userMapper.setReminderInterval(userId, reminderInterval.getNumber());
        return Result.success();
    }

    @Override
    public Result<WaterReminderInfo> getWaterReminderInfo(Integer userId) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId)
                .select(User::getDefaultReminderInterval, User::getDefaultWaterIntake));
        return Result.success(ResultStatusEnum.SUCCESS,new WaterReminderInfo(user.getDefaultWaterIntake(), user.getDefaultReminderInterval()));
    }

    @Override
    public Result<Integer> getHistoryFocusTime(Integer id) {
        return Result.success(ResultStatusEnum.SUCCESS,userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, id).select(User::getTotalCompletionTime)).getTotalCompletionTime());
    }

    @Override
    public Result<Integer> getHistoryWaterIntake(Integer id) {
        return Result.success(ResultStatusEnum.SUCCESS,userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, id).select(User::getTotalWaterIntake)).getTotalWaterIntake());
    }

    @Override
    public Result<List<UserVo>> getByUId(String uid) {
        return Result.success(ResultStatusEnum.SUCCESS, userMapper.selectList(new LambdaQueryWrapper<User>().eq(User::getUid,uid)).stream().map(UserVo::new).collect(Collectors.toList()));
    }

    @Override
    public Result addFriend(Integer userId, String uid) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUid, uid));

        if (!Objects.isNull(friendRelationRepository.findByUserIds(userId, user.getId()))) {
            return Result.fail(ResultStatusEnum.ALREADY_FRIEND);
        }
        if (userId.equals(user.getId())){
            return Result.fail(ResultStatusEnum.ADD_SELF);
        }
        Friendship friendship = new Friendship();
        friendship.setFromId(userId);
        friendship.setToId(user.getId());
        friendship.setCreateTime(LocalDateTime.now());
        friendship.setExpirationTime(LocalDateTime.now().plusDays(Constants.VALIDITY_TIME));
        try {
            friendshipMapper.insert(friendship);
        } catch (DuplicateKeyException e) {
            friendship = friendshipMapper.selectOne(new LambdaQueryWrapper<Friendship>().eq(Friendship::getFromId, userId).eq(Friendship::getToId, user.getId()));
            friendship.setCreateTime(LocalDateTime.now());
            friendship.setExpirationTime(LocalDateTime.now().plusDays(Constants.VALIDITY_TIME));
            friendshipMapper.updateById(friendship);
        }
        return Result.success(ResultStatusEnum.SUCCESS);
    }

    @Override
    public List<FriendApplyVO> getFriendApply(Integer userId) {
        List<FriendApplyVO> friendApplyVOS = friendshipMapper.selectApply(userId);
        friendApplyVOS.forEach(FriendApplyVO::setStatus);
        return friendApplyVOS;
    }

    @Override
    public Result dealFriendApply(Integer userId, DealFriendApplyDTO dealFriendApplyDTO) {

        Friendship friendship = friendshipMapper.selectById(dealFriendApplyDTO.getFriendId());
        if (Objects.isNull(friendship)) {
            return Result.fail(ResultStatusEnum.FRIEND_APPLY_NOT_EXIST);
        }
        if(!userId.equals(friendship.getToId())){
            return Result.fail(ResultStatusEnum.NOT_AUTHORIZATION);
        }
        if(!friendship.getStatus().equals(Friendship.statusType.APPLICATION.getStatus())){
            return Result.fail(ResultStatusEnum.ALREADY_DEAL);
        }
        if(friendship.getExpirationTime().isBefore(LocalDateTime.now())){
            return Result.fail(ResultStatusEnum.OVERDUE);
        }
        friendship.setStatus(DealFriendApplyDTO.OperatorNumber.parse(dealFriendApplyDTO.getOperatorNumber()).getStatus());
        friendshipMapper.updateById(friendship);
        if (DealFriendApplyDTO.OperatorNumber.parse(dealFriendApplyDTO.getOperatorNumber()).equals(Friendship.statusType.AGREE)) {
            friendRelationRepository.addFriendship(userId, friendship.getFromId());
        }
        return Result.success(ResultStatusEnum.SUCCESS);
    }

    @Override
    public Result<List<UserVo>> searchMyFriends(Integer userId) {
        List<UserNode> userNodes = userNodeRepository.selectById(userId);
        List<Integer> ids = userNodes.stream().map(UserNode::getUserId).collect(Collectors.toList());
        return Result.success(ResultStatusEnum.SUCCESS, userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getId, ids).select(User::getId, User::getName, User::getHeadImage, User::getUid, User::getEmail)).stream().map(UserVo::new).collect(Collectors.toList()));
    }

    @Override
    public Result<UserVo> getByEmail(String email) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        return Result.success(ResultStatusEnum.SUCCESS, new UserVo(user));
    }
}
