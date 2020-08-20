package com.example.api_json_kotlin

import android.content.Intent
import android.graphics.Color
import com.example.api_json_kotlin.R
import android.os.Bundle
import android.text.Layout
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.activity_graph_show.*
import kotlinx.android.synthetic.main.activity_main.*


class GraphShow : AppCompatActivity() {

    var playerId = ""

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

        var graph = findViewById<GraphView>(R.id.graph)

        var series = LineGraphSeries<DataPoint>()
        var gridLabelRenderer = GridLabelRenderer(graph)

        if (finalData != null) {
            for (i in 0 until finalData.size){

                var x = finalData[i].playerShipJsonClassData.shipTier.toDouble()
                var y = finalData[i].playerShipJsonClassData.winRate.toDouble()
                series.appendData(DataPoint(x,y),true, 10)
            }
        }

        graph.title = "Winrate"
        graph.addSeries(series)
        series.setDataPointsRadius(100F)
        series.setColor(Color.BLUE)
        series.setDrawDataPoints(true)
        series.setDataPointsRadius(10F)
        series.setThickness(5)


    }
}
