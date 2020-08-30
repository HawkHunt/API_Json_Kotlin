package com.example.api_json_kotlin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_graph_show.*


class GraphShow : AppCompatActivity() {

    // a variable that will be filled using data parsed from the MainActivity
    var playerId = ""

    // a testing function to make sure the activity is operating
    fun doATest(A: Int, B: Int){
        var C = A * B
    }

    //default On Create function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_show)

        //call the fillEntryListAndDrawGraph function
        fillEntryListAndDrawGraph()

        // set an on click listener for the Button in the activity
        Button_ReturnToSelect.setOnClickListener(){
            //when the button is pressed, call the return to select function
            returnToSelectMenu()
        }
    }

    // the function to be called when the back button in this activity is pressed
    private fun returnToSelectMenu() {
        // declare the intent of which activity needs to be started
        val i_GraphShow = Intent(this, MainActivity::class.java)
        //extra data that needs to be transferred back to the MainActivity
        i_GraphShow.putExtra("playerID", playerId)
        //start the activity using the previously declared Intent
        startActivity(i_GraphShow)
    }

    //the function to actually fill the data and then draw the graph
    fun fillEntryListAndDrawGraph() {
        // get the finalData from the MainActivity.kt file with the key of ExtraData
        var finalData = intent.extras?.getParcelableArrayList<PlayerShipJson>("ExtraData")
        //playerId = intent.getStringExtra("playerID")

        //declare an array of colors to be used to color the datapoints in the chart
        val colors = ArrayList<Int>()
        //declare a mutablelist of entries to be used in the data for the chart
        val entries = mutableListOf<Entry>()

        //if the data is not null the for loop may execute
        if (finalData != null) {

            // for every item in the finalData list that is provided by the main activity
            for (i in 0 until finalData.size){
                //declare a new Entry object
                var newEntry = Entry(
                    //set the X coordinate of the entry point to be the ship tier from the finalData list
                    finalData[i].playerShipJsonClassData.shipTier.toFloat(),
                    //set the Y coordinate of the entry point to be the winrate from the finalData list
                    finalData[i].playerShipJsonClassData.winRate.toFloat()
                )
                //add the new Entries to the mutable list
                entries.add(i, newEntry)


                //if the value is between the values given in the within range check then:

                //super Unicum winrate = purple
                if(finalData[i].playerShipJsonClassData.winRate.toFloat() in 65.1..100.0){
                    //declare a color for this option
                    colors.add(Color.argb(255, 225, 0, 255))
                }
                //unicum winrate = pink
                else if (finalData[i].playerShipJsonClassData.winRate.toFloat() in 55.1..65.0){
                    //declare a color for this option
                    colors.add(Color.argb(255, 255, 0, 157))
                }
                //good winrate = green
                else if (finalData[i].playerShipJsonClassData.winRate.toFloat() in 50.1..55.0){
                    //declare a color for this option
                    colors.add(Color.argb(255, 0, 252, 42))
                }
                //average winrate = yellow
                else if (finalData[i].playerShipJsonClassData.winRate.toFloat() in 48.1..50.0){
                    //declare a color for this option
                    colors.add(Color.argb(255, 233, 237, 2))
                }
                //below average winrate = orange
                else if (finalData[i].playerShipJsonClassData.winRate.toFloat() in 45.1..48.0){
                    //declare a color for this option
                    colors.add(Color.argb(255, 237, 163, 2))
                }
                //bad winrate = red
                else{
                    //declare a color for this option
                    colors.add(Color.argb(255, 255, 0, 0))
                }

            }
        }

        //declare a line data set
        var dataSet = LineDataSet(
            entries,
            "Winrate of ships from: ${finalData?.get(0)?.playerShipJsonClassData?.nation}"
        )
        // set the line color to white
        dataSet.color = Color.WHITE
        // set the width of the line to 3.0
        dataSet.lineWidth = 3.0F
        // set the circle colors according to the above function
        dataSet.circleColors = colors
        // set circle radius to 5.0
        dataSet.circleRadius = 5.0F
        // set the text size of the values to 12
        dataSet.valueTextSize = 12.0F
        // declare a line dataset to be the previously declared dataSet
        var lineData = LineData(dataSet)
        // allow highlighting for DataSet
        dataSet.isHighlightEnabled = true
        // Set Highlight color to Red
        dataSet.highLightColor = Color.RED
        // set the highlight line width to 2.0
        dataSet.highlightLineWidth = 2.0F
        // set the data in the chart to be the previously declared lineData
        chart.data = lineData
        // Updates the chart
        chart.invalidate()

    }
}
