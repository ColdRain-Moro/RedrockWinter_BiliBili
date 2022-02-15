package kim.bifrost.rain.bilibili.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView
import kotlin.properties.Delegates

/**
 * kim.bifrost.rain.bilibili.widget.MyNestedScrollView
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/15 17:56
 **/
class MyNestedScrollView : NestedScrollView {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var mParentScrollHeight by Delegates.notNull<Int>()
    var mScrollY by Delegates.notNull<Int>()

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        if (mScrollY > mParentScrollHeight) {
            consumed[0] = dx
            consumed[1] = dy
            scrollBy(0, dy)
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        mScrollY = t
    }

}