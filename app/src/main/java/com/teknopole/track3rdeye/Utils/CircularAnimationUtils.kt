package com.teknopole.track3rdeye.Utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

/**
 * Created by MD. ABDUR ROUF on 5/23/2018.
 */
class CircularAnimationUtils {
     fun RegisterCircularRevealEnterAnimation(v: View,centerX:Int,centerY:Int,width:Int,height:Int,startColor:Int,endColor: Int) {



        val finalRadius = (Math.max(width, height) / 2 + Math.max(width - centerX, height - centerY)).toFloat()

         val initialRadius = (Math.max(width, height) / 2).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(v, centerX, centerY, 150f, finalRadius)
        anim.interpolator = AccelerateInterpolator()
        anim.duration = 300
        anim.start()
        startColorAnimation(v,startColor,endColor,300)
    }
     fun RegisterCircularRevealExitAnimation(v: View,centerX:Int,centerY:Int,width:Int,height:Int,startColor:Int,endColor: Int) {
        val initRadius = Math.sqrt((width * width + height * height).toDouble()).toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(v, centerX, centerY, initRadius, 0f)
        anim.interpolator = DecelerateInterpolator()
        anim.duration = 200
        anim.start()
        startColorAnimation(v,startColor,endColor,200)
    }
    private fun startColorAnimation(view: View, startColor: Int, endColor: Int, duration: Int) {
        val anim = ValueAnimator()
        anim.setIntValues(startColor, endColor)
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator -> view.setBackgroundColor(valueAnimator.animatedValue as Int) }
        anim.duration = duration.toLong()
        anim.start()
    }
}