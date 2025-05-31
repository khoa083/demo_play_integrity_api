package com.kblack.base.extensions

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Context.sp2px(spValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}

fun Fragment.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Fragment.px2dp(pxValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

fun Fragment.sp2px(spValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (spValue * scale + 0.5f).toInt()
}

fun Fragment.px2sp(pxValue: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (pxValue / scale + 0.5f).toInt()
}

///**
// * Chuyển đổi từ dp (density-independent pixels) sang px (pixels).
// *
// * @param dpValue Giá trị dp cần chuyển đổi.
// * @return Giá trị tương ứng trong px, được làm tròn đến số nguyên gần nhất.
// */
//fun Resources.dp2px(dpValue: Float): Int {
//    val scale = displayMetrics.density
//    return (dpValue * scale).roundToInt()
//}
//
///**
// * Chuyển đổi từ px (pixels) sang dp (density-independent pixels).
// *
// * @param pxValue Giá trị px cần chuyển đổi.
// * @return Giá trị tương ứng trong dp, được làm tròn đến số nguyên gần nhất.
// */
//fun Resources.px2dp(pxValue: Float): Int {
//    val scale = displayMetrics.density
//    return (pxValue / scale).roundToInt()
//}
//
///**
// * Chuyển đổi từ sp (scale-independent pixels) sang px (pixels).
// *
// * @param spValue Giá trị sp cần chuyển đổi.
// * @return Giá trị tương ứng trong px, được làm tròn đến số nguyên gần nhất.
// */
//fun Resources.sp2px(spValue: Float): Int {
//    val scale = displayMetrics.scaledDensity
//    return (spValue * scale).roundToInt()
//}
//
///**
// * Chuyển đổi từ px (pixels) sang sp (scale-independent pixels).
// *
// * @param pxValue Giá trị px cần chuyển đổi.
// * @return Giá trị tương ứng trong sp, được làm tròn đến số nguyên gần nhất.
// */
//fun Resources.px2sp(pxValue: Float): Int {
//    val scale = displayMetrics.scaledDensity
//    return (pxValue / scale).roundToInt()
//}