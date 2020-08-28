package com.example.api_json_kotlin

import android.R.attr
import android.R.attr.data
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



        val entries = mutableListOf<Entry>()

        if (finalData != null) {
            for (i in 0 until finalData.size){
                var newEntry = Entry(finalData[i].playerShipJsonClassData.shipTier.toFloat(),finalData[i].playerShipJsonClassData.winRate.toFloat() )
                entries.add(i, newEntry )
            }
        }

        var dataSet = LineDataSet(entries, "Winrate of ships from: ${finalData?.get(0)?.playerShipJsonClassData?.nation}")
        dataSet.color = Color.WHITE
        var lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()

        //var graph = findViewById<GraphView>(R.id.graph)

        //var series = LineGraphSeries<DataPoint>()

        /*if (finalData != null) {
            for (i in 0 until finalData.size){
                var x = finalData[i].playerShipJsonClassData.shipTier.toDouble()
                var y = finalData[i].playerShipJsonClassData.winRate.toDouble()
                series.appendData(DataPoint(x, y), true, 10)
            }
        }

        add percentage character to vertical axis
        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    super.formatLabel(value, isValueX)
                } else {
                    super.formatLabel(value, isValueX) + " %"
                }
            }
        }

        var colorToUse = Color.WHITE

        graph.title = "Winrate"
        graph.titleColor = colorToUse
        series.color = colorToUse
        series.isDrawDataPoints = true
        series.dataPointsRadius = 100F
        series.dataPointsRadius = 10F
        series.thickness = 5
        graph.addSeries(series)*/

    }
}
