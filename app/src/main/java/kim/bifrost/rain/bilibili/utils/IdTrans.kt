package kim.bifrost.rain.bilibili.utils

import kotlin.math.pow

/**
 * kim.bifrost.rain.bilibili.utils.IdTrans
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/6 21:04
 **/
private val table = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF".toCharArray()
private var tr: MutableMap<Char, Int>? = null
private val s = intArrayOf(11, 10, 3, 8, 4, 6)
private const val xor = 177451812L
private const val add = 8728348608L

/**
 *
 * @return 错误时返回 0
 */
fun String.toAid(): Long {
    if (tr == null) {
        tr = HashMap()
        for (i in 0..57) {
            (tr as HashMap<Char, Int>)[table[i]] = i
        }
    }
    var re: Long = 0
    try {
        for (i in 0..5) {
            re += (tr!![this[s[i]]]!! * 58.0.pow(i.toDouble())).toLong()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return 0
    }
    return re - add xor xor
}

fun Long.toBvid(): String {
    var av = this
    av = (av xor xor) + add
    val re = "BV1  4 1 7  ".toCharArray()
    for (i in 0..5) {
        re[s[i]] = table[(av / Math.pow(58.0, i.toDouble()).toInt() % 58).toInt()]
    }
    return String(re)
}