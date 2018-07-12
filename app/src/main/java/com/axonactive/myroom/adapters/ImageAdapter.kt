package com.axonactive.myroom.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.axonactive.myroom.R
import kotlinx.android.synthetic.main.activity_sign_up.view.*
import kotlinx.android.synthetic.main.profile_image_list_item.view.*


/**
 * Created by Phuong Nguyen on 5/19/2018.
 */
class ImageAdapter(private var context: Context, private var imageId : ArrayList<Int>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val grid : View
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            grid = inflater.inflate(R.layout.profile_image_list_item, null)
            val viewHolder : ViewHolder = ViewHolder(grid)
            viewHolder.imgView.setImageResource(imageId[position])
        }
        else {
            grid = convertView
        }
        return grid
    }

    override fun getCount(): Int {
        return imageId.size
    }

    override fun getItem(p0: Int): Any {
        return imageId.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    class ViewHolder(view : View) {
        var imgView : ImageView = view.profile_image_list_item
    }
}