package com.example.api_json_kotlin

fun main(){

    val testList = mutableListOf<String>("10", "9", "5", "7", "8", "6")
    val testList2 = mutableListOf<String>("10", "9", "5", "7", "8", "6", "1", "2", "3", "4")
    val testList3 = mutableListOf<Int>(10, 9, 5, 7, 8, 6, 1, 2, 3, 4)

    testList.sortBy { it }
    testList2.sortBy { it.toInt() }
    testList3.sortBy { it }

    println(testList)
    println(testList2)
    println(testList3)
}