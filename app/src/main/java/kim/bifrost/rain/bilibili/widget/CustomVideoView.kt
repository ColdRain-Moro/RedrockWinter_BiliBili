package kim.bifrost.rain.bilibili.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

/**
 * kim.bifrost.rain.bilibili.widget.CustomVideoView
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/6 17:49
 **/
class CustomVideoView : VideoView {
    // 构造器
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultSize(0, widthMeasureSpec)
        val height = getDefaultSize(0, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }
}