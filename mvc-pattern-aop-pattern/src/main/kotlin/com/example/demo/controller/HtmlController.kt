package com.example.demo.controller

import com.example.demo.dao.ToDoListDao
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HtmlController(private val toDoListDao: ToDoListDao) {

    @GetMapping("/")
    fun lists(model: Model): String {
        model["lists"] = toDoListDao.getAll()
        return "lists"
    }
}