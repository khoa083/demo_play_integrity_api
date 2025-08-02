package com.kblack.base.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

// Extension properties for cleaner access
private val Context.density: Float
    get() = resources.displayMetrics.density

private val Fragment.density: Float
    get() = resources.displayMetrics.density

// Context extensions - using TypedValue for SP conversions
// * Chuyển đổi từ dp (density-independent pixels) sang px (pixels).
fun Context.dp(value: Float): Int = (value * density).roundToInt()
fun Context.dp(value: Int): Int = dp(value.toFloat())

// * Chuyển đổi từ sp (scale-independent pixels) sang px (pixels).
fun Context.sp(value: Float): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    value,
    resources.displayMetrics
).roundToInt()
fun Context.sp(value: Int): Int = sp(value.toFloat())

fun Context.px2dp(value: Float): Float = value / density
fun Context.px2dp(value: Int): Float = px2dp(value.toFloat())

fun Context.px2sp(value: Float): Float = value / TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    1f,
    resources.displayMetrics
)
fun Context.px2sp(value: Int): Float = px2sp(value.toFloat())

// Fragment extensions
fun Fragment.dp(value: Float): Int = requireContext().dp(value)
fun Fragment.dp(value: Int): Int = requireContext().dp(value)

fun Fragment.sp(value: Float): Int = requireContext().sp(value)
fun Fragment.sp(value: Int): Int = requireContext().sp(value)

fun Fragment.px2dp(value: Float): Float = requireContext().px2dp(value)
fun Fragment.px2dp(value: Int): Float = requireContext().px2dp(value)

fun Fragment.px2sp(value: Float): Float = requireContext().px2sp(value)
fun Fragment.px2sp(value: Int): Float = requireContext().px2sp(value)

// Resources extensions
fun Resources.dp(value: Float): Int = (value * displayMetrics.density).roundToInt()
fun Resources.dp(value: Int): Int = dp(value.toFloat())

fun Resources.sp(value: Float): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    value,
    displayMetrics
).roundToInt()
fun Resources.sp(value: Int): Int = sp(value.toFloat())

fun Resources.px2dp(value: Float): Float = value / displayMetrics.density
fun Resources.px2dp(value: Int): Float = px2dp(value.toFloat())

fun Resources.px2sp(value: Float): Float = value / TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    1f,
    displayMetrics
)
fun Resources.px2sp(value: Int): Float = px2sp(value.toFloat())