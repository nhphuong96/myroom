package com.axonactive.myroom.adapters

import android.app.Dialog
import android.support.v7.app.AlertDialog
import android.content.Context
import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.axonactive.myroom.R
import com.axonactive.myroom.db.DatabaseHelper
import com.axonactive.myroom.models.RoomAttribute
import com.rengwuxian.materialedittext.MaterialEditText
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.room_attribute_item.view.*

/**
 * Created by Phuong Nguyen on 6/3/2018.
 */
class AttributeAdapter(private val context: Context, private val roomAttributes : RoomAttribute) : BaseAdapter() {

    private val db = DatabaseHelper(context)

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val grid: View
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            grid = inflater.inflate(R.layout.room_attribute_item, null)
            val viewHolder = ViewHolder(grid)
            viewHolder.imgView.setImageResource(context.resources.getIdentifier(roomAttributes.attributes[position].icon, "drawable", context.packageName))
            viewHolder.tvTitle.text = roomAttributes.attributes[position].name
            viewHolder.aaView.setOnClickListener { _ ->
                showDialogWithCustomView(roomAttributes.roomId, roomAttributes.attributes[position].attributeId!!)
            }

        } else {
            grid = convertView
        }
        return grid
    }

    private fun showDialogWithCustomView(roomId: Long, attrId : Long) {
        val roomAttr = db.getRoomAttribute(roomId, attrId)
        if (roomAttr!!.room != null && roomAttr!!.attribute != null) {
            val builder = AlertDialog.Builder(context)
            var view : View = LayoutInflater.from(context).inflate(R.layout.dialog_header_with_switch, null)
            var tvTitle : TextView = view.findViewById(R.id.textView5)
            var switch : SwitchCompat = view.findViewById(R.id.editable)
            switch.visibility = View.GONE
            tvTitle.text = roomAttr.attribute!!.name
            builder.setCustomTitle(view)
            var viewBody : View = View.inflate(context, R.layout.room_attribute_setting_view, null)
            var rdoGroup = viewBody.findViewById<RadioGroup>(R.id.unit_group)
            rdoGroup.weightSum = 1f
            var rprms : RadioGroup.LayoutParams
            var unitsInDb = db.getAllUnits()
            for (i in unitsInDb.indices) {
                var radioBtn = AppCompatRadioButton(context)
                radioBtn.text = unitsInDb[i].unitName
                radioBtn.id = unitsInDb[i].unitId!!.toInt()
                radioBtn.textSize = 10f
                rprms = RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
                rprms.weight = 0.25f
                rdoGroup.addView(radioBtn, rprms)
            }
            builder.setView(viewBody)
            builder.setNegativeButton(context.resources.getString(R.string.cancel), null)
            builder.setPositiveButton(context.resources.getString(R.string.update), null)
            val dialog : Dialog = builder.create()
            dialog.setOnShowListener { _ ->
                val etAttrValue : MaterialEditText = dialog.findViewById(R.id.id_attribute_value)
                val button : Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                button.text = context.resources.getString(R.string.accept)
                etAttrValue.setText(roomAttr.value)
                val rdoGroup  = dialog.findViewById<RadioGroup>(R.id.unit_group)
                var checkedId : Int = 0
                rdoGroup!!.setOnCheckedChangeListener { _, i ->
                    checkedId = rdoGroup.getChildAt(i).id
                }
                button.setOnClickListener { view : View? ->
                    val resultString : String = roomAttr.attribute!!.name+ " - " + etAttrValue.text.toString() +
                            " - " + checkedId
                    Toasty.info(context, resultString, Toast.LENGTH_SHORT, true).show()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

    }

    override fun getCount(): Int {
        return roomAttributes.attributes.size
    }

    override fun getItem(p0: Int): Any {
        return roomAttributes.attributes[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    class ViewHolder {
        val aaView : View
        var imgView : ImageView
        var tvTitle : TextView

        constructor(view : View) {
            imgView = view.circleImageView!!
            tvTitle = view.tvAttributeTitle!!
            aaView = view
        }
    }
}