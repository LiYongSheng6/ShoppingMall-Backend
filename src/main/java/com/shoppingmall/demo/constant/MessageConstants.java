package com.shoppingmall.demo.constant;

/**
 * @author redmi k50 ultra
 * * @date 2024/7/17
 */
public class MessageConstants {
    public static final String EMAIL_READY_EXIT="邮箱地址已被注册！";
    public static final String USER_PHONE_EXIST = "该用户手机已存在";
    public static final String STUDENT_ID_EXIST = "该用户学号已存在";

    public static final String TOKEN_NOT_FOUND = "登录信息丢失，请重新登录！";
    public static final String TOKEN_INVALIDATED = "登录信息已过期，请重新登录!";
    public static final String TOKEN_CHECK_EXCEPTION = "登录信息校验异常，请重新登录！";
    public static final String TOKEN_UPDATE_SUCCESS = "token更新成功";
    public static final String TOKEN_RESOLVE_ERROR = "token解析异常";
    public static final String URL_RESOLVE_ERROR = "URL解析异常";
    public static final String NO_LOGIN_ERROR = "尚未登录";
    public static final String VERIFY_CODE_ERROR = "验证码错误";
    public static final String VERIFY_CODE_INVALIDATED = "验证码已失效";

    public static final String NO_PERMISSION_ERROR = "权限不足";
    public static final String ACCESS_RESTRICTED_ERROR = "访问受限";

    public static final String FILE_SIZE_EXCEEDED = "文件大小超出限制！";
    public static final String PICTURE_FORMAT_ERROR = "图片格式错误";

    public static final String REGISTER_SUCCESS = "注册成功";
    public static final String REGISTER_ERROR = "注册失败";
    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String LOGOUT_SUCCESS = "退出登录成功";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String UPDATE_SUCCESS = "修改成功";
    public static final String UPDATE_ERROR = "修改失败";
    public static final String DELETE_SUCCESS="删除成功";
    public static final String DELETE_ERROR = "删除失败";
    public static final String CANCEL_SUCCESS = "取消成功";
    public static final String CANCEL_ERROR = "取消失败";
    public static final String OPERATION_SUCCESS = "操作成功";
    public static final String OPERATION_ERROR = "操作失败";
    public static final String COMMENT_SUCCESS = "评价成功";
    public static final String COMMENT_ERROR = "评价失败";
    public static final String SAVE_SUCCESS = "添加成功";
    public static final String SAVE_ERROR = "添加失败";
    public static final String EXPORT_SUCCESS = "导出Excel文件成功";
    public static final String EXPORT_ERROR = "导出Excel文件失败";
    public static final String EMAIL_SEND_SUCCESS = "邮件信息发送成功";
    public static final String EMAIL_SEND_ERROR = "邮件信息发送失败";
    public static final String EMAIL_CODE_TITLE = "邮箱验证码";
    public static final String EMAIL_CODE_SEND_SUCCESS = "邮箱验证码发送成功";
    public static final String EMAIL_CODE_SEND_ERROR = "邮箱验证码发送失败";
    public static final String EMAIL_CODE_STILL_VALID = "邮箱验证码还未失效，请稍后再试！";

    public static final String USER_FORBIDDEN_EXIST = "当前用户已被封禁！";
    public static final String USER_UNBLOCKING_EXIST = "当前用户已被解封！";
    public static final String USER_FORBIDDEN_ERROR = "该用户已被禁用，请联系管理员";


