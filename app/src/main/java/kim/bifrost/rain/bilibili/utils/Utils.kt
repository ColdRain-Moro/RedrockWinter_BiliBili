package kim.bifrost.rain.bilibili.utils

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.math.BigDecimal
import java.math.RoundingMode
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