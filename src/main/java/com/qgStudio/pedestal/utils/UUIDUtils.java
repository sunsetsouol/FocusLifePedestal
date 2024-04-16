package com.qgStudio.pedestal.utils;

import cn.hutool.core.lang.id.NanoId;
import cn.hutool.core.util.RandomUtil;
import com.qgStudio.pedestal.constant.Constants;

import java.util.Random;

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

    public static String getRandomStr(int size) {
        return NanoId.randomNanoId(new Random(), Constants.GEN_BASE_CHAR.toCharArray(), size);
    }

    public static String getRandomNumStr(int size) {
        return NanoId.randomNanoId(new Random(), Constants.GEN_BASE_NUM_CHAR.toCharArray(), size);
    }

    /**
     * @param suffix 生成的文件名的后缀 mp4 jpg png等
     * @return 完整文件名
     */
    public static String generateFilename(String suffix) {
        return System.currentTimeMillis() + "-" + RandomUtil.randomString("1234567890qwertyuiopasdfghjklzxcvbnm", 6) + "." + suffix;
    }

}


