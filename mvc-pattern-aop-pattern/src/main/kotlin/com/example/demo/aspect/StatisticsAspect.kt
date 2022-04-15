package com.example.demo.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component


@Aspect
@Component
class StatisticsAspect {
    private val count: MutableMap<String, Int> = HashMap()
    private val totalTime: MutableMap<String, Long> = HashMap()

    @Around("execution(* com.example.demo.controller.ToDoListController.*(..))")
    private fun aroundFindAccountsAdvice(processingJoinPoint: ProceedingJoinPoint): Any {
        val time = System.currentTimeMillis()
        val result = processingJoinPoint.proceed(processingJoinPoint.args)
        val executionTime = System.currentTimeMillis() - time
        val signature = processingJoinPoint.signature.toString()
        totalTime[signature] = totalTime.getOrDefault(signature, 0.toLong()) + executionTime
        count[signature] = count.getOrDefault(signature, 0) + 1
        printStatistic(signature)
        return result
    }

    private fun printStatistic(signature: String) {
        val totalTime = totalTime[signature]
        val count = count[signature]
        println("--")
        println(signature)
        println("   amount: $count")
        println("   total execution time: $totalTime ms")
        println("   average execution time: " + totalTime!! / count!! + " ms")
        println()
    }
}