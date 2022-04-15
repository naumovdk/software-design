package com.example.demo.config

import com.example.demo.dao.ToDoListDao
import com.example.demo.dao.ToDoListInMemoryDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InMemoryDaoContextConfiguration {

    @Bean
    fun toDoListDao(): ToDoListDao = ToDoListInMemoryDao()
}