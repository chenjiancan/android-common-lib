package io.github.chenjiancan.android.analog

import androidx.annotation.IntDef
import io.github.chenjiancan.android.analog.Analog.Companion.VERBOSE
import io.github.chenjiancan.android.analog.Analog.Companion.DEBUG
import io.github.chenjiancan.android.analog.Analog.Companion.INFO
import io.github.chenjiancan.android.analog.Analog.Companion.WARN
import io.github.chenjiancan.android.analog.Analog.Companion.ERROR
import io.github.chenjiancan.android.analog.Analog.Companion.ASSERT

import io.github.chenjiancan.android.analog.Analog.Companion.isDebug
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
        const val VERBOSE = AndroidLog.VERBOSE
        const val DEBUG = AndroidLog.DEBUG
        const val INFO = AndroidLog.INFO
        const val WARN = AndroidLog.WARN
        const val ERROR = AndroidLog.ERROR
        const val ASSERT = AndroidLog.ASSERT

        var minLevel: Int = DEBUG
            set(value) {
                field = if (value > ASSERT)
                    ASSERT
                else
                    value
            }

        var isDebug = false
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

fun Analog(tag: String? = null, level: Int = DEBUG): Analog {

    val trimTag = if (tag != null && tag.length <= 23) {
        tag
    } else {
        tag?.substring(0, 23)
    }

    return object : Analog {
        override val tag: String
            get() = trimTag ?: super.tag
        override val realseMinLevel: Int
            get() = level
    }
}


// 判断是设置为 debug 模式，debug 模式下根据实际等级进行判断
// release 模式，判断是否系统 property 允许打印
fun Analog.isLoggable(priority: Int): Boolean {
    return (isDebug || priority >= realseMinLevel) &&
            AndroidLog.isLoggable(tag, priority)
}

private fun Analog.log(msg: String, priority: Int = DEBUG, throwable: Throwable? = null) {
    when (priority) {
        VERBOSE -> {
            AndroidLog.v(tag, msg, throwable)
        }
        DEBUG -> {
            AndroidLog.d(tag, msg, throwable)
        }
        INFO -> {
            AndroidLog.i(tag, msg, throwable)
        }
        WARN -> {
            AndroidLog.w(tag, msg, throwable)

        }
        ERROR -> {
            AndroidLog.e(tag, msg, throwable)

        }
        ASSERT -> {
            AndroidLog.e(tag, msg, throwable)
            assert(false) { "Analog ASSERT" }
        }
    }
}

fun Analog.v(msg: String, throwable: Throwable? = null) {
    if (isLoggable(VERBOSE))
        log(msg, VERBOSE, throwable)

}

fun Analog.d(msg: String, throwable: Throwable? = null) {
    if (isLoggable(DEBUG))
        log(msg, DEBUG, throwable)
}

fun Analog.i(msg: String, throwable: Throwable? = null) {
    if (isLoggable(INFO))
        log(msg, INFO, throwable)
}

fun Analog.w(msg: String, throwable: Throwable? = null) {
    if (isLoggable(WARN))
        log(msg, WARN, throwable)
}

fun Analog.e(msg: String, throwable: Throwable? = null) {
    if (isLoggable(ERROR))
        log(msg, ERROR, throwable)
}

// 支持 lazy 创建字符串
fun Analog.v(getMsg: () -> String, throwable: Throwable? = null) {
    if (isLoggable(VERBOSE)) {
        v(getMsg(), throwable)
    }
}

fun Analog.d(getMsg: () -> String, throwable: Throwable? = null) {
    if (isLoggable(DEBUG))
        d(getMsg(), throwable)
}

fun Analog.i(getMsg: () -> String, throwable: Throwable? = null) {
    if (isLoggable(INFO))
        i(getMsg(), throwable)
}

fun Analog.w(getMsg: () -> String, throwable: Throwable? = null) {
    if (isLoggable(WARN))
        w(getMsg(), throwable)
}

fun Analog.e(getMsg: () -> String, throwable: Throwable? = null) {
    if (isLoggable(ERROR))
        e(getMsg(), throwable)
}
