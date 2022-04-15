package com.example.demo.controller

import com.example.demo.dao.ToDoList
import com.example.demo.dao.ToDoListDao
import org.springframework.web.bind.annotation.*
import org.springframework.data.repository.query.Param

@RestController
@RequestMapping("/api/v1")
class ToDoListController(private val toDoListDao: ToDoListDao) {

    @GetMapping
    fun getToDoLists(): List<ToDoList> = toDoListDao.getAll()

    @PostMapping
    fun addToDoList(@Param("listName") listName: String) = toDoListDao.add(listName)

    @PostMapping("/{list-id}")
    fun deleteToDoList(@PathVariable("list-id") listId: Int): Boolean = toDoListDao.delete(listId)

    @PostMapping("/{list-id}/new-task")
    fun addTask(@PathVariable("list-id") listId: Int, @Param("description") description: String) =
        toDoListDao.addTask(listId, description)

    @PostMapping("/{list-id}/{task-id}")
    fun markAsCompleted(@PathVariable("list-id") listId: Int, @PathVariable("task-id") taskId: Int) =
        toDoListDao.markCompleted(listId, taskId)
}