package com.axonactive.myroom.views

/**
 * Created by Phuong Nguyen on 7/22/2018.
 */
interface RiseNumberBase {
    fun start()
    fun withNumber(number: Float): RiseNumberTextView
    fun withNumber(number: Int): RiseNumberTextView
    fun setDuration(duration: Long): RiseNumberTextView
    fun setOnEnd(callback: RiseNumberTextView.EndListener)
}