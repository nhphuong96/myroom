package com.axonactive.myroom.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView

import com.axonactive.myroom.R
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.views.RiseNumberTextView

/**
 * Created by Phuong Nguyen on 7/18/2018.
 */
class BillCalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_calculator)
        initializeActionBar()
        val str : Long = intent.getLongExtra(Constants.ROOM_ID_EXTRA, 0L)
        val riseNumber = findViewById<RiseNumberTextView>(R.id.riseNumber)
        riseNumber.withNumber(100000)
                .setDuration(1500)
                .start()
    }

    private fun initializeActionBar() {
        var toolbar : Toolbar = findViewById<Toolbar>(R.id.toolbar)
        var title : TextView = toolbar.findViewById<TextView>(R.id.toolbar_title)
        title.text = "Calculate bill"
        title.textSize = 14f
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_backspace))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.blue)))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}