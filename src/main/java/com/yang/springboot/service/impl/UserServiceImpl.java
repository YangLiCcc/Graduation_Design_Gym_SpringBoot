package com.yang.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.springboot.common.Constants;
import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Role;
import com.yang.springboot.entity.User;
import com.yang.springboot.mapper.UserDao;
import com.yang.springboot.param.LoginParam;
import com.yang.springboot.param.LoginUser;
import com.yang.springboot.param.RegisterParam;
import com.yang.springboot.param.UserInfoParam;
import com.yang.springboot.param.vo.UserInfoVo;
import com.yang.springboot.service.UserService;
import com.yang.springboot.utils.JwtUtils;
import com.yang.springboot.utils.RedisCache;
import com.yang.springboot.utils.UserUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    private final AuthenticationManager authenticationManager;

    private final RedisCache redisCache;

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager, RedisCache redisCache, UserDao userDao) {
        this.authenticationManager = authenticationManager;
        this.redisCache = redisCache;
        this.userDao = userDao;
    }

    @Override
    public Result getUserPage(String type, Integer currentPage, Integer pageSize, String username, String nickname, String phone) {
        // 根据currentPage和pageSize 设置分页查询范围
        Page<User> userPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 根据username nickname phone查询数据库
        // 判断参数是否为空 决定是否拼接条件
        // 使用like对查询条件进行模糊查询
        switch (type) {
            case "admin":
                userLambdaQueryWrapper.eq(User::getRoleId, 1);
                break;
            case "stuff":
                userLambdaQueryWrapper.eq(User::getRoleId, 2);
                break;
            case "coach":
                userLambdaQueryWrapper.eq(User::getRoleId, 3);
                break;
            case "member":
                userLambdaQueryWrapper.eq(User::getRoleId, 4);
                break;
            case "all":
            default:
                break;
        }
        if (StrUtil.isNotEmpty(username)) {
            userLambdaQueryWrapper.like(User::getUsername, username);
        }
        if (StrUtil.isNotEmpty(nickname)) {
            userLambdaQueryWrapper.like(User::getNickname, nickname);
        }
        if (StrUtil.isNotEmpty(phone)) {
            userLambdaQueryWrapper.like(User::getPhone, phone);
        }

        return new Result(Constants.CODE_200, "获取分页信息成功!", page(userPage, userLambdaQueryWrapper));
    }

    /**
     * 登录操作的实现类
     * 1. 首先验证 输入的验证码是否正确 如果不正确直接返回错误
     * 2. 调用自定义的登录接口 调用ProviderManager的authenticate方法进行认证 如果认证通过则进行生成jwt
     * 3. 将token存入Redis中进行缓存
     * @param loginParam 登录所需要的参数
     */
    @Override
    public Result login(LoginParam loginParam) {
        // 获取Cookie中携带的uuid 根据uuid获取Redis中的验证码
        String uuid = loginParam.getUuid();
        String codeInRedis = redisCache.getCacheObject(uuid).toString();
        // 校验token
        if (StrUtil.isEmpty(codeInRedis)) {
            return new Result(Constants.CODE_500, "验证码不存在!请重试!", null);
        } else if (!codeInRedis.equals(loginParam.getCaptchaCode())) {
            return new Result(Constants.CODE_500, "验证码错误!请重新输入!", null);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword());
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (LockedException e) {
            return new Result(Constants.CODE_500, "账号已停用!", null);
        } catch (InternalAuthenticationServiceException e) {
            return new Result(Constants.CODE_500, "登录失败!", null);
        }
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("用户名或密码错误!");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取用户id生成token
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtils.createJWT(userId);
        // 将数据存入Redis 根据ID查找和存储
        redisCache.setCacheObject("UserLogin: " + userId, loginUser, 1, TimeUnit.DAYS);
        // 以KEY-VALUE形式返回给前端
        Map<String, String> map = new HashMap<>();
        map.put("token", token);

        return new Result(Constants.CODE_200, "登录成功!", map);
    }

    /**
     * 注册操作
     * 1. 首先校验验证码 如果输入错误 直接返回错误
     * 2. 如果选择的职别是健身教练/工作人员/管理员 校验内部验证码是否正确 如果错误 直接返回错误
     * 3. 查询数据库中是否已存在username 如果有相同username 直接返回错误
     * @param registerParam 注册所需要的参数
     */
    @Override
    public Result register(RegisterParam registerParam) {
        // 获取Cookie中携带的uuid 根据uuid获取Redis中的验证码
        String uuid = registerParam.getUuid();
        String codeInRedis = redisCache.getCacheObject(uuid).toString();
        // 校验验证码
        if (StrUtil.isEmpty(codeInRedis)) {
            return new Result(Constants.CODE_500, "验证码不存在!请重试!", null);
        } else if (!codeInRedis.equals(registerParam.getCaptchaCode())) {
            return new Result(Constants.CODE_500, "验证码错误!请重新输入!", null);
        }
        // 如果注册的不是会员用户 则进行校验码判断
        switch (registerParam.getRoleId()) {
            case 1: // roleId为1 表示管理员
                if (Constants.VERIFY_CODE_REGISTER_ADMIN.equals(registerParam.getVerifyCode())) {
                    return new Result(Constants.CODE_401, "内部验证码输入错误!请重试!", null);
                }
                break;
            case 2: // roleId为2 表示工作人员
                if (Constants.VERIFY_CODE_REGISTER_STUFF.equals(registerParam.getVerifyCode())) {
                    return new Result(Constants.CODE_401, "内部验证码输入错误!请重试!", null);
                }
                break;
            case 3: // roleId为3 表示健身教练
                if (Constants.VERIFY_CODE_REGISTER_COACH.equals(registerParam.getVerifyCode())) {
                    return new Result(Constants.CODE_401, "内部验证码输入错误!请重试!", null);
                }
                break;
            default:
                break;
        }
        // 判断用户名是否重复
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, registerParam.getUsername());
        User one = getOne(lambdaQueryWrapper);
        if (!Objects.isNull(one)) {
            return new Result(Constants.CODE_500, "用户名已存在!请重试!", null);
        }
        User register = new User();
        // 将注册的参数赋值给User对象
        BeanUtil.copyProperties(registerParam, register);
        // 创建时间为当前时间
        register.setCreatedTime(LocalDateTime.now());
        // 头像为默认头像
        register.setAvatarUrl("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");

        if (save(register)) {
            return new Result(Constants.CODE_200, "注册成功!", null);
        }

        return new Result(Constants.CODE_500, "注册失败!请稍后重试!", null);
    }

    /**
     * 根据条件 获取用户ids
     * 1. 传入查询条件 根据条件查询所有相关用户
     * 2. 将查询到的用户的ids返回
     * @param lambdaQueryWrapper 查询条件
     * @return ids
     */
    @Override
    public List<Long> listUserIds(LambdaQueryWrapper<User> lambdaQueryWrapper) {
        List<User> userList = list(lambdaQueryWrapper);
        List<Long> userIds = new ArrayList<>();
        for (User user : userList) {
            userIds.add(user.getId());
        }

        return userIds;
    }

    /**
     * 登出操作
     * 1. 从SecurityContextHolder中获取用户数据
     * 2. 根据用户数据 删除Redis中的数据
     */
    @Override
    public Result logout() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        redisCache.deleteObject("UserLogin: " + userId);

        return new Result(Constants.CODE_200, "退出登录成功!", null);
    }

    /**
     * 获取用户信息
     * 1. 在校验token的过滤器中 已经将用户信息存入SecurityContextHolder中 所以这里可以直接获取存放的用户信息
     * 2. 除了基本信息 还需要通过role表去查询用户的权限信息
     */
    @Override
    public Result getUserInfo() {
        User user = UserUtils.getUserInfo();
        // 获取用户的权限信息 根据roleId查找role
        Role role = userDao.getRoleByUserId(user.getId());
        // 把需要的内容复制到UserInfoVo中 并传给前端
        UserInfoVo userInfoVo = copyToVo(user, role);

        return new Result(Constants.CODE_200, "获取用户信息成功!", userInfoVo);
    }

    @Override
    public Result updateUserInfoById(UserInfoParam userInfoParam, String token) {
        try {
            // 1. 从token中获取当前操作用户的userId
            Claims claims = JwtUtils.parseJWT(token);
            String userId = claims.getSubject();
            // 2. 从Redis中获取存放的User数据
            LoginUser loginUser = redisCache.getCacheObject("UserLogin: " + userId);
            User user = loginUser.getUser();
            UpdateUser(user, userInfoParam);
            // 3. 进行更新操作
            if (userDao.updateById(user) < 0) {
                return new Result(Constants.CODE_400, "更新用户信息失败!", null);
            }
            loginUser.setUser(user);
            // 4. 将更新后的数据写入Redis中
            redisCache.setCacheObject("UserLogin: " + userId, loginUser, 1, TimeUnit.DAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(Constants.CODE_400, "更新用户信息成功!", null);
    }

    @Override
    public Result insertUser(User user) {
        // 获取当前管理员用户信息
        User admin = UserUtils.getUserInfo();
        // 如果新添加的用户的用户名已存在 则返回
        if (!IsNotExistUsername(user.getUsername())) {
            return new Result(Constants.CODE_500, "用户名已存在!", null);
        }
        // 如果没有上传头像 则设置默认头像
        if (StrUtil.isEmpty(user.getAvatarUrl())) {
            user.setAvatarUrl("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        }
        user.setCreatedBy(admin.getId());
        user.setModifiedTime(LocalDateTime.now());
        user.setModifiedBy(admin.getId());
        // 加密密码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (userDao.insert(user) < 0) {
            return new Result(Constants.CODE_500, "系统错误!添加失败!", null);
        }

        return new Result(Constants.CODE_200, "添加用户成功!", null);
    }

    @Override
    public Result changeUserStatus(Long id, Boolean status) {
        // 获取当前管理员用户信息
        User admin = UserUtils.getUserInfo();
        // 根据id查询对应的用户
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId, id);
        User user = getOne(userLambdaQueryWrapper);
        user.setStatus(status);
        user.setModifiedBy(admin.getId());
        user.setModifiedTime(LocalDateTime.now());
        if (updateById(user)) {
            return new Result(Constants.CODE_200, "修改用户状态成功!", null);
        }

        return new Result(Constants.CODE_500, "修改用户状态失败!", null);
    }

    @Override
    public Result changeUserPassword(Long id, String password) {
        // 获取当前用户信息
        User userInfo = UserUtils.getUserInfo();
        // 判断当前用户是否为超级管理员或需要修改密码的用户
        if (userInfo.getRoleId() != 1 || !Objects.equals(userInfo.getId(), id)) {
            return new Result(Constants.CODE_401, "没有权限修改该用户密码!", null);
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId, id);
        User one = getOne(userLambdaQueryWrapper);
        one.setPassword(password);
        one.setModifiedBy(userInfo.getId());
        one.setModifiedTime(LocalDateTime.now());
        if (updateById(one)) {
            return new Result(Constants.CODE_200, "修改密码成功!", null);
        }

        return new Result(Constants.CODE_500, "修改密码失败!", null);
    }

    @Override
    public Result deleteUserById(Long id) {
        if (removeById(id)) {
            return new Result(Constants.CODE_200, "删除用户成功!", null);
        }
        return new Result(Constants.CODE_500, "删除用户失败!", null);
    }

    @Override
    public Result deleteBatchByUserIds(List<Long> ids) {
        if (removeBatchByIds(ids)) {
            return new Result(Constants.CODE_200, "批量删除用户成功!", null);
        }
        return new Result(Constants.CODE_500, "批量删除用户失败!", null);
    }

    public UserInfoVo copyToVo(User user, Role role) {
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(user, userInfoVo);
        userInfoVo.setRole(role);
        return userInfoVo;
    }

    public void UpdateUser(User user, UserInfoParam userInfoParam) {
        BeanUtil.copyProperties(userInfoParam, user);
        user.setModifiedBy(user.getId());
        user.setModifiedTime(LocalDateTime.now());
    }

    public boolean IsNotExistUsername(String username) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        User one = getOne(userLambdaQueryWrapper);
        return Objects.isNull(one);
    }



}
