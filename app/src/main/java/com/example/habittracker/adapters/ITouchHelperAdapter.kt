package com.example.habittracker.adapters

interface ITouchHelperAdapter {

    fun deleteItem(position: Int)

    fun moveItem(startPosition: Int, nextPosition: Int)
}