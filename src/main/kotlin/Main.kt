package org.example

import redis.clients.jedis.HostAndPort
import redis.clients.jedis.JedisCluster


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val cluster = RedisCluster("localhost", 6379)
    val redisMap = cluster.getRedisMap("testMap")

    redisMap["hello"] = "world"
    println(redisMap["hello"])
}