package cc.mrbird.febs.common.service.impl;

import cc.mrbird.febs.common.entity.RedisInfo;
import cc.mrbird.febs.common.exception.RedisConnectException;
import cc.mrbird.febs.common.function.JedisExecutor;
import cc.mrbird.febs.common.service.RedisService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * Redis 工具类，只封装了几个常用的 redis 命令，
 * 可根据实际需要按类似的方式扩展即可。
 *
 * @author MrBird
 */
@Slf4j
@Service("redisService")
public class RedisServiceImpl implements RedisService {
	
	private Long expireTime = 600 * 1000L; // 300s
	private ObjectMapper mapper;

    @Autowired
    JedisPool jedisPool;

    /**
     * 处理 jedis请求
     *
     * @param j 处理逻辑，通过 lambda行为参数化
     * @return 处理结果
     */
    private <T> T excuteByJedis(JedisExecutor<Jedis, T> j) throws RedisConnectException {
        try (Jedis jedis = jedisPool.getResource()) {
            return j.excute(jedis);
        } catch (Exception e) {
            throw new RedisConnectException(e.getMessage());
        }
    }

    @Override
    public List<RedisInfo> getRedisInfo() throws RedisConnectException {
        String info = this.excuteByJedis(
                j -> {
                    Client client = j.getClient();
                    client.info();
                    return client.getBulkReply();
                }
        );
        List<RedisInfo> infoList = new ArrayList<>();
        String[] strs = Objects.requireNonNull(info).split("\n");
        RedisInfo redisInfo;
        if (strs.length > 0) {
            for (String str1 : strs) {
                redisInfo = new RedisInfo();
                String[] str = str1.split(":");
                if (str.length > 1) {
                    String key = str[0];
                    String value = str[1];
                    redisInfo.setKey(key);
                    redisInfo.setValue(value);
                    infoList.add(redisInfo);
                }
            }
        }
        return infoList;
    }

    @Override
    public Map<String, Object> getKeysSize() throws RedisConnectException {
        Long dbSize = this.excuteByJedis(
                j -> {
                    Client client = j.getClient();
                    client.dbSize();
                    return client.getIntegerReply();
                }
        );
        Map<String, Object> map = new HashMap<>();
        map.put("create_time", System.currentTimeMillis());
        map.put("dbSize", dbSize);
        return map;
    }

    @Override
    public Map<String, Object> getMemoryInfo() throws RedisConnectException {
        String info = this.excuteByJedis(
                j -> {
                    Client client = j.getClient();
                    client.info();
                    return client.getBulkReply();
                }
        );
        String[] strs = Objects.requireNonNull(info).split("\n");
        Map<String, Object> map = null;
        for (String s : strs) {
            String[] detail = s.split(":");
            if ("used_memory".equals(detail[0])) {
                map = new HashMap<>();
                map.put("used_memory", detail[1].substring(0, detail[1].length() - 1));
                map.put("create_time", System.currentTimeMillis());
                break;
            }
        }
        return map;
    }

    @Override
    public Set<String> getKeys(String pattern) throws RedisConnectException {
        return this.excuteByJedis(j -> j.keys(pattern));
    }
    
    @Override
    public String get(String key) throws RedisConnectException {
        return this.excuteByJedis(j -> j.get(key));
    }
    
    @Override
    public Long getLong(String key) {
        try {
			String s = this.excuteByJedis(j -> j.get(key));
			if(s != null)
				return Long.valueOf(s);
		} catch (RedisConnectException e) {
			log.error("redis connect error");
		}
        return 0L;
    }

    @Override
    public String set(String key, String value) throws RedisConnectException {
        return this.excuteByJedis(j -> j.set(key, value));
    }

    @Override
    public String set(String key, String value, Long milliscends) throws RedisConnectException {
        String result = this.set(key, value);
        this.pexpire(key, milliscends);
        return result;
    }

