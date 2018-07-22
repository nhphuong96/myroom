package com.axonactive.myroom.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.nineoldandroids.animation.ValueAnimator
import java.text.DecimalFormat

/**
 * Created by Phuong Nguyen on 7/22/2018.
 */
class RiseNumberTextView : TextView, RiseNumberBase {
    private val STOPPED = 0

    private val RUNNING = 1

    private var mPlayingState = STOPPED

    private var number: Float = 0.toFloat()

    private var fromNumber: Float = 0.toFloat()

    private var duration: Long = 1500
    /**
     * 1.int 2.float
     */
    private var numberType = 2

    private var fnum: DecimalFormat? = null

    private var mEndListener: EndListener? = null

    val sizeTable = intArrayOf(9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE)

    constructor(context: Context): super(context)

    constructor(context: Context, attr: AttributeSet): super(context, attr)

    constructor(context: Context, attr: AttributeSet, defStyle: Int): super(context, attr, defStyle)

    interface EndListener {
        fun onEndFinish()
    }


    fun isRunning(): Boolean {
        return mPlayingState == RUNNING
    }


    private fun runFloat() {
        val valueAnimator = ValueAnimator.ofFloat(fromNumber, number)
        valueAnimator.setDuration(duration)

        valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {

                setText(fnum!!.format(java.lang.Float.parseFloat(valueAnimator.getAnimatedValue().toString())))
                if (valueAnimator.getAnimatedFraction() >= 1) {
                    mPlayingState = STOPPED
                    if (mEndListener != null)
                        mEndListener!!.onEndFinish()
                }
            }


        })
        valueAnimator.start()
    }

    private fun runInt() {
        val valueAnimator = ValueAnimator.ofInt(fromNumber.toInt(), number.toInt())
        valueAnimator.setDuration(duration)

        valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {

                setText(valueAnimator.getAnimatedValue().toString())
                if (valueAnimator.getAnimatedFraction() >= 1) {
                    mPlayingState = STOPPED
                    if (mEndListener != null)
                        mEndListener!!.onEndFinish()
                }
            }
        })
        valueAnimator.start()
    }

    fun sizeOfInt(x: Int): Int {
        var i = 0
        while (true) {
            if (x <= sizeTable[i])
                return i + 1
            i++
        }
    }

    override  fun onFinishInflate() {
        super.onFinishInflate()
        fnum = DecimalFormat("##0.00")
    }

    override fun start() {

        if (!isRunning()) {
            mPlayingState = RUNNING
            if (numberType == 1)
                runInt()
            else
                runFloat()
        }
    }


    override fun withNumber(number: Float): RiseNumberTextView {

        this.number = number
        numberType = 2
        if (number > 1000) {
            fromNumber = number - Math.pow(10.0, (sizeOfInt(number.toInt()) - 2).toDouble()).toFloat()
        } else {
            fromNumber = number / 2
        }

        return this
    }

    override fun withNumber(number: Int): RiseNumberTextView {
        this.number = number.toFloat()
        numberType = 1
        if (number > 1000) {
            fromNumber = number - Math.pow(10.0, (sizeOfInt(number) - 2).toDouble()).toFloat()
        } else {
            fromNumber = (number / 2).toFloat()
        }

        return this

    }

    override fun setDuration(duration: Long): RiseNumberTextView {
        this.duration = duration
        return this
    }

    override fun setOnEnd(callback: EndListener) {
        mEndListener = callback
    }
}