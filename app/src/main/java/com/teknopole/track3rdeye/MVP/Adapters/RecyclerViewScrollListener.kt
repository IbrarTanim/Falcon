package com.teknopole.track3rdeye.MVP.Adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView

abstract class RecyclerViewScrollListener : RecyclerView.OnScrollListener() {

    private var isScrolling: Boolean = false
    private var currentItems = 0
    private var totalItems = 0
    private var scrollOutItems = 0


    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isScrolling = true
        }
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        currentItems = recyclerView!!.layoutManager.childCount
        totalItems = recyclerView.layoutManager.itemCount
        scrollOutItems = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        if (isScrolling && (currentItems + scrollOutItems) == totalItems) {
            isScrolling = false
            onScrolledToBottom()
        }
    }

    abstract fun onScrolledToBottom()


}