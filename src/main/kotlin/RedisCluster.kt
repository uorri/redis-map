package org.example

import redis.clients.jedis.HostAndPort
import redis.clients.jedis.JedisCluster

class RedisCluster(host: String, port: Int) {
    private val hashKeyToRedisMap = mutableMapOf<String, RedisMap>()

    private val cluster: JedisCluster by lazy {
        JedisCluster(HostAndPort(host, port))
    }

    fun getRedisMap(hashKey: String) = hashKeyToRedisMap.getOrPut(hashKey) {
        RedisMap(hashKey)
    }

    inner class RedisMap(private val hashKey: String) : MutableMap<String, String> {
        override val entries: MutableSet<MutableMap.MutableEntry<String, String>>
            get() = cluster.hgetAll(hashKey).entries

        override val keys: MutableSet<String>
            get() = cluster.hkeys(hashKey)

        override val values: MutableCollection<String>
            get() = cluster.hvals(hashKey)

        override val size: Int
            get() {
                val len = cluster.hlen(hashKey)
                return if (len > Int.MAX_VALUE) Int.MAX_VALUE else len.toInt()
            }

        override fun put(key: String, value: String): String {
            cluster.hset(hashKey, key, value)
            return value
        }

        override fun remove(key: String): String? {
            val prev = get(key) ?: return null
            return if (cluster.hdel(hashKey, key) > 0) prev else null
        }

        override fun putAll(from: Map<out String, String>) {
            for ((key, value) in from) put(key, value)
        }

        override fun clear() { cluster.del(hashKey) }

        override fun isEmpty() = size == 0

        override fun get(key: String): String? = cluster.hget(hashKey, key)

        override fun containsValue(value: String) = value in cluster.hvals(hashKey)

        override fun containsKey(key: String) = cluster.hexists(hashKey, key)
    }
}



