package kim.bifrost.rain.retrofit

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * kim.bifrost.rain.retrofit.Converter
 * RainRetrofit
 * 序列化接口，对接各个序列化库
 *
 * @author 寒雨
 * @since 2022/1/16 0:27
 **/
interface Converter {
    fun <T> convert(body: ResponseBody, type: Type): T
}

class GsonConverter private constructor(
    private val gson: Gson
) : Converter {

    @Suppress("UNCHECKED_CAST")
    override fun <T> convert(body: ResponseBody, type: Type): T {
//        val adapter = gson.getAdapter(TypeToken.get(type))
//        val jsonReader = gson.newJsonReader(body.charStream())
//        val result = adapter.read(jsonReader)
//        if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
//            error("JSON document was not fully consumed.")
//        }
//        return (result as T).also { body.close() }
        return gson.fromJson(body.string(), TypeToken.get(type).type)
    }

    companion object {
        fun create(): GsonConverter {
            return GsonConverter(GsonBuilder().create())
        }

        fun create(gson: Gson): GsonConverter {
            return GsonConverter(gson)
        }
    }
}