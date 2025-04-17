package com.shoppingmall.demo.service.common;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author：Hikko
 * @date: 2024/03/26
 * @time: 22:14
 */
@Component
public class RedisCacheService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Boolean hasKey(final String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 设置redis键值对
     *
     * @param key   redis键
     * @param value redis值
     */
    @Async
    public void setCacheObject(final String key, final Object value) {
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    /**
     * 设置set集合的键值对
     *
     * @param key   redis键
     * @param value redis值
     */
    @Async
    public void setCacheObjectSet(final String key, final Object value) {
        stringRedisTemplate.opsForSet().add(key, JSON.toJSONString(value));
    }

    /**
     * 设置redis键值对及其过期时间
     *
     * @param key      redis键
     * @param value    redis值
     * @param time     过期时间
     * @param timeUnit 时间单位
     */
    @Async
    public void setCacheObject(final String key, final Object value, final Integer time, final TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value), time, timeUnit);
    }

    /**
     * redis中如果不存在该键值对则设置
     *
     * @param key   redis键
     * @param value redis值
     * @return true=设置成功；false=设置失败
     */
    public Boolean setCacheObjectIfAbsent(final String key, final Object value) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, JSON.toJSONString(value));
    }


    /**
     * redis中如果不存在该键值对则设置并设置过期时间
     *
     * @param key   redis键
     * @param value redis值
     * @return true=设置成功；false=设置失败
     */
    public Boolean setCacheObjectIfAbsent(final String key, final Object value, final Integer time, final TimeUnit timeUnit) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, JSON.toJSONString(value), time, timeUnit);
    }

    /**
     * redis中如果不存在该键值对则设置
     *
     * @param key  redis键
     * @param time 过期时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public Boolean expire(final String key, final long time, final TimeUnit unit) {
        return stringRedisTemplate.expire(key, time, unit);
    }

    /**
     * 获得缓存的对象
     *
     * @param key redis键
     * @return redis值
     */
    public <T> T getCacheObject(final String key, Class<T> type) {
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JSON.parseObject(json, type);
    }

    /**
     * 获取存储为String类型的缓存
     */
    public String getCaCheString(final String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key redis键
     */
    @Async
    public void deleteObject(final String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 缓存Set
     *
     * @param key   缓存键值
     * @param value 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> long setCacheSet(final String key, final Object value) {
        Long count = stringRedisTemplate.opsForSet().add(key, JSON.toJSONString(value));
        return count == null ? 0 : count;
    }


    /**
     * 判断key-set中是否存在value
     *
     * @param key   缓存键值
     * @param value 缓存的数据
     * @return true=存在；false=不存在
     */
    public Boolean containsCacheSet(final String key, final Object value) {
        return stringRedisTemplate.opsForSet().isMember(key, JSON.toJSONString(value));
    }

    /**
     * 统计Set中缓存元素的个数
     *
     * @param key
     * @return
     */
    public Long countCacheSet(final String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    /**
     * 获取key的过期时间
     *
     * @param key 缓存键值
     * @return key 过期时间
     */
    public Long getExpireTime(final String key) {
        return stringRedisTemplate.opsForValue().getOperations().getExpire(key);
    }


    /**
     * 自增
     *
     * @param key redis键
     */
    @Async
    public void increment(final String key) {
        stringRedisTemplate.opsForValue().increment(key);
    }

    /**
     * 自增
     *
     * @param key redis键
     */
    public Long incrementAndGet(final String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    /**
     * 哈希值自增
     *
     * @param key     redis键
     * @param hashKey 哈希键
     * @param step    步长
     */
    public void incrementHash(final String key, final String hashKey, final long step) {
        stringRedisTemplate.opsForHash().increment(key, hashKey, step);
    }


    /**
     * 增加给定值
     *
     * @param key  redis键
     * @param incr 增量
     */
    @Async
    public void increment(final String key, final long incr) {
        stringRedisTemplate.opsForValue().increment(key, incr);
    }


    /**
     * 自减
     *
     * @param key redis键
     * @return 自减后的值
     */
    public Long decrement(final String key) {
        return stringRedisTemplate.opsForValue().decrement(key);
    }

    /**
     * lua原子自减（先查后改原子操作）
     *
     * @param key redis键
     * @return 自减后的值
     */
    public long luaAtomicDecr(final String key) {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(LuaAtomicDecrScript(), Long.class);
        Number n = stringRedisTemplate.execute(redisScript, Collections.singletonList(key));
        if (n == null) {
            return -1;
        }
        return n.longValue();
    }


    /**
     * 向key队列左插数据value
     *
     * @param key   队列key
     * @param value 数据
     */
    @Async
    public void leftPushValue(final String key, final String value) {
        stringRedisTemplate.opsForList().leftPushIfPresent(key, value);
    }


    /**
     * 从key队列右移value
     *
     * @param key 队列key
     */
    @Async
    public void rightPopValue(final String key) {
        stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从Set的Key中移除值为values[]的元素
     *
     * @param key    队列key
     * @param values 值
     * @return 成功移除的数量
     */
    public Long removeSetValues(final String key, final Object... values) {
        return stringRedisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 从Set的Key中移除值为value元素
     *
     * @param key   队列key
     * @param value 值
     * @return 成功移除的数量
     */
    public Long removeSetValue(final String key, final Object value) {
        return stringRedisTemplate.opsForSet().remove(key, JSON.toJSONString(value));
    }

    /**
     * 获取Set的所有元素
     *
     * @param key redis 键
     * @return 游标对象
     */
    public Cursor getSetAll(final String key) {

        return stringRedisTemplate.opsForSet().scan(key, ScanOptions.NONE);
    }

    /**
     * 从key队列中移除count个值为value的元素
     *
     * @param key   队列key
     * @param value 值
     * @param count 数量
     * @return 成功移除的数量
     */
    public Long removeListValue(final String key, final String value, final long count) {
        return stringRedisTemplate.opsForList().remove(key, count, value);
    }


    /**
     * 从key哈希表中存hashKey-hashValue
     *
     * @param key       redis键
     * @param hashKey   哈希key
     * @param hashValue 哈希value
     * @return 是否设置成功
     */
    public Boolean putHashKeyIfAbsent(final String key, final String hashKey, final Object hashValue) {
        return stringRedisTemplate.opsForHash().putIfAbsent(key, hashKey, JSON.toJSONString(hashValue));
    }

    /**
     * 从key哈希表中存hashKey-hashValue
     *
     * @param key       redis键
     * @param hashKey   哈希key
     * @param hashValue 哈希value
     * @return 是否设置成功
     */
    public void putHashKey(final String key, final String hashKey, final Object hashValue) {
        stringRedisTemplate.opsForHash().put(key, hashKey, JSON.toJSONString(hashValue));
    }

    /**
     * 获取哈希表中元素
     *
     * @param key     redis键
     * @param hashKey 哈希key
     * @return 哈希值
     */
    public Object getHashValue(final String key, final String hashKey) {
        return stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取哈希表中所有键值对
     *
     * @param key redis键
     * @return 哈希键值对
     */
    public Map<Object, Object> getHashAll(final String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }


    /**
     * 移除hash表中元素
     *
     * @param key     redis键
     * @param hashKey 哈希key
     */
    @Async
    public void removeHashKey(final String key, final String hashKey) {
        stringRedisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * @param key     redis键
     * @param hashKey hash键
     * @return 是否存在该键值
     */
    public boolean containHashValue(final String key, final String hashKey) {
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 向有序集合ZSet中插入元素
     *
     * @param key     redis键
     * @param zSetKey zset值
     * @param score   分数
     * @return 是否插入成功
     */
    public Boolean addZSetIfAbsent(final String key, final Object zSetKey, final Long score) {
        return stringRedisTemplate.opsForZSet().addIfAbsent(key, JSON.toJSONString(zSetKey), score);
    }

    /**
     * 向有序集合ZSet中插入元素
     *
     * @param key     redis键
     * @param zSetKey zset值
     * @param score   分数
     * @return 是否插入成功
     */
    public Boolean addZSet(final String key, final Object zSetKey, final Long score) {
        return stringRedisTemplate.opsForZSet().add(key, JSON.toJSONString(zSetKey), score);
    }

    /**
     * 移除ZSet中元素
     *
     * @param key     redis键
     * @param zSetKey zset值
     */
    @Async
    public void removeZSetKey(final String key, final Object zSetKey) {
        Long remove = stringRedisTemplate.opsForZSet().remove(key, JSON.toJSONString(zSetKey));
    }

    /**
     * 弹出并获取ZSet中排名第一的元素
     *
     * @param key redis键
     * @return value
     */
    public <T> T removeMaxScoreZSetKey(final String key, Class<T> type) {
        ZSetOperations.TypedTuple<String> stringTypedTuple = stringRedisTemplate.opsForZSet().popMin(key);
        if (!ObjectUtils.isEmpty(stringTypedTuple)) {
            return JSON.parseObject(stringTypedTuple.getValue(), type);
        }
        return null;
    }

    /**
     * 获取ZSet中排名第一的元素
     *
     * @param key  redis键
     * @param type 类型
     * @return value
     */
    public <T> T getFirstZSetValue(final String key, Class<T> type) {
        Set<String> range = stringRedisTemplate.opsForZSet().range(key, 0, 0);
        if (!CollectionUtils.isEmpty(range)) {
            return JSON.parseObject(range.iterator().next(), type);
        }
        return null;
    }

    /**
     * ZSet中是否存在某个值
     *
     * @param key     redis键
     * @param zSetKey zset值
     * @return 是否存在
     */
    public Boolean zSetContains(final String key, final Object zSetKey) {
        return (Boolean) (stringRedisTemplate.opsForZSet().rank(key, JSON.toJSONString(zSetKey)) != null);

    }

    /**
     * 获取ZSet中所有值
     *
     * @param key redis键
     * @return ZSet中所有值
     */
    public List<String> getAllZSetValue(final String key) {
        Set<String> set = stringRedisTemplate.opsForZSet().range(key, 0, Integer.MAX_VALUE);
        if (ObjectUtils.isEmpty(set)) {
            return null;
        }
        return new ArrayList<>(set);
    }

    /**
     * 获取ZSet中所有值
     *
     * @param key redis键
     * @return ZSet中所有值
     */
    public Set<String> getAllZSetValueAsSet(final String key) {
        Set<String> set = stringRedisTemplate.opsForZSet().range(key, 0, Integer.MAX_VALUE);
        if (ObjectUtils.isEmpty(set)) {
            return null;
        }
        return set;
    }


    /**
     * 获取ZSet长度
     *
     * @param key redis键
     * @return 长度
     */
    public Long getZSetSize(final String key) {
        return stringRedisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取ZSet中某个值的排名
     *
     * @param key     redis键
     * @param zSetKey zset值
     * @return 排名
     */
    public Long getZSetRank(final String key, final Object zSetKey) {
        return stringRedisTemplate.opsForZSet().rank(key, JSON.toJSONString(zSetKey));
    }

    /**
     * 使Zet中某个key的分数自增
     *
     * @param key    redis键
     * @param setKey setKey
     * @param step   步长
     */
    public void incrementForZetScore(String key, String setKey, Long step) {
        stringRedisTemplate.opsForZSet().incrementScore(key, setKey, step);
    }

    public Set<ZSetOperations.TypedTuple<String>> getAllZset(String key) {
        return stringRedisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
    }


    /**
     * 获取hash表大小
     *
     * @param key redis键
     * @return hash表大小
     */
    public Long getHashSize(final String key) {
        return stringRedisTemplate.opsForHash().size(key);
    }


    /**
     * lua原子脚本
     */
    private String LuaAtomicDecrScript() {
        return "local n" +
                "\nn = redis.call('get',KEYS[1])" +
                "\nif n and tonumber(n) < 0 then" +
                "\nreturn n;" +
                "\nend" +
                "\nn = redis.call('decr',KEYS[1])" +
                "\nreturn n";
    }

    /**
     * 获取有序集合中指定范围的元素。
     *
     * @param key        Redis键。
     * @param startRange 开始索引。
     * @param endRange   结束索引。
     * @return 集合中的元素集。
     */
    public Set<String> getElementsInRange(String key, long startRange, long endRange) {
        ZSetOperations<String, String> zSetOps = stringRedisTemplate.opsForZSet();
        return zSetOps.reverseRange(key, startRange, endRange);
    }


}
