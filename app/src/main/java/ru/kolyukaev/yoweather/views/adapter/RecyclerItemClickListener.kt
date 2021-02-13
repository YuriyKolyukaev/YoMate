package ru.kolyukaev.yoweather.views.adapter

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class RecyclerItemClickListener (recyclerView: RecyclerView, private val mListener: OnItemClickListener?) : RecyclerView.OnItemTouchListener {

    private var mGestureDetector: GestureDetector = GestureDetector(recyclerView.context, object: GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return super.onSingleTapUp(e)
        }
        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
        }
    })

    interface OnItemClickListener {
        fun onItemClick (view: View, position: Int)
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
            return true
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("Not yet implemented")
    }
}