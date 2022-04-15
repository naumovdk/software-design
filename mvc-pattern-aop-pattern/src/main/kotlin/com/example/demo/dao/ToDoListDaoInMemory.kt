package com.example.demo.dao

import com.example.demo.exception.NotFoundException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class ToDoListInMemoryDao : ToDoListDao {

    private val lastId = AtomicInteger(0)

    private val toDoLists: MutableMap<Int, ToDoList> = ConcurrentHashMap()

    override fun add(name: String): ToDoList {
        val id = lastId.incrementAndGet()
        val toDoList = ToDoList(id, name)
        toDoLists[id] = toDoList
        return toDoList
    }

    override fun getAll(): List<ToDoList> = ArrayList(toDoLists.values)

    override fun getById(id: Int): ToDoList? = toDoLists[id]

    override fun delete(id: Int): Boolean = toDoLists.remove(id) != null

    override fun addTask(listId: Int, description: String): Task {
        val toDoList = forcedGetById(listId)
        val id = lastId.incrementAndGet()
        val task = Task(id, listId, description)
        toDoList.tasks.add(task)
        return task
    }

    override fun markCompleted(listId: Int, taskId: Int): Boolean {
        val toDoList = forcedGetById(listId)
        val task = toDoList.tasks.firstOrNull { it.id == taskId }
            ?: throw NotFoundException("Task with id {$taskId} in list with id {$listId}")

        val previousState = task.completed
        task.completed = true
        return !previousState
    }

    private fun forcedGetById(listId: Int): ToDoList =
        toDoLists[listId] ?: throw NotFoundException("List with id {$listId} not found")
}