package io.github.chenjiancan.android.analog

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

    val _logTag: String
        get() = getLogTag(javaClass)

    val realseMinLevel: Int
        get() = minLevel
}

private fun getLogTag(clazz: Class<*>): String {
    val tag = clazz.simpleName
    return if (tag.length <= 23) {
        tag
    } else {
        tag.substring(0, 23)
    }
}

fun Analog(logTag: String? = null, level: Int = DEBUG): Analog {

    val trimTag = if (logTag != null && logTag.length <= 23) {
        logTag
    } else {
        logTag?.substring(0, 23)
    }

    return object : Analog {
        override val _logTag: String
            get() = trimTag ?: super._logTag
        override val realseMinLevel: Int
            get() = level
    }
}


// 判断是设置为 debug 模式，debug 模式下根据实际等级进行判断
// release 模式，判断是否系统 property 允许打印
fun Analog.isLoggable(priority: Int): Boolean {
    return (isDebug || priority >= realseMinLevel) &&
            AndroidLog.isLoggable(_logTag, priority)
}

private fun Analog.log(msg: String, priority: Int = DEBUG, throwable: Throwable? = null) {
    when (priority) {
        VERBOSE -> {
            AndroidLog.v(_logTag, msg, throwable)
        }
        DEBUG -> {
            AndroidLog.d(_logTag, msg, throwable)
        }
        INFO -> {
            AndroidLog.i(_logTag, msg, throwable)
        }
        WARN -> {
            AndroidLog.w(_logTag, msg, throwable)

        }
        ERROR -> {
            AndroidLog.e(_logTag, msg, throwable)

        }
        ASSERT -> {
            AndroidLog.wtf(_logTag, msg, throwable)
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

fun Analog.wtf(msg: String, throwable: Throwable? = null) {
    log(msg, ASSERT, throwable)
}

// 支持 lazy 创建字符串
fun Analog.v(throwable: Throwable? = null, getMsg: () -> String) {
    if (isLoggable(VERBOSE)) {
        v(getMsg(), throwable)
    }
}

fun Analog.d(throwable: Throwable? = null, getMsg: () -> String) {
    if (isLoggable(DEBUG))
        d(getMsg(), throwable)
}

fun Analog.i(throwable: Throwable? = null, getMsg: () -> String) {
    if (isLoggable(INFO))
        i(getMsg(), throwable)
}

fun Analog.w(throwable: Throwable? = null, getMsg: () -> String) {
    if (isLoggable(WARN))
        w(getMsg(), throwable)
}

fun Analog.e(throwable: Throwable? = null, getMsg: () -> String) {
    if (isLoggable(ERROR))
        e(getMsg(), throwable)
}

fun Analog.wtf(throwable: Throwable? = null, getMsg: () -> String) {
   wtf(getMsg(), throwable)
}

// 无 throwable
fun Analog.v(getMsg: () -> String) {
    if (isLoggable(VERBOSE)) {
        v(getMsg())
    }
}

fun Analog.d(getMsg: () -> String) {
    if (isLoggable(DEBUG))
        d(getMsg())
}

fun Analog.i(getMsg: () -> String) {
    if (isLoggable(INFO))
        i(getMsg())
}

fun Analog.w(getMsg: () -> String) {
    if (isLoggable(WARN))
        w(getMsg())
}

fun Analog.e(getMsg: () -> String) {
    if (isLoggable(ERROR))
        e(getMsg())
}

fun Analog.wtf(getMsg: () -> String) {
    if (isLoggable(ERROR))
        wtf(getMsg())
}
