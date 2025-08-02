package com.kblack.base.extensions

import android.animation.Animator
import android.view.animation.AnimationUtils
import android.animation.IntEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import com.kblack.base.R

private lateinit var viewsToAnimate: Array<ViewGroup>

fun showViewsWithAnimation() {
    viewsToAnimate.forEach { view ->
        ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
            duration = 500L
            interpolator = DecelerateInterpolator()
            view.isVisible = true
            start()
        }
    }
}

fun hideViewsWithAnimation() {
    viewsToAnimate.forEach { view ->
        if (view.isVisible) {
            ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).apply {
                duration = 300L
                interpolator = AccelerateInterpolator()
                start()
            }.doOnEnd { view.isVisible = false }
        }
    }
}
//-------------------------------------------------------------------------------------------------------------

/**
 * 设置宽度，带有过渡动画
 * @param targetValue 目标宽度
 * @param duration 时长
 * @param action 可选行为
 * @return 动画
 */
fun View.animateWidth(
    targetValue: Int, duration: Long = 400, listener: Animator.AnimatorListener? = null,
    action: ((Float) -> Unit)? = null
): ValueAnimator? {
    var animator: ValueAnimator? = null
    post {
        animator = ValueAnimator.ofInt(width, targetValue).apply {
            addUpdateListener {
                width(it.animatedValue as Int)
                action?.invoke((it.animatedFraction))
            }
            if (listener != null) addListener(listener)
            setDuration(duration)
            start()
        }
    }
    return animator
}

/**
 * 设置高度，带有过渡动画
 * @param targetValue 目标高度
 * @param duration 时长
 * @param action 可选行为
 * @return 动画
 */
fun View.animateHeight(
    targetValue: Int,
    duration: Long = 400,
    listener: Animator.AnimatorListener? = null,
    action: ((Float) -> Unit)? = null
): ValueAnimator? {
    var animator: ValueAnimator? = null
    post {
        animator = ValueAnimator.ofInt(height, targetValue).apply {
            addUpdateListener {
                height(it.animatedValue as Int)
                action?.invoke((it.animatedFraction))
            }
            if (listener != null) addListener(listener)
            setDuration(duration)
            start()
        }
    }
    return animator
}

/**
 * 设置宽度和高度，带有过渡动画
 * @param targetWidth 目标宽度
 * @param targetHeight 目标高度
 * @param duration 时长
 * @param action 可选行为
 * @return 动画
 */
fun View.animateWidthAndHeight(
    targetWidth: Int,
    targetHeight: Int,
    duration: Long = 400,
    listener: Animator.AnimatorListener? = null,
    action: ((Float) -> Unit)? = null
): ValueAnimator? {
    var animator: ValueAnimator? = null
    post {
        val startHeight = height
        val evaluator = IntEvaluator()
        animator = ValueAnimator.ofInt(width, targetWidth).apply {
            addUpdateListener {
                widthAndHeight(
                    it.animatedValue as Int,
                    evaluator.evaluate(it.animatedFraction, startHeight, targetHeight)
                )
                action?.invoke((it.animatedFraction))
            }
            if (listener != null) addListener(listener)
            setDuration(duration)
            start()
        }
    }
    return animator
}

//fun fadeInView(
//    view: View, listener: Animation.AnimationListener = object : Animation.AnimationListener {
//        override fun onAnimationStart(animation: Animation?) {}
//        override fun onAnimationEnd(animation: Animation?) {}
//        override fun onAnimationRepeat(animation: Animation?) {}
//    }
//) {
//    view.apply {
//        visibility = View.VISIBLE
//        animation?.cancel()
//        startAnimation(
//            AnimationUtils.loadAnimation(view.context, R.anim.fade_in_anim).apply {
//                setAnimationListener(listener)
//            })
//    }
//}

//fun fadeInView(
//    view: View,
//    inDuration: Long,
//    listener: Animation.AnimationListener = object : Animation.AnimationListener {
//        override fun onAnimationStart(animation: Animation?) {}
//        override fun onAnimationEnd(animation: Animation?) {}
//        override fun onAnimationRepeat(animation: Animation?) {}
//    }
//) {
//    view.apply {
//        visibility = View.VISIBLE
//        animation?.cancel()
//        startAnimation(
//            AnimationUtils.loadAnimation(view.context, R.anim.fade_in_anim).apply {
//                setAnimationListener(listener)
//                duration = inDuration
//            }
//        )
//    }
//}

//fun fadeOutView(view: View) {
//    when (view.visibility) {
//        View.VISIBLE -> {
//            view.apply {
//                animation?.cancel()
//                startAnimation(
//                    AnimationUtils.loadAnimation(view.context, R.anim.fade_out_anim).apply {
//                        setAnimationListener(object : Animation.AnimationListener {
//                            override fun onAnimationStart(p0: Animation?) {}
//
//                            override fun onAnimationEnd(p0: Animation?) {
//                                visibility = View.GONE
//                            }
//
//                            override fun onAnimationRepeat(p0: Animation?) {}
//                        })
//                    })
//            }
//        }
//        View.GONE -> {
//        }
//        View.INVISIBLE -> {
//        }
//    }
//}

//fun fadeOutView(view: View, listener: Animation.AnimationListener) {
//    when (view.visibility) {
//        View.VISIBLE -> {
//            view.apply {
//                animation?.cancel()
//                startAnimation(
//                    AnimationUtils.loadAnimation(view.context, R.anim.fade_out_anim).apply {
//                        setAnimationListener(listener)
//                    })
//            }
//        }
//        View.GONE -> {
//        }
//        View.INVISIBLE -> {
//        }
//    }
//}

//fun fadeOutView(view: View, inDuration: Long, listener: Animation.AnimationListener) {
//    when (view.visibility) {
//        View.VISIBLE -> {
//            view.apply {
//                visibility = View.VISIBLE
//                animation?.cancel()
//                startAnimation(
//                    AnimationUtils.loadAnimation(view.context, R.anim.fade_out_anim).apply {
//                        setAnimationListener(listener)
//                        duration = inDuration
//                    }
//                )
//            }
//        }
//        View.GONE -> {
//        }
//        View.INVISIBLE -> {
//        }
//    }
//}