package com.axonactive.myroom.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.axonactive.myroom.R
import com.axonactive.myroom.models.RoomAttribute
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
            val viewHolder: ViewHolder = ViewHolder(grid)
            viewHolder.imgView.setImageResource(context.resources.getIdentifier(attributes[position].iconStr, "drawable", context.packageName))
            viewHolder.tvTitle.text = attributes[position].title
        } else {
            grid = convertView
        }
        return grid
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

    class ViewHolder(view : View) {
        var imgView : ImageView = view.circleImageView!!
        var tvTitle : TextView = view.tvAttributeTitle!!
    }
}