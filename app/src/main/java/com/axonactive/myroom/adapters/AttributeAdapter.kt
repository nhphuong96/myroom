package com.axonactive.myroom.adapters

import android.app.Dialog
import android.support.v7.app.AlertDialog
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.axonactive.myroom.R
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.models.RoomAttribute
import com.axonactive.myroom.validation.Validator
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.room_attribute_item.view.*

/**
 * Created by Phuong Nguyen on 6/3/2018.
 */
class AttributeAdapter(private val context: Context, private val attributes : ArrayList<RoomAttribute>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val grid: View
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            grid = inflater.inflate(R.layout.room_attribute_item, null)
            val viewHolder = ViewHolder(grid)
            viewHolder.imgView.setImageResource(context.resources.getIdentifier(attributes[position].iconStr, "drawable", context.packageName))
            viewHolder.tvTitle.text = attributes[position].title
            viewHolder.aaView.setOnClickListener { _ ->
                val title = viewHolder.tvTitle.text.toString()
                showDialogWithCustomView(title)
            }

        } else {
            grid = convertView
        }
        return grid
    }

    private fun showDialogWithCustomView(title : String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setView(R.layout.room_attribute_setting_view)
        builder.setNegativeButton(context.resources.getString(R.string.cancel), null)
        builder.setPositiveButton(context.resources.getString(R.string.update), null)
        val dialog : Dialog = builder.create()
        dialog.setOnShowListener { _ ->
            val etAttrValue : MaterialEditText = dialog.findViewById(R.id.id_attribute_value)
            val button : Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            button.text = context.resources.getString(R.string.accept)
            button.setOnClickListener { _: View? ->
                val resultString : String = title + " - " + etAttrValue.text.toString()
                Toast.makeText(context, resultString, Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun getCount(): Int {
        return attributes.size
    }

    override fun getItem(p0: Int): Any {
        return attributes[p0]
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