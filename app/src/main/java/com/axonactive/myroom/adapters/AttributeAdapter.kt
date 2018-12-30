package com.axonactive.myroom.adapters

import android.app.Dialog
import android.support.v7.app.AlertDialog
import android.content.Context
import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.SwitchCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.axonactive.myroom.R
import com.axonactive.myroom.db.DatabaseHelper
import com.axonactive.myroom.models.RoomAttribute
import com.axonactive.myroom.views.NumberTextWatcherForThousand
import com.rengwuxian.materialedittext.MaterialEditText
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.dialog_header_with_switch.view.*
import kotlinx.android.synthetic.main.room_attribute_item.view.*
import kotlinx.android.synthetic.main.room_attribute_setting_view.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * Created by Phuong Nguyen on 6/3/2018.
 */
class AttributeAdapter(private val context: Context, private val roomAttributes: RoomAttribute) : BaseAdapter() {

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

    private fun reInitRoomAttributes() {
        roomAttributes.attributes.clear()
        val attrs = db.getRoomAttributeByRoomId(roomAttributes.roomId)
        for (i in attrs.indices) {
            roomAttributes.attributes.add(attrs[i])
        }
        notifyDataSetChanged()
    }

    private fun showDialogWithCustomView(roomId: Long, attrId: Long) {
        val roomAttr = db.getRoomAttribute(roomId, attrId)
        if (roomAttr!!.room != null && roomAttr!!.attribute != null) {
            val builder = AlertDialog.Builder(context)
            var view: View = LayoutInflater.from(context).inflate(R.layout.dialog_header_with_switch, null)
            val headerViewHolder = DialogHeaderViewHolder(view)
            headerViewHolder.tvTitle.text = roomAttr.attribute!!.name
            headerViewHolder.editable.visibility = View.GONE
            builder.setCustomTitle(view)

            var viewBody: View = View.inflate(context, R.layout.room_attribute_setting_view, null)
            val bodyViewHolder = DialogBodyViewHolder(viewBody, db, context)
            builder.setView(viewBody)
            builder.setNegativeButton(context.resources.getString(R.string.cancel), null)
            builder.setPositiveButton(context.resources.getString(R.string.update), null)

            val dialog: Dialog = builder.create()
            dialog.setOnShowListener { _ ->
                val button: Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                button.text = context.resources.getString(R.string.accept)
                bodyViewHolder.etPrice.setText(roomAttr.value)
                bodyViewHolder.etPrice.addTextChangedListener(NumberTextWatcherForThousand(bodyViewHolder.etPrice))

                var curSelected = bodyViewHolder.rdoGroup.getChildAt(roomAttr.unit!!.unitId!!.minus(1).toInt()) as AppCompatRadioButton
                curSelected.isChecked = true

                var checkedUnitId = bodyViewHolder.rdoGroup.checkedRadioButtonId
                bodyViewHolder.rdoGroup.setOnCheckedChangeListener { _ , i ->
                    when(i) {
                        1 -> checkedUnitId = 1
                        2 -> checkedUnitId = 2
                        3 -> checkedUnitId = 3
                        4 -> checkedUnitId = 4
                    }
                }
                button.setOnClickListener { _: View? ->
                    if (db.updateRoomAttribute(roomAttr.room.roomId!!,
                                    roomAttr.attribute!!.attributeId!!,
                                    bodyViewHolder.etPrice.text.toString(),
                                    checkedUnitId.toLong()) > 0) {
                        reInitRoomAttributes()
                        Toasty.info(context, String.format(context.resources.getString(R.string.updated_attribute_successfully),
                                roomAttr.attribute!!.name,
                                NumberTextWatcherForThousand.trimCommaOfString(bodyViewHolder.etPrice.text.toString()),
                                (bodyViewHolder.rdoGroup.getChildAt(checkedUnitId - 1) as AppCompatRadioButton).text),
                                Toast.LENGTH_SHORT, true).show()
                        dialog.dismiss()
                    } else {
                        Toasty.error(context, String.format(context.resources.getString(R.string.updated_attribute_unsuccessfylly), roomAttr.attribute!!.name),
                                Toast.LENGTH_SHORT, true).show()
                    }
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

    class DialogHeaderViewHolder {
        val tvTitle: TextView
        val editable: SwitchCompat

        constructor(view: View) {
            tvTitle = view.textView5
            editable = view.editable
        }
    }

    class DialogBodyViewHolder {
        val rdoGroup : RadioGroup
        val etPrice : MaterialEditText
        val db : DatabaseHelper
        val context : Context

        constructor(view : View, db: DatabaseHelper, context : Context) {
            rdoGroup = view.unit_group
            etPrice = view.id_attribute_value
            this.db = db
            this.context = context
            createUnitGroup()
        }

        private fun createUnitGroup() {
            var rprms: RadioGroup.LayoutParams
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
        }

    }

    class ViewHolder {
        val aaView: View
        var imgView: ImageView
        var tvTitle: TextView

        constructor(view: View) {
            imgView = view.circleImageView!!
            tvTitle = view.tvAttributeTitle!!
            aaView = view
        }
    }
}