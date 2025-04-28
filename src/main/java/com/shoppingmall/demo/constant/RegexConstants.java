package com.shoppingmall.demo.constant;

/**
 * @author redmi k50 ultra
 * * @date 2025/4/17
 */
public class RegexConstants {
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
     * 商品类型正则
     */
    public static final String GOOD_TYPE_REGEX = "^[01234]$";

    /**
     * 订单状态正则
     */
    public static final String ORDER_STATUS_REGEX = "^[01234]$";

    /**
     * 地名类型正则
     */
    public static final String ADDRESS_TYPE_REGEX = "^[012]$";

    /**
     * 逻辑删除字段正则
     */
    public static final String FORBIDDEN_REGEX = "^[01]$";

    /**
     * 商品状态正则
     */
    public static final String GOOD_STATUS_REGEX = "^[01]$";

    /**
     * 性别正则
     */
    public static final String GENDER_REGEX = "^[01]$";

    /**
     * 用户类型正则
     */
    public static final String USER_TYPE_REGEX = "^[0123]$";

    /**
     * 邮箱验证码正则
     */
    public static final String CODE_REGEX = "^\\d{6}||\\w{4}$";

    /**
     * 权限类型正则
     */
    public static final String PERMISSION_TYPE_REGEX = "^[01]$";

    /**
     * 邮箱验证码正则
     */
    public static final String EMAIL_VERIFY_CODE_REGEX = "^\\d{6}$";
}
