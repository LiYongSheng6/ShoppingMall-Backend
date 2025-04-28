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
     * 商品销量排行榜
     */
    public static final String GOOD_SALES_NUM_DAY_KEY = "good:sales:num:day:goodId";
    public static final String GOOD_SALES_NUM_WEEK_KEY = "good:sales:num:week:goodId";
    public static final String GOOD_SALES_NUM_YEAR_KEY = "good:sales:num:year:goodId";
    public static final String GOOD_SALES_NUM_OVERALL_KEY = "good:sales:num:overall:goodId";


    /**
     * 账号注册-图片验证码key
     */
    public static final String REGISTER_IMAGE_CODE_KEY = "code:image:register:";

    /**
     * 账号注册-邮箱验证码key
     */
    public static final String REGISTER_EMAIL_CODE_KEY = "code:email:register:";

    /**
     * 账号登录-邮箱验证码key
     */
    public static final String LOGIN_EMAIL_CODE_KEY = "code:email:login:";

    /**
     * 密码重置-邮箱验证码key
     */
    public static final String RESET_EMAIL_CODE_KEY = "code:email:reset:";

    /**
     * 邮箱修改-邮箱验证码key
     */
    public static final String UPDATE_EMAIL_CODE_KEY = "code:email:update:";

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
     * 停用词文件路径
     */
    public static final String CLASSPATH_STOP_WORDS_TXT = "classpath:stopWords.txt";

    /**
     * 聊天历史记录ID前缀
     */
    public static final String CHAT_HISTORY_ID = "chat_history_id:";
    public static final String NO_READ_CHAT_HISTORY_TOTAL_NUM_KEY = "no_read_chat_history_total_num_id";
    public static final String NO_READ_CHAT_HISTORY_SEPARATE_NUM_KEY = "no_read_chat_history_separate_num_id";
    public static final String NO_READ_TOTAL_NUM_KEY = "no_read_total_num_id";
    public static final String CHAT_HISTORY_LOCK = "chat_history_lock:";

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
     * 标签ID前缀
     */
    public static final String TAG_ID_PREFIX = "tagId";

    /**
     * 收货地址ID前缀
     */
    public static final String DELIVERY_ID_PREFIX = "deliveryId";

    /**
     * 地址ID前缀
     */
    public static final String ADDRESS_ID_PREFIX = "addressId";

    /**
     * 商品ID前缀
     */
    public static final String GOOD_ID_PREFIX = "goodId";

    /**
     * 订单ID前缀
     */
    public static final String ORDER_ID_PREFIX = "orderId";

    /**
     * 角色ID前缀
     */
    public static final String ROLE_ID = "roleId";

    /**
     * 角色权限ID前缀
     */
    public static final String ROLE_PERMISSION_ID = "rolePermissionId";

    /**
     * 用户角色ID前缀
     */
    public static final String USER_ROLE_ID = "userRoleId";

    /**
     * 分类ID前缀
     */
    public static String CATEGORY_ID_PREFIX = "categoryId";

    /**
     * 商品库存更新锁
     */
    public static final String GOOD_STOCK_UPDATE_LOCK = "good_stock_update_lock:";

    /**
     * 订单生成锁
     */
    public static final String ORDER_SAVE_LOCK = "order_save_lock:";

}
