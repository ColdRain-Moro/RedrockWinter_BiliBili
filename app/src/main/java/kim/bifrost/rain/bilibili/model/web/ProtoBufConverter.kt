package kim.bifrost.rain.bilibili.model.web

import kim.bifrost.rain.bilibili.utils.invokeStatic
import kim.bifrost.rain.retrofit.Converter
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * kim.bifrost.rain.bilibili.model.web.ProtoBufConverter
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/19 22:20
 **/
class ProtoBufConverter : Converter {
    override fun <T> convert(body: ResponseBody, type: Type): T {
        return (type as Class<*>).invokeStatic<T>("parseFrom", body.byteString())!!.also { body.close() }
    }
}