    public static final String STUDENT_ID_REGEX_MESSAGE = "学号格式为3x2x00xxxx";
    public static final String USERNAME_REGEX_MESSAGE = "用户名为1到20位非空字符";
    public static final String PASSWORD_REGEX_MESSAGE = "密码为1到20位非空字符";
    public static final String CODE_LENGTH_MESSAGE = "验证码长度为6位";
    public static final String PHONE_REGEX_MESSAGE = "手机号码格式错误";
    public static final String EMAIL_REGEX_MESSAGE = "请输入正确的邮箱地址";
    public static final String GENDER_REGEX_MESSAGE = "用户性别值不符合要求";
    public static final String USER_TYPE_REGEX_MESSAGE = "用户类型值不符合要求";
    public static final String GOOD_TYPE_REGEX_MESSAGE = "商品类型值不符合要求";
    public static final String GOOD_STATUS_REGEX_MESSAGE = "商品状态值不符合要求";
    public static final String ORDER_STATUS_REGEX_MESSAGE = "订单状态值不符合要求";
    public static final String FORBIDDEN_REGEX_MESSAGE = "封禁判定值不符合要求";
    public static final String ADDRESS_TYPE_REGEX_MESSAGE = "地址类型值不符合要求";
    public static final String TAG_TYPE_REGEX_MESSAGE = "标签类型值不符合要求";
    public static final String CATEGORY_TYPE_REGEX_MESSAGE = "分类类型值不符合要求";
    public static final String PERMISSION_TYPE_REGEX_MESSAGE = "权限类型值不符合要求";

    public static final String GOOD_RANK_TYPE_NULL_ERROR = "商品榜单类型不能为空!";
    public static final String NO_ENOUGH_GOOD_ERROR = "商品库存数量不足！";

    public static final String PARAM_MISSING = "缺少必要参数";
    public static final String INFO_ERROR = "报名信息缺失";
    public static final String NO_FOUND_USER_ERROR = "找不到相关用户";
    public static final String NO_FOUND_TIME_ERROR = "没有找到相关时间信息";
    public static final String NO_FOUND_PERMISSION_ERROR = "没有找到相关权限信息";
    public static final String NO_FOUND_BAND_ERROR = "没有找到相关品牌信息";
    public static final String NO_FOUND_TAG_ERROR = "没有找到相关标签信息";
    public static final String NO_FOUND_CATEGORY_ERROR = "没有找到相关分类信息";
    public static final String NO_FOUND_DELIVERY_ERROR = "没有找到相关收货地址信息";
    public static final String NO_FOUND_ADDRESS_ERROR = "没有找到相关地址信息";
    public static final String NO_FOUND_GOOD_ERROR = "没有找到相关商品信息";
    public static final String NO_FOUND_ORDER_ERROR = "没有找到相关订单信息";
    public static final String NO_FOUND_ROLE_ERROR = "没有找到相关角色信息";
    public static final String NO_FOUND_ADDRESS_NAME_ERROR = "没有要操作的相关地名信息";
    public static final String NO_FOUND_CATEGORY_NAME_ERROR = "没有要操作的相关分类信息";
    public static final String NO_FOUND_TAG_NAME_ERROR = "没有要操作的相关标签信息";
    public static final String NO_FOUND_AUTHENTICATION_ERROR = "没有找到相关认证信息";
    public static final String NO_FOUND_STUDENT_ID_NAME_MAP_ERROR = "没有要操作的相关学号和实名信息Map";
    public static final String AUTHENTICATION_MATCH_ERROR = "实名认证信息比对失败";
    public static final String AUTHENTICATION_APPLIED_EXIST = "实名认证信息已被使用";


    public static final String NUMBER_NEGATIVE_ERROR = "操作结果不能为负数";
    public static final String GET_LOCK_ERROR = "获取锁失败";

    public static final String PERMISSION_PROHIBITED_ERROR = "禁止操作不属于自己的事物";
    public static final String PURCHASE_OWN_ERROR = "禁止购买自己的商品";
    public static final String GOOD_NOT_REMOVE_ERROR = "商品未下架，无法操作";
    public static final String GOOD_REMOVE_ERROR = "商品已下架";

    public static final String PERMISSION_CODE_EXIST = "资源编码已存在";
    public static final String PERMISSION_PATH_EXIST = "资源路径已存在";
    public static final String ADDRESS_NAME_EXIST = "地名名称已存在";
    public static final String CATEGORY_NAME_EXIST = "分类名称已存在";
    public static final String TAG_NAME_EXIST = "标签名称已存在";
    public static final String ROLE_CODE_EXIST = "角色编码已存在";

    public static final String PERMISSION_USED_ERROR = "该权限资源已被分配使用，无法删除";
    public static final String ROLE_USED_ERROR = "该角色已被分配使用，无法删除";
}

