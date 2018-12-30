package com.axonactive.myroom.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*

import com.axonactive.myroom.R
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.db.DatabaseHelper
import com.axonactive.myroom.db.model.Attribute
import com.axonactive.myroom.db.model.RoomAttribute
import com.axonactive.myroom.views.NumberTextWatcherForThousand

/**
 * Created by Phuong Nguyen on 7/18/2018.
 */
class BillCalculatorActivity : AppCompatActivity() {

    var databaseHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_calculator)
        initializeActionBar()
        val roomId : Long = intent.getLongExtra(Constants.ROOM_ID_EXTRA, 0L)
//        val riseNumber = findViewById<RiseNumberTextView>(R.id.riseNumber)
//        riseNumber.withNumber(1500000)
//                .setDuration(1000)
//                .start()

        renderLayoutDedendOnRoomSetting(roomId)
    }

    private fun renderLayoutDedendOnRoomSetting(roomId : Long) {
        var tableLayoutDetailElement = findViewById<TableLayout>(R.id.tableLayoutBillCalculator)
        var layoutParams = TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT)
        tableLayoutDetailElement.layoutParams = layoutParams

        var roomAttributes = databaseHelper.getRoomAttributeByRoomId(roomId)
        for (item : Attribute in roomAttributes) {
            var attribute = databaseHelper.getRoomAttribute(roomId, item.attributeId)
            if (attribute!!.attribute!!.name.contains("Electricity")) {
                tableLayoutDetailElement.addView(renderLayoutWithTwoEditText("Electricity", R.id.electricityLastIndex, R.id.electricityCurrentIndex))
                continue
            }
            if (attribute!!.attribute!!.name.contains("Water")) {
                var attributeName = attribute!!.unit!!.unitName
                if (attributeName.contains("m³")) {
                    tableLayoutDetailElement.addView(renderLayoutWithTwoEditText("Water", R.id.waterLastIndex, R.id.waterCurrentIndex))
                }
                else if (attributeName.contains("person")) {
                    tableLayoutDetailElement.addView(renderLayoutWithResult("Water", attribute))
                }
                else {
                    tableLayoutDetailElement.addView(renderLayoutWithResult("Water - please select 'person' or 'm³'", null))
                }
                continue
            }
            if (attribute!!.attribute!!.name.contains("Internet")) {
                var attributeName = attribute!!.unit!!.unitName
                if (attributeName.contains("month") || attributeName.contains("person")) {
                    tableLayoutDetailElement.addView(renderLayoutWithResult("Internet", attribute))
                }
                else {
                    tableLayoutDetailElement.addView(renderLayoutWithResult("Internet - please select 'person' or 'month'", null))
                }
            }
        }

        var billCalculatorLayout = findViewById<LinearLayout>(R.id.layoutBillCalculator)
        if (tableLayoutDetailElement.parent != null) {
            billCalculatorLayout.removeView(tableLayoutDetailElement)
        }
        billCalculatorLayout.addView(tableLayoutDetailElement)
    }

    private fun renderLayoutWithResult(title: String, roomAttribute: RoomAttribute?) : TableRow {
        var tableRow = TableRow(this);
        tableRow.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        var tvTitle = TextView(this)
        tvTitle.text = title
        tvTitle.textSize = 11f
        tvTitle.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)
        tableRow.addView(tvTitle)

        if (roomAttribute != null) {
            var tvValue = TextView(this)
            tvValue.textSize = 11f
            tvValue.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)

            var value = NumberTextWatcherForThousand.trimCommaOfString(roomAttribute.value).toInt()
            if (roomAttribute!!.unit!!.unitName.contains("month")) {
                tvValue.text = String.format(resources.getString(R.string.month_detail), value)
            }
            else if (roomAttribute!!.unit!!.unitName.contains("person")){
                var peopleCounter = getTotalPeopleInRoom(roomAttribute.room.roomId!!).toInt()
                tvValue.text = String.format(resources.getString(R.string.bill_detail), peopleCounter, value, value * peopleCounter)
            }

            var layoutParams = tvValue.layoutParams as TableRow.LayoutParams
            layoutParams.span = 2
            tableRow.addView(tvValue)
        }

        return tableRow
    }

    private fun getTotalPeopleInRoom(roomId : Long) : Int {
        return databaseHelper.getHolderByRoomId(roomId).size
    }

    private fun renderLayoutWithTwoEditText(title: String, lastIndexId: Int, newIndexId: Int) : TableRow
    {
        var tableRow = TableRow(this);
        tableRow.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        var tvTitle = TextView(this)
        tvTitle.text = title
        tvTitle.textSize = 11f
        tvTitle.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)

        var etLastIndex = EditText(this)
        etLastIndex.setEms(10)
        etLastIndex.inputType = InputType.TYPE_CLASS_NUMBER
        etLastIndex.hint = "Last Index"
        etLastIndex.textSize = 11f
        etLastIndex.id = lastIndexId
        etLastIndex.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)

        var etCurrentIndex = EditText(this)
        etCurrentIndex.setEms(10)
        etCurrentIndex.inputType = InputType.TYPE_CLASS_NUMBER
        etCurrentIndex.hint = "Current Index"
        etCurrentIndex.textSize = 11f
        etCurrentIndex.id = newIndexId
        etCurrentIndex.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)

        tableRow.addView(tvTitle)
        tableRow.addView(etLastIndex)
        tableRow.addView(etCurrentIndex)

        return tableRow
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