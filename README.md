# Analog
基于 android.util.Log 的日志库，依赖 Kotlin

## 依赖

```
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}
dependencies {
    // VERSION 为具体版本号
    implementation 'com.github.chenjiancan:android-common-lib:{VERSION}'}
```

## 使用用法
以下几种方法选择一种

1. 继承 Analog 接口
就这样。 在类内部使用 v(), d(), i(), e(), w() 进行对应等级的日志输出就行。

```
class MainActivity : AppCompatActivity(), Analog {

    override fun onCreate(savedInstanceState: Bundle?) {
        // ...
        v("hello vebose")
        e("hello error")
    }
}
```

2. 创建 Analog 实例

```
    val logger = Analog(tag = javaClass.simpleName)
    logger.v("hi verbose")
    logger.e("hi error")
```

## 配置具体表现

0. 系统约束： 默认下，Analog 根据 Android 系统对具体 log.tag 这个 property 的值，进行过滤等级的输出输出，这个行为和
android.util.Log 的 isLoggable() 方法是一致的。 比如：
"adb shell setprop log.tag.MainActivity D" 设置 MainActivity 这个 tag 输出等级为 D，则 v() 不会打印到
logcat 上。

1. 设置运行模式， 通过给 Analog.isDebug 赋值，把 app 运行模式告知 Analog

```
Analog.isDebug = true
```

2. 设置 release 模式下的等级
如： 想要设置 app 以 release 模式运行时，日志最低的打印等级为 info，则通过如下设置

```
Analog.minLevel = Analog.INFO
```

3. 例子

```
    Analog.isDebug = false
    Analog.minLevel = Analog.INFO

    // 并且通过 adb 设置   adb shell setprop log.tag.MainActivity W

    val logger = Analog(tag = "MainActivity")
    logger.v("verbose")
    logger.d("debug")
    logger.i("info")
    logger.w("warning")
    logger.e("error")

```

以上例子，会输出 warning 和 error，因为 isDebug=false 和 Analog.minLevel 作用导致 v(),d() 都不会输出。
而 “adb shell setprop log.tag.MainActivity W” 导致 i() 输出不了;
如果 adb shell setprop log.tag.MainActivity I, 则会输出 info, waring, error;
如果 adb shell setprop log.tag.MainActivity V, 也会输出 info, waring, error;
再把 isDebug = true, 则全部输出 verbose, debug, info, waring, error;