import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.example.RedisCluster

class RedisMapTest : StringSpec({
    val redisCluster = RedisCluster("localhost", 6379)
    lateinit var redisMap1: RedisCluster.RedisMap
    lateinit var redisMap2: RedisCluster.RedisMap

    beforeTest {
        redisMap1 = redisCluster.getRedisMap("testMap1")
        redisMap2 = redisCluster.getRedisMap("testMap2")
    }

    afterTest {
        redisCluster.getRedisMap("testMap1").clear()
        redisCluster.getRedisMap("testMap2").clear()
    }

    "Adding and retrieving data from RedisMap should work correctly" {
        redisMap1["key1"] = "value1"
        redisMap1["key2"] = "value2"

        redisMap1["key1"] shouldBe "value1"
        redisMap1["key2"] shouldBe "value2"
    }

    "Removing data from RedisMap should work correctly" {
        redisMap1["key1"] = "value1"
        redisMap1.remove("key1")

        redisMap1.containsKey("key1") shouldBe false
        redisMap1["key1"] shouldBe null
    }

    "Size of RedisMap should be correct" {
        redisMap1["key1"] = "value1"
        redisMap1["key2"] = "value2"

        redisMap1.size shouldBe 2
    }

    "Getting a non-existent key should return null" {
        redisMap1["key1"] shouldBe null
    }

    "Checking for key existence should work correctly" {
        redisMap1["key1"] = "value1"

        redisMap1.containsKey("key1") shouldBe true
        redisMap1.containsKey("key2") shouldBe false
    }

    "Adding and updating data should work correctly" {
        redisMap1["key1"] = "value1"
        redisMap1["key1"] shouldBe "value1"

        redisMap1["key1"] = "updatedValue1"
        redisMap1["key1"] shouldBe "updatedValue1"
    }

    "Clearing RedisMap should result in empty map" {
        redisMap1["key1"] = "value1"
        redisMap1.clear()

        redisMap1.isEmpty() shouldBe true
        redisMap1.size shouldBe 0
    }

    "RedisMap should be empty initially" {
        redisMap1.isEmpty() shouldBe true
        redisMap1.size shouldBe 0
    }

    "Getting keys, values, and entries should work correctly" {
        redisMap1["key1"] = "value1"
        redisMap1["key2"] = "value2"

        redisMap1.keys shouldBe setOf("key1", "key2")
        redisMap1.values shouldBe listOf("value1", "value2")
        redisMap1.entries.map { (k, v) -> "$k=$v" } shouldBe setOf("key1=value1", "key2=value2")
    }

    "Removing a non-existent key should return null" {
        redisMap1.remove("key1") shouldBe null
    }

    "Adding a large number of data should work correctly" {
        repeat(10000) {
            redisMap1["key$it"] = "value$it"
        }

        redisMap1.size shouldBe 10000
    }

    "Both RedisMaps should be independent" {
        redisMap1["key1"] = "value1"
        redisMap2["key2"] = "value2"

        redisMap1.containsKey("key2") shouldBe false
        redisMap2.containsKey("key1") shouldBe false
    }
})