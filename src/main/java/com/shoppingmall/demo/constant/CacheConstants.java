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
     * 用户ID前缀
     */
    public static final String USER_ID_PREFIX = "user:key:";

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
     * 用户注册分配ID
     */
    public static final String USER_ID_KEY = "userId";

    /**
     * 权限ID前缀
     */
    public static final String PERMISSION_ID = "permissionId";

    /**
     * 品牌ID前缀
     */
    public static final String BAND_ID_PREFIX = "bandId";

    /**
     * 收货地址ID前缀
     */
    public static final String DELIVERY_ID_PREFIX = "deliveryId";

    /**
     * 分类ID前缀
     */
    public static String CATEGORY_ID_PREFIX = "categoryId";
}
