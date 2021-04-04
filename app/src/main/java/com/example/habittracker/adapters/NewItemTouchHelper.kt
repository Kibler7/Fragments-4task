package com.example.habittracker.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView

class NewItemTouchHelper(private val adapter: HabitAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = UP or DOWN
        val swipeFlags = ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlags  , swipeFlags)

    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }
    override fun onMove(recyclerView: RecyclerView, dragged: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        val oldPosition = dragged.adapterPosition
        val newPosition = target.adapterPosition
        adapter.moveItem(oldPosition, newPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        adapter.deleteItem(pos)
    }

}