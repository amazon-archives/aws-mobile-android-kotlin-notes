/*
Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.amazonaws.mobile.samples.awsomenotes.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.amazonaws.mobile.samples.awsomenotes.R
import com.amazonaws.mobile.samples.awsomenotes.models.Note

/**
 * Implements an ItemTouchHelper that deals with swipe-left.  It displays a red area underneath
 * the swipe to allow the user to delete an item within the recycler view.  When the delete item
 * is actually pressed, the callback "onRemove" is called
 */
class SwipeToDelete(context: Context, private val onRemove: (Note) -> Unit): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val background: Drawable = ColorDrawable(Color.RED)
    private val xMark: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_clear_24dp)!!.apply {
        setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }
    private val xMarkMargin: Int = context.resources.getDimension(R.dimen.ic_clear_margin).toInt()

    /**
     * Called on completion of a move operation - we don't use this.
     */
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean
            = false

    /**
     * Called on completion of a swipe operation - we use this to remove the item
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int)
            = onRemove((viewHolder as NoteListViewHolder).note!!)

    /**
     * Called during the swipe to animate the background draw
     */
    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?,
                             viewHolder: RecyclerView.ViewHolder?,
                             dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {
        if (viewHolder != null) {
            // If the item has already been swiped away, ignore it
            if (viewHolder.adapterPosition == -1) return

            val vr = viewHolder.itemView.right
            val vt = viewHolder.itemView.top
            val vb = viewHolder.itemView.bottom
            val vh = vb - vt
            val iw = xMark.intrinsicWidth
            val ih = xMark.intrinsicWidth

            background.setBounds(vr + dX.toInt(), vt, vr, vb)
            background.draw(c)

            val xMarkLeft = vr - xMarkMargin - iw
            val xMarkRight = vr - xMarkMargin
            val xMarkTop = vt + (vh - ih) / 2
            val xMarkBottom = xMarkTop + ih
            xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)
            xMark.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
