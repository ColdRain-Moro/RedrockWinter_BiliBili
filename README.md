# BiliBili

> 好，我来作死了
>
> 既然之前已经写过一个WanAndroid，那么下一个项目肯定不能简单了
>
> 于是我选择了最难的哈哈哈哈哈哈，开摆就完啦

## 简要介绍

仿写BiliBili，实现其中比较核心的功能

## API接口

[哔哩哔哩-API收集整理](https://github.com/SocialSisterYi/bilibili-API-collect)

## 功能展示

// TODO

## 技术亮点

- Retrofit简单实现

## 心得体会

### Seek API

事实上API接口整理是非常不完善的，你甚至没办法找到首页推荐视频的API。

于是我翻阅了很多github项目，自己收集整理了一些API

这里放一些api收集整理里面没有的api，以备不时之需

原来官方有[开放接口文档](https://openhome.bilibili.com/doc)... 也放在这里吧

### Debug Retrofit

其实我也没想到我写出来的东西会这么漏洞百出

我总结了两个原因: 我个人对java反射API不够熟悉 / Kotlin代码编译为class后的某些东西过于阴间

#### First. Gson序列化对象后获取字段出现异常

> java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap cannot be cast to xxxxxx

首先我问了下度娘，发现是`Gson#fromJson(json, type)`中的type不对劲造成的问题

在没有使用typeToken的情况下，直接把有泛型的对象的Class传入这个方法就会导致这个问题

于是我发现我对这方面的处理不到位，借鉴了一些retrofit-gson-converter的代码，解决了这个问题

然后我发现这个问题依然存在？!

这可真是让人摸不着头脑，但上面做的确实不是无用功。实际上，如果不是我执着于兼容协程，我已经可以开心的码界面了

众所周知，原版retrofit是支持将接口中的函数用suspend修饰来直接支持协程的 （即帮你调用了Call#await方法)

我已经知道suspend修饰的方法在编译后会在其尾部加上一个Continuation（状态机）类型的参数，于是在之前的设计中，我利用这个特性来判断代理的方法是否是挂起函数

但我没想到的是，被suspend修饰的方法的返回值类型居然也改变了，它们永远返回Object类型，而原来的返回值类型被放在了Continuation的泛型里

说起来也不难理解 但我debug的时候真的受了不少苦，我是在断点了无数次后发现了传入GsonConverter的type居然是一个Object的Class

然后我怀疑自己获取返回值类型的方法用错了，但上网搜索后我发现自己并没有用错

然后我便怀疑起了suspend关键字，于是开了一个测试项目，将编译后的含有suspend关键字的kotlin class再用recaf反编译为了java代码

于是就有了这张图 谜题终于揭晓

![](https://gitee.com/coldrain-moro/images_bed/raw/master/images/DM5J@%9E_E}}CN()]7Y$NSS.png)

![](https://gitee.com/coldrain-moro/images_bed/raw/master/images/recaf-continuation.png)

在那之后还有一个小插曲

因为原版retrofit一旦在协程中执行，将自动帮我们切到IO调度器下的协程

而我并没有这么处理，于是直接在Main调度器下的协程下发起了网络请求。奇怪的是，没有任何报错，但是网络请求的数据一直下不来，也折腾了我不少时间。

### 请求视频资源需要加请求标头 referer

其实获取到的视频url是不能直接使用的，我用浏览器访问后得到的都是403的结果

后来百度了才知道，是要加上referer表头才能拿到视频流的 (referer的内容就是这个视频的bilibili网页播放地址)