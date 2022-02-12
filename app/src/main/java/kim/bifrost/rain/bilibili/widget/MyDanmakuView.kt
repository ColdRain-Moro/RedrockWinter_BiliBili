package kim.bifrost.rain.bilibili.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.widget.parser.BiliDanmakuParser
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import master.flame.danmaku.ui.widget.DanmakuView
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.player.VideoView
import xyz.doikki.videoplayer.util.PlayerUtils
import java.io.InputStream


/**
 * kim.bifrost.rain.bilibili.widget.MyDanmakuView
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/11 1:50
 **/
class MyDanmakuView : DanmakuView, IControlComponent {

    private lateinit var parser: BaseDanmakuParser
    private val danmakuContext by lazy { DanmakuContext.create() }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3F)
            .setDuplicateMergingEnabled(false)
            .setScrollSpeedFactor(1.2f)
            .setScaleTextSize(1.2f)
            .setMaximumLines(null)
            .preventOverlapping(null)
            .setDanmakuMargin(40)
        setCallback(object : DrawHandler.Callback {
            override fun prepared() {
                start()
                if (!App.enableDanmaku) {
                    hide()
                }
            }

            override fun updateTimer(timer: DanmakuTimer?) {

            }

            override fun danmakuShown(danmaku: BaseDanmaku?) {

            }

            override fun drawingFinished() {

            }
        })
        enableDanmakuDrawingCache(true)
    }

    override fun attach(controlWrapper: ControlWrapper) {}

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation?) {}

    override fun onPlayStateChanged(playState: Int) {
        when (playState) {
            VideoView.STATE_IDLE -> release()
            VideoView.STATE_PREPARING -> {
                if (isPrepared) {
                    restart()
                }
                prepare(parser, danmakuContext)
            }
            VideoView.STATE_PLAYING -> if (isPrepared && isPaused) {
                resume()
            }
            VideoView.STATE_PAUSED -> if (isPrepared) {
                pause()
            }
            VideoView.STATE_PLAYBACK_COMPLETED -> {
                clear()
                clearDanmakusOnScreen()
            }
        }
    }

    override fun onPlayerStateChanged(playerState: Int) {}

    override fun setProgress(duration: Int, position: Int) {}

    override fun onLockStateChanged(isLocked: Boolean) {}

    /**
     * 发送文字弹幕
     *
     * @param text   弹幕文字
     * @param isSelf 是不是自己发的
     */
    fun addDanmaku(text: String, isSelf: Boolean) {
        danmakuContext.setCacheStuffer(SpannedCacheStuffer(), null)
        val danmaku: BaseDanmaku =
            danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
                ?: return
        danmaku.text = text
        danmaku.priority = 0 // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = false
        danmaku.time = currentTime + 1200
        danmaku.textSize = PlayerUtils.sp2px(context, 12f).toFloat()
        danmaku.textColor = Color.WHITE
        danmaku.textShadowColor = Color.GRAY
        danmaku.borderColor = if (isSelf) Color.GREEN else Color.TRANSPARENT
        addDanmaku(danmaku)
    }

    fun createParser(input: InputStream?): BaseDanmakuParser {
        if (input == null) {
            return object : BaseDanmakuParser() {
                override fun parse(): IDanmakus = Danmakus()
            }
        }
        val loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI)
        loader.load(input)
        val parser = BiliDanmakuParser()
        val dataSource = loader.dataSource
        parser.load(dataSource)
        return parser.also { this.parser = it }
    }

}