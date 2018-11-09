/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.merchandise.service.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RedisClusterServiceContract {

  String get(String key);

  String set(String key, String value);

  String set(String key, String value, int second);

  String setObject(String key, Object value);

  String hget(String key, String field);

  String hset(String key, String field, String value);

  List<Object> hmget(String key, Collection<Object> fields);

  void hmset(String key, Map<String, String> map);

  String hsetObject(String key, String field, Object value);

  Boolean existKey(String key);

  Long incr(String key);

  Boolean expire(String key, int second);

  Long ttl(String key);

  Boolean del(String key);

  Long hdel(String key, String field);
}
