package com.shoppingmall.demo.utils;

import cn.hutool.core.util.RandomUtil;

/**
 * @author：Hikko
 * @date: 2023/5/25
 * @time: 22:02
 */
public class UUIDUtils {

    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }

    /**
     * @param suffix 生成的文件名的后缀 mp4 jpg png等
     * @return 完整文件名
     */
    public static String generateFilename(String suffix) {
        return System.currentTimeMillis() + "-" + RandomUtil.randomString("1234567890qwertyuiopasdfghjklzxcvbnm", 6) + "." + suffix;
    }

}


