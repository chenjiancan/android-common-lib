package io.github.chenjiancan.android.analog

import android.util.Log as AndroidLog


/**
 * Created by ChenJiancan on 2019/10/17.
 * 日志库
 *
 * 1. 方便被继承: 作为接口被继承
 * 2. 自动生成 TAG
 * 3. 方便使用： 实现接口的拓展方法作为日志输出接口，子类直接调用输出
 * 4. 日志等级，兼容 android.Log
 * 5. 可根据调试选择性输出等级日志，在debug模式下全部输出，在 release模式下只输出 info 以上
 * 6. lazy 输出
 */

interface Analog {
    companion object {
        var minLevel = AndroidLog.DEBUG
    }

    val tag: String
        get() = getTag(javaClass)

    val realseMinLevel: Int
        get() = minLevel
}

private fun getTag(clazz: Class<*>): String {
    val tag = clazz.simpleName
    return if (tag.length <= 23) {
        tag
    } else {
        tag.substring(0, 23)
    }
}

fun Analog(tag: String? = null, level: Int = AndroidLog.DEBUG): Analog {
    return object : Analog {
        override val tag: String
            get() = tag ?: super.tag
        override val realseMinLevel: Int
            get() = level
    }
}


// 判断是否release模式，是否系统 property 允许打印
fun Analog.isLoggable(priority: Int): Boolean {
    return (BuildConfig.DEBUG || priority >= realseMinLevel) &&
            AndroidLog.isLoggable(tag, priority)
}

private fun Analog.log(msg: String, priority: Int = AndroidLog.DEBUG, throwable: Throwable? = null) {
    when (priority) {
        AndroidLog.VERBOSE -> {
            AndroidLog.v(tag, msg, throwable)
        }
        AndroidLog.DEBUG -> {
            AndroidLog.d(tag, msg, throwable)
        }
        AndroidLog.INFO -> {
            AndroidLog.i(tag, msg, throwable)
        }
        AndroidLog.WARN -> {
            AndroidLog.w(tag, msg, throwable)

        }
        AndroidLog.ERROR -> {
            AndroidLog.e(tag, msg, throwable)

        }
        AndroidLog.ASSERT -> {
            AndroidLog.e(tag, msg, throwable)
            assert(false) { "Analog ASSERT" }
        }
    }
}

fun Analog.v(msg: String, throwable: Throwable? = null) {
    if (isLoggable(AndroidLog.VERBOSE))
        log(msg, AndroidLog.VERBOSE, throwable)

}

fun Analog.d(msg: String, throwable: Throwable? = null) {
    if (isLoggable(AndroidLog.DEBUG))
        log(msg, AndroidLog.DEBUG, throwable)
}

fun Analog.i(msg: String, throwable: Throwable? = null) {
    if (isLoggable(AndroidLog.INFO))
        log(msg, AndroidLog.INFO, throwable)
}

fun Analog.w(msg: String, throwable: Throwable? = null) {
    if (isLoggable(AndroidLog.WARN))
        log(msg, AndroidLog.WARN, throwable)
}

fun Analog.e(msg: String, throwable: Throwable? = null) {
    if (isLoggable(AndroidLog.ERROR))
        log(msg, AndroidLog.ERROR, throwable)
}

// 支持 lazy 创建字符串
fun Analog.v(getMsg: () -> String, throwable: Throwable? = null) {
    if (isLoggable(AndroidLog.VERBOSE)) {
        v(getMsg(), throwable)
    }
}

fun Analog.d(getMsg: () -> String, throwable: Throwable? = null) {
    if (isLoggable(AndroidLog.DEBUG))
        d(getMsg(), throwable)
}

fun Analog.i(getMsg: () -> String, throwable: Throwable? = null) {
    if (isLoggable(AndroidLog.INFO))
        i(getMsg(), throwable)
}

fun Analog.w(getMsg: () -> String, throwable: Throwable? = null) {
    if (isLoggable(AndroidLog.WARN))
        w(getMsg(), throwable)
}

fun Analog.e(getMsg: () -> String, throwable: Throwable? = null) {
    if (isLoggable(AndroidLog.ERROR))

        e(getMsg(), throwable)
}
