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

    var playerId = ""


    fun doATest(A: Int, B: Int){
        var C = A * B
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_graph_show)
        drawGraph()

        Button_ReturnToSelect.setOnClickListener(){
            returnToSelectMenu()
        }
    }

    private fun returnToSelectMenu() {
        val i_GraphShow = Intent(this, MainActivity::class.java)
        i_GraphShow.putExtra("playerID", playerId)

        startActivity(i_GraphShow)
    }

    fun drawGraph() {
        var finalData = intent.extras?.getParcelableArrayList<PlayerShipJson>("ExtraData")
        playerId = intent.getStringExtra("playerID")

        val colors = ArrayList<Int>()
        val entries = mutableListOf<Entry>()

        if (finalData != null) {
            for (i in 0 until finalData.size){
                var newEntry = Entry(
                    finalData[i].playerShipJsonClassData.shipTier.toFloat(),
                    finalData[i].playerShipJsonClassData.winRate.toFloat()
                )
                entries.add(i, newEntry)

                //super Unicum winrate = purple
                if(finalData[i].playerShipJsonClassData.winRate.toFloat() in 65.1..100.0){
                    colors.add(Color.argb(255, 225, 0, 255))
                }
                //unicum winrate = pink
                else if (finalData[i].playerShipJsonClassData.winRate.toFloat() in 55.1..65.0){
                    colors.add(Color.argb(255, 255, 0, 157))
                }
                //good winrate = green
                else if (finalData[i].playerShipJsonClassData.winRate.toFloat() in 50.1..55.0){
                    colors.add(Color.argb(255, 0, 252, 42))
                }
                //average winrate = yellow
                else if (finalData[i].playerShipJsonClassData.winRate.toFloat() in 48.1..50.0){
                    colors.add(Color.argb(255, 233, 237, 2))
                }
                //below average winrate = orange
                else if (finalData[i].playerShipJsonClassData.winRate.toFloat() in 45.1..48.0){
                    colors.add(Color.argb(255, 237, 163, 2))
                }
                //bad winrate = red
                else{
                    colors.add(Color.argb(255, 255, 0, 0))
                }

            }
        }

        var dataSet = LineDataSet(
            entries,
            "Winrate of ships from: ${finalData?.get(0)?.playerShipJsonClassData?.nation}"
        )

        dataSet.color = Color.WHITE
        dataSet.circleColors = colors
        dataSet.circleRadius = 5.0F
        dataSet.lineWidth = 3.0F
        dataSet.valueTextSize = 12.0F
        var lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()

    }
}
