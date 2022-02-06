package kim.bifrost.rain.bilibili.ui.view.fragment.dialog

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kim.bifrost.rain.bilibili.R
import kim.bifrost.rain.bilibili.databinding.FragmentCoinDialogBinding
import kim.bifrost.rain.bilibili.ui.viewmodel.frag.CoinDialogFragViewModel
import kim.bifrost.rain.bilibili.utils.toastConcurrent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * kim.bifrost.rain.bilibili.ui.view.fragment.dialog.CoinDialogFragment
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/2/3 13:37
 **/
class CoinDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCoinDialogBinding

    private lateinit var viewModel: CoinDialogFragViewModel

    private val aid: Int by lazy { requireArguments().getInt("aid") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CoinDialogFragViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoinDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            btnOne.setOnClickListener {
                handleCoinClick(btnOne, 1)
            }
            btnTwo.setOnClickListener {
                handleCoinClick(btnTwo, 2)
            }
            val dialog = dialog as BottomSheetDialog
            dialog.window!!
                .findViewById<View>(R.id.design_bottom_sheet)
                .background = ColorDrawable(Color.TRANSPARENT)
        }
    }

    private fun handleCoinClick(btn: ImageView, amount: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                ValueAnimator.ofFloat(0f, -50f, 0f).apply {
                    addUpdateListener {
                        btn.translationY = it.animatedValue as Float
                    }
                    addListener(
                        onEnd = {
                            lifecycleScope.launch {
                                delay(200)
                                dismiss()
                            }
                        }
                    )
                    duration = 500
                    start()
                }
            }
            val data = viewModel.coin(aid, amount)
            if (data.code != 0) {
                toastConcurrent(data.message)
                return@launch
            }
            toastConcurrent("投币成功!")
        }
    }

    companion object {
        fun newInstance(aid: Int): CoinDialogFragment {
            val args = Bundle()
                .apply { putInt("aid", aid) }
            val fragment = CoinDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}