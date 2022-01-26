package kim.bifrost.rain.retrofit.internal

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kim.bifrost.rain.retrofit.Call
import kim.bifrost.rain.retrofit.RainRetrofit
import kim.bifrost.rain.retrofit.annotation.*
import kim.bifrost.rain.retrofit.annotation.Field
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.reflect.*
import java.net.URLEncoder

/**
 * kim.bifrost.rain.retrofit.internal.RetrofitProxyHandler
 * RainRetrofit
 *
 * @author 寒雨
 * @since 2022/1/15 19:20
 **/
class RetrofitProxyHandler<T>(
    private val clazz: Class<T>,
    private val retrofit: RainRetrofit
) : InvocationHandler {

    @Suppress("UNCHECKED_CAST")
    fun getInstance(): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz), this) as T
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun invoke(proxy: Any, method: Method, args: Array<out Any?>): Any? {
        val annotations = method.annotations
        val httpMethod = annotations.firstOrNull { it is GET || it is POST }
            ?: error("unknown http method with method ${method.name}")
        val params = method.parameterAnnotations
        fun handleUrl(sub: String): String {
            var subUrl = sub
            val queryParams = HashMap<String, String>()
            params.forEachIndexed { i, annotations ->
                val path = annotations.firstOrNull { a -> a is Path } as Path?
                val query = annotations.firstOrNull { q -> q is Query } as Query?
                path?.let {
                    subUrl = subUrl.replace("{${path.value}}", args[i].toString())
                }
                query?.let {
                    args[i]?.let { s -> queryParams[it.value] = s.toString() }
                }
            }
            if (queryParams.isNotEmpty()) {
                val builder = StringBuilder("?")
                val iter = queryParams.iterator()
                while (iter.hasNext()) {
                    val v = iter.next()
                    builder.append("${v.key}=${URLEncoder.encode(v.value, "utf-8")}")
                    if (iter.hasNext()) builder.append("&")
                }
                subUrl += builder.toString()
            }
            if (subUrl.startsWith("https://") || subUrl.startsWith("http://")) {
                return subUrl
            }
            return retrofit.baseurl.run { if (endsWith("/")) this else "$this/" } + subUrl
        }
        when(httpMethod) {
            is GET -> {
                val url = handleUrl(httpMethod.value)
                if (Call::class.java.isAssignableFrom(method.returnType)) {
                    val innerClazz = method.genericReturnType.getGenerics()[0]
                    return Call<Any>(
                        okhttpCall = retrofit.okHttpClient.newCall(
                            Request.Builder()
                                .url(url)
                                .build()
                        ),
                        type = innerClazz,
                        converter = retrofit.converter
                    )
                }
                // 协程拓展
                if (isSuspendMethod(method)) {
                    // 挂起函数编译后返回值类型变为Object，真正的返回值类型要去Continuation的泛型里面拿
                    // 我真麻了 太阴间了这也
                    val type = (method.genericParameterTypes.run { get(size - 1) }.getGenerics()[0] as WildcardType).lowerBounds[0]
                    return Call<Any>(
                        okhttpCall = retrofit.okHttpClient.newCall(
                            Request.Builder()
                                .url(url)
                                .build()
                        ),
                        type = type,
                        converter = retrofit.converter
                    ).execute()
                }
            }
            is POST -> {
                val url = handleUrl(httpMethod.value)
                if (annotations.any { it is FormUrlEncoded }) {
                    val body = FormBody.Builder().run {
                        params.forEachIndexed { i, annotations ->
                            val field = annotations.firstOrNull { it is Field } as Field? ?: return@forEachIndexed
                            add(field.value, args[i].toString())
                        }
                        build()
                    }
                    if (Call::class.java.isAssignableFrom(method.returnType)) {
                        val innerClazz = method.genericReturnType.getGenerics()[0]
                        return Call<Any>(
                            okhttpCall = retrofit.okHttpClient.newCall(
                                Request.Builder()
                                    .url(url)
                                    .post(body)
                                    .build()
                            ),
                            type = innerClazz,
                            converter = retrofit.converter
                        )
                    }
                    // 协程拓展
                    if (isSuspendMethod(method)) {
                        return Call<Any>(
                            okhttpCall = retrofit.okHttpClient.newCall(
                                Request.Builder()
                                    .url(url)
                                    .post(body)
                                    .build()
                            ),
                            type = method.genericReturnType,
                            converter = retrofit.converter
                        ).execute()
                    }
                }
            }
        }
        return null
    }

    private fun Type.getGenerics(): Array<Type> {
        return (this as ParameterizedType).actualTypeArguments
    }

    // 判断方法是否是挂起函数
    @RequiresApi(Build.VERSION_CODES.P)
    private fun isSuspendMethod(method: Method): Boolean {
        val last = method.genericParameterTypes.lastOrNull() ?: return false
        return last.typeName.startsWith("kotlin.coroutines.Continuation")
    }
}