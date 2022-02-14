package kim.bifrost.rain.bilibili.utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.style.DynamicDrawableSpan
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.reflect.TypeToken
import kim.bifrost.rain.bilibili.App
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URL
import java.security.MessageDigest
import java.util.zip.Inflater
import kotlin.random.Random

/**
 * kim.bifrost.coldrain.wanandroid.utils.Utils
 * WanAndroid
 *
 * @author 寒雨
 * @since 2021/12/21 0:14
 **/
@RequiresApi(Build.VERSION_CODES.O)
fun randomColor() = Color.valueOf(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())

fun View.getMarginLayoutParams() = if (layoutParams is ViewGroup.MarginLayoutParams) layoutParams as ViewGroup.MarginLayoutParams else ViewGroup.MarginLayoutParams(
    layoutParams).apply { layoutParams = this }


fun RecyclerView.scrollToTop() {
    if ((layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > 20) {
        scrollToPosition(0)
    } else {
        smoothScrollToPosition(0)
    }
}

fun String.htmlDecode(): Spanned = Html.fromHtml(this)

fun Int.toNumberFormattedString(): String {
    if (this >= 10000) {
        return BigDecimal(this.toDouble() / 10000).setScale(1, RoundingMode.HALF_DOWN).toString() + "万"
    }
    return toString()
}

/**
 * 通过md5校验计算sign
 *
 * @param args url参数
 * @param salt appkey对应盐值
 */
fun sign(vararg args: Pair<String, String>, salt: String): String {
    val iter = args.iterator()
    val sb = StringBuilder()
    while (iter.hasNext()) {
        iter.next().also {
            sb.append("${it.first}=${it.second}")
            if (iter.hasNext()) {
                sb.append("&")
            }
        }
    }
    return md5(sb.toString() + salt)
}

fun md5(content: String): String {
    val hash = MessageDigest.getInstance("MD5").digest(content.toByteArray())
    val hex = StringBuilder(hash.size * 2)
    for (b in hash) {
        var str = Integer.toHexString(b.toInt())
        if (b < 0x10) {
            str = "0$str"
        }
        hex.append(str.substring(str.length -2))
    }
    return hex.toString()
}

fun dynamicDrawableSpan(init: () -> Drawable) = object : DynamicDrawableSpan() {
    override fun getDrawable(): Drawable = init()
}

/**
 * 通过json的序列化与反序列化进行两个结构相似的不同类型的转换
 *
 * @param T
 * @return
 */
inline fun <reified T> Any.castToType(): T = App.gson.fromJson(App.gson.toJson(this), object : TypeToken<T>(){}.type)

//解压deflate数据的函数
private fun _decompress(data: ByteArray): ByteArray {
    var output: ByteArray
    val decompresser = Inflater(true)
    decompresser.reset()
    decompresser.setInput(data)
    val o = ByteArrayOutputStream(data.size)
    try {
        val buf = ByteArray(1024)
        while (!decompresser.finished()) {
            val i = decompresser.inflate(buf)
            o.write(buf, 0, i)
        }
        output = o.toByteArray()
    } catch (e: Exception) {
        output = data
        e.printStackTrace()
    } finally {
        try {
            o.close();
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    decompresser.end()
    return output
}

fun ByteArray.decompress() = _decompress(this)
