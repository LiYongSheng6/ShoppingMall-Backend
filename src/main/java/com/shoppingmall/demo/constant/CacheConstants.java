package com.shoppingmall.demo.constant;

/**
 * @author：Lys
 * @date: 2023/05/25
 * @time: 21:46
 */
public class CacheConstants {
    /**
     * token用户ID
     */
    public static final String TOKEN_USER_ID_KEY = "token:user:id:";

    /**
     * token用户名
     */
    public static final String TOKEN_USERNAME_KEY = "token:username:";

    /**
     * 用户令牌token
     */
    public static  final String LOGIN_USER_TOKEN_KEY = "login:user:token:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_USER = "user:";

    /**
     * 用户注册分配ID
     */
    public static final String USER_ID_KEY = "userId";

    /**
     * 用户ID前缀
     */
    public static final String USER_ID_PREFIX = "user:key:";

    /**
     * 权限ID前缀
     */
    public static final String PERMISSION_ID = "permissionID";

    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEX = "\\w{2,}@\\w{2,20}(\\.\\w{2,20}){1,2}";

    /**
     * 学号正则
     */
    public static final String STUDENT_ID_REGEX = "3[12]2\\d00\\d{4}";

    /**
     * 密码正则
     */
    public static final String PASSWORD_REGEX = "^\\S{1,20}$";

    /**
     * 用户名正则
     */
    public static final String USERNAME_REGEX = ".{1,20}";

    /**
     * 手机号正则
     */
    public static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    /**
     * 逻辑删除字段正则
     */
    public static final String FORBIDDEN_REGEX = "^[01]$";

    /**
     * 性别正则
     */
    public static final String GENDER_REGEX = "^[01]$";

    /**
     * 用户类型正则
     */
    public static final String USER_TYPE_REGEX = "^[01]$";

    /**
     * 邮箱验证码正则
     */
    public static final String CODE_REGEX = "^\\w{6}$";

    /**
     * 账号注册-邮箱验证码key
     */
    public static  final String REGISTER_EMAIL_CODE_KEY = "email:code:register:";

    /**
     * 账号登录-邮箱验证码key
     */
    public static  final String LOGIN_EMAIL_CODE_KEY = "email:code:login:";

    /**
     * 密码重置-邮箱验证码key
     */
    public static final String RESET_EMAIL_CODE_KEY = "email:code:reset:";

    /**
     * 邮箱修改-邮箱验证码key
     */
    public static final String UPDATE_EMAIL_CODE_KEY = "email:code:update:";

    /**
     * 数字验证码生成源
     */
    public static final String NUMBER_VERIFY_CODE_SOURCE = "0123456789";

    /**
     * 邮箱验证码过期时间（秒）
     */
    public static final int EMAIL_CODE_EXPIRE_TIME = 120;

    /**
     * 邮箱验证码长度
     */
    public static final int EMAIL_CODE_LENGTH = 6;

    /**
     * 布尔值-是
     */
    public static final String TRUE = "1";
    /**
     * 布尔值-否
     */
    public static final String FALSE = "0";


}
