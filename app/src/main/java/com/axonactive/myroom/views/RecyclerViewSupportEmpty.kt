package com.axonactive.myroom.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.Toast

/**
 * Created by Phuong Nguyen on 5/27/2018.
 */
class RecyclerViewSupportEmpty : RecyclerView {

    private lateinit var emptyView : View

    private val emptyObserver : RecyclerView.AdapterDataObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            val adapter : Adapter<*> = adapter
            if (adapter.itemCount == 0) {
                emptyView.visibility = View.VISIBLE
                this@RecyclerViewSupportEmpty.visibility = View.GONE
            }
            else {
                emptyView.visibility = View.GONE
                this@RecyclerViewSupportEmpty.visibility = View.VISIBLE
            }
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            Toast.makeText(context, "on Item Range Changed", Toast.LENGTH_SHORT).show()
        }
    }

    constructor(context : Context) : super(context)

    constructor(context : Context, attributes: AttributeSet) : super(context, attributes)

    constructor(context : Context, attributeSet: AttributeSet, defStyle : Int) : super(context, attributeSet, defStyle)

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver)
        }
        emptyObserver.onChanged()

    }

    fun setEmptyView(empty : View) {
        this.emptyView = empty
    }
}