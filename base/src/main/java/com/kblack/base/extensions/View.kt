package com.kblack.base.extensions

import android.animation.Animator
import android.animation.IntEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

/**
 * 隐藏View
 * @receiver View
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * 显示View
 * @receiver View
 */
fun View.visible() {
    this.visibility = View.GONE
}

/**
 * View不可见但存在原位置
 * @receiver View
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * 设置 View 为 [View.VISIBLE]
 * 如果 [isVisible] 值为true，将 [View.setVisibility] 设置为 [View.VISIBLE],反之为 [View.GONE]
 *
 * @receiver View
 * @param isVisible Boolean 是否显示
 */
fun View.setVisible(isVisible: Boolean) {
    if (isVisible) visible() else gone()
}

/**
 * 设置 View 为 [View.GONE]
 * 如果 [isGone] 值为true，将 [View.setVisibility] 设置为 [View.GONE],反之为 [View.VISIBLE]
 *
 * @receiver View
 * @param isGone Boolean 是否隐藏
 */
fun View.setGone(isGone: Boolean) {
    if (isGone) visible() else gone()
}

/*************************************** View宽高相关 ********************************************/
/**
 * 设置 View 的高度
 * @receiver View
 * @param height Int 目标高度
 * @return View
 */
fun View.height(height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.height = height
    layoutParams = params
    return this
}

/**
 * 设置View的宽度
 * @receiver View
 * @param width Int 目标宽度
 * @return View
 */
fun View.width(width: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.width = width
    layoutParams = params
    return this
}

/**
 * 设置View的宽度和高度
 * @receiver View
 * @param width Int 要设置的宽度
 * @param height Int 要设置的高度
 * @return View
 */
fun View.widthAndHeight(width: Int, height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    params.width = width
    params.height = height
    layoutParams = params
    return this
}

fun View.getViewId(): Int {
    var id = id
    if (id == View.NO_ID) {
        id = View.generateViewId()
    }
    return id
}

/**
 * 给 [View] 设置带有防抖效果的点击事件
 *
 * @receiver [View]
 * @param delayTime Int 防抖间隔时间，单位是毫秒，默认值 500ms
 * @param listener (v: View) -> Unit 具体的点击事件
 * @see OnSingleClickListener
 */
fun View.setOnSingleClickListener(delayTime: Int = 500, listener: (v: View) -> Unit) {
    setOnClickListener(OnSingleClickListener(delayTime, listener))
}

class OnSingleClickListener(
    private val mDelayTime: Int = 500,
    private val mListener: (v: View) -> Unit
) : View.OnClickListener {

    private var mLastClickTime = 0L
    override fun onClick(v: View) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - mLastClickTime >= mDelayTime) {
            mLastClickTime = currentTimeMillis
            mListener.invoke(v)
        }
    }
}

/***
 * 设置延迟时间的View扩展
 * @param delay Long 延迟时间，默认1500毫秒
 * @return T
 */
fun <T : View> T.withTrigger(delay: Long = 800): T {
    triggerDelay = delay
    return this
}

/***
 * 点击事件的View扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    if (clickEnable()) {
        @Suppress("UNCHECKED_CAST")
        block(it as T)
    }
}

fun <T:View> T.longClick(block: (T) -> Unit) = setOnLongClickListener{
    @Suppress("UNCHECKED_CAST")
    block(it as T)
    true
}

/***
 * 带延迟过滤的点击事件View扩展
 * @param delay Long 延迟时间，默认800毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(time: Long = 800, block: (T) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            @Suppress("UNCHECKED_CAST")
            block(it as T)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

//@SuppressLint("CheckResult")
//fun <T : EditText> T.textChangeDebounce(time: Long = 500, block: (String) -> Unit) {
//    textChanges().skip(1).debounce(time, TimeUnit.MILLISECONDS)
//        .compose(RxThreadHelper.rxSchedulerHelper(this))
//        .map { it.toString() }
//        .subscribe { block(it) }
//}
//
//@SuppressLint("CheckResult")
//fun <T : EditText> T.textChangeSubscribe(block: (String) -> Unit): Disposable {
//    return textChanges()
//        .observeOn(AndroidSchedulers.mainThread())
//        .map { it.toString() }
//        .subscribe(block)
//}

/*解决TextView自动换行问题*/
fun <T : TextView> T.autoSpilt(): String {
    val rawText: String = text.toString() //原始文本
    val tvPaint: Paint = paint //paint，包含字体等信息
    val tvWidth: Float = width - paddingLeft - paddingRight.toFloat() //控件可用宽度
    //将原始文本按行拆分
    val rawTextLines =
        rawText.replace("\r".toRegex(), "").split("\n".toRegex()).toTypedArray()
    val sbNewText = StringBuilder()
    for (rawTextLine in rawTextLines) {
        if (tvPaint.measureText(rawTextLine) <= tvWidth) {
            //如果整行宽度在控件可用宽度之内，就不处理了
            sbNewText.append(rawTextLine)
        } else {
            //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
            var lineWidth = 0f
            var cnt = 0
            while (cnt != rawTextLine.length) {
                val ch = rawTextLine[cnt]
                lineWidth += tvPaint.measureText(ch.toString())
                if (lineWidth <= tvWidth) {
                    sbNewText.append(ch)
                } else {
                    sbNewText.append("\n")
                    lineWidth = 0f
                    --cnt
                }
                ++cnt
            }
        }
        sbNewText.append("\n")
    }
    //把结尾多余的\n去掉
    if (!rawText.endsWith("\n")) {
        sbNewText.deleteCharAt(sbNewText.length - 1)
    }
    return sbNewText.toString()
}