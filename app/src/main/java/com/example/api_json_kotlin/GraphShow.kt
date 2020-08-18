package com.example.api_json_kotlin

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


class GraphShow : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_graph_show)
        drawGraph()
    }

    fun drawGraph() {
        var finalData = intent.extras?.getParcelableArrayList<PlayerShipJson>("ExtraData")

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

        graph.addSeries(series)
        series.setDataPointsRadius(100F)
        series.setColor(Color.GREEN)
        series.setDrawDataPoints(true)
        series.setDataPointsRadius(10F)
        series.setThickness(5)
        gridLabelRenderer.horizontalAxisTitle = "Ship Tier"
        gridLabelRenderer.verticalAxisTitle = "Win Rate %"

    }
}