    @Override
    public Object get(String key, Class c) {
    	try {
			String cacheStr = this.excuteByJedis(j -> j.get(key));
			return this.mapper.readValue(cacheStr, c);
		} catch (Exception e) {
			log.error("redis connect error");
		}
		return null;
    }

    @Override
    public String set(String key, Object obj) {
    	try {
			if (obj != null){
				String str =  mapper.writeValueAsString(obj);
				return this.excuteByJedis(j -> j.set(key, str));
			}
		} catch (Exception e) {
			log.error("redis connect error");
		}	
        return null;
    }

    @Override
    public String set(String key, Object obj, Long milliscends) {       
        try {
        	String result = this.set(key, obj);
			this.pexpire(key, milliscends);
			return result;
		} catch (RedisConnectException e) {
			log.error("redis connect error");
		}
        return null;
    }

    @Override
    public Long del(String... key) throws RedisConnectException {
        return this.excuteByJedis(j -> j.del(key));
    }

    @Override
    public Boolean exists(String key) throws RedisConnectException {
        return this.excuteByJedis(j -> j.exists(key));
    }

    @Override
    public Long pttl(String key) throws RedisConnectException {
        return this.excuteByJedis(j -> j.pttl(key));
    }

    @Override
    public Long pexpire(String key, Long milliseconds) throws RedisConnectException {
        return this.excuteByJedis(j -> j.pexpire(key, milliseconds));
    }

    @Override
    public Long zadd(String key, Double score, String member) throws RedisConnectException {
        return this.excuteByJedis(j -> j.zadd(key, score, member));
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) throws RedisConnectException {
        return this.excuteByJedis(j -> j.zrangeByScore(key, min, max));
    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) throws RedisConnectException {
        return this.excuteByJedis(j -> j.zremrangeByScore(key, start, end));
    }

    @Override
    public Long zrem(String key, String... members) throws RedisConnectException {
        return this.excuteByJedis(j -> j.zrem(key, members));
    }
    
    @Override
    public Long hset(String key, String field, String value) throws RedisConnectException {
        return this.excuteByJedis(j -> j.hset(key, field, value));
    }
    
    @Override
    public String hget(String key, String field) {
    	try {
    		return this.excuteByJedis(j -> j.hget(key, field));
    	} catch (RedisConnectException e) {
			log.error("redis connect error");
		}
		return null;
    }
    
    @Override
    public Map<String, String> hgetAll(String key) throws RedisConnectException {
        return this.excuteByJedis(j -> j.hgetAll(key));
    }
    
    @Override
    public boolean hexists(String key, String field) throws RedisConnectException {
        return this.excuteByJedis(j -> j.hexists(key, field));
    }
    
    @Override
    public Long hincrby(String key, String field, Long increment) {
    	try {
    		return this.excuteByJedis(j -> j.hincrBy(key, field, increment));
    	} catch (RedisConnectException e) {
			log.error("redis connect error");
		}
		return 0L;
    }

	@Override
	public Long incr(String key) {	
		try {
			return this.excuteByJedis(j -> j.incr(key));
		} catch (RedisConnectException e) {
			log.error("redis connect error");
		}
		return 0L;
	}

	@Override
	public Long incrBy(String key, long increment){
		try {
			return this.excuteByJedis(j -> j.incrBy(key, increment));
		} catch (RedisConnectException e) {
			log.error("redis connect error");
		}
		return 0L;
	}

	@Override
	public Set<String> hkeys(String key) {
		try {
			return this.excuteByJedis(j -> j.hkeys(key));
		} catch (RedisConnectException e) {
			log.error("redis connect error");
		}
		return null;
	}

	@Override
	public Long hdel(String key, String fields) {
		try {
			return this.excuteByJedis(j -> j.hdel(key, fields));
		} catch (RedisConnectException e) {
			log.error("redis connect error");
		}
		return null;
	}
    
}
