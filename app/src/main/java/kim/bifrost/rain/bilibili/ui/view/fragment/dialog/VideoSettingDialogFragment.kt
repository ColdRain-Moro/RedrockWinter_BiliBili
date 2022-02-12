package kim.bifrost.rain.bilibili.ui.view.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import kim.bifrost.rain.bilibili.App
import kim.bifrost.rain.bilibili.databinding.FragmentDialogVideoSettingBinding

/**
 * kim.bifrost.rain.bilibili.ui.view.fragment.dialog.VideoSettingDialogFragment
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/11 19:06
 **/
class VideoSettingDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogVideoSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDialogVideoSettingBinding
            .inflate(inflater, container, false)
            .also { binding = it }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            switchEnableDanmaku.apply {
                isChecked = App.enableDanmaku
                setOnCheckedChangeListener { _, b ->
                    App.enableDanmaku = b
                }
            }
            spinnerDanmakuShieldLevel.apply {
                setSelection(App.danmakuShieldLevel - 1)
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        App.danmakuShieldLevel = position + 1
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) { }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = (App.context.resources.displayMetrics.density * 400).toInt()
        params.height = (App.context.resources.displayMetrics.density * 300).toInt()
        dialog!!.window!!.attributes = params
    }
}