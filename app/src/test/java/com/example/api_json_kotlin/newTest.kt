package com.example.api_json_kotlin

import android.content.Context
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class newTest {

    //this is a mock dependency
    @Mock
    private lateinit var mockContext: MainActivity

    @Test
    fun test1(){
        var c = mockContext.mainActivityTestFunction(2,2)
        assertEquals(4, c)
    }
}