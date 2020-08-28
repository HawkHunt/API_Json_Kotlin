package com.example.api_json_kotlin

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.api_json_kotlin", appContext.packageName)
    }

    //testing the testingfunction in the production code
    // this is  to be removed later
    @Test
    fun testMainActivityTestFunction(){
        val result = activityRule.activity.mainActivityTestFunction(2,2)
        assert(result == 4)
    }

    //This is a UI test but I am trying it here anyway
    @Test
    fun testMainActivityOnCreate(){

    }

    //This is a UI test but I am trying it here anyway
    @Test
    fun testHandleInitialNationToSearchForByUser(){

    }

    //This is a UI test but I am trying it here anyway
    @Test
    fun testHandleInitialPlayedIdInputByUser(){

    }

    //test if the application can fetch a JSON and test if the errors it throws are the correct ones
    interface testUserFeedBackInterface : MainActivity.UserFeedBackInterface{
        fun testFeedbackFunction(Msg: String, Sts: Int)
    }

    @Test
    fun fetchShipNameJson() {


    }
}