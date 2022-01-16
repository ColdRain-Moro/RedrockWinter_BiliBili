package kim.bifrost.coldrain.wanandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kim.bifrost.rain.bilibili.utils.invokeStatic
import java.lang.reflect.ParameterizedType

/**
 * kim.bifrost.coldrain.wanandroid.base.BaseFragment
 * WanAndroid
 *
 * @author 寒雨
 * @since 2021/11/24 17:12
 **/
abstract class BaseFragment<T : ViewBinding> : Fragment() {

    protected lateinit var binding: T
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewBinding(inflater, container)
        return binding.root
    }

    private fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): T {
        return ((javaClass as ParameterizedType).actualTypeArguments
            .first { it is Class<*> && it.isAssignableFrom(ViewBinding::class.java) } as Class<*>)
            .invokeStatic<T>("inflate", layoutInflater, container, false)!!
    }

    open fun scrollToTop() {}

}