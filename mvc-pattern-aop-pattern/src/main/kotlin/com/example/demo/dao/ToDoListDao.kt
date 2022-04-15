package com.example.demo.dao

data class Task(val id: Int, val parentId: Int, val description: String, var completed: Boolean = false)

data class ToDoList(val id: Int, val name: String, val tasks: MutableList<Task> = mutableListOf())

interface ToDoListDao {

    fun add(name: String): ToDoList

    fun getAll(): List<ToDoList>

    fun getById(id: Int): ToDoList?

    fun delete(id: Int): Boolean

    fun addTask(listId: Int, description: String): Task

    fun markCompleted(listId: Int, taskId: Int): Boolean
}