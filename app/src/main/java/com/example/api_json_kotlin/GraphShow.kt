package com.example.api_json_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_graph_show.*
import java.math.BigDecimal
import java.math.RoundingMode


class GraphShow : AppCompatActivity(), OnChartValueSelectedListener,
    com.github.mikephil.charting.listener.OnChartValueSelectedListener {

    var playerId = ""
    var finalData2 = ArrayList<PlayerShipJson>()

    var techLineEntryList = mutableListOf<Entry>()
    var alternateTechLineEntryList = mutableListOf<Entry>()
    var premiumEntryList = mutableListOf<Entry>()

    // a testing function to make sure the activity is operating
    fun doATest(A: Int, B: Int){
        var C = A * B
    }

    //default On Create function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_show)

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
        i_GraphShow.putExtra("playerID", playerId)
        startActivity(i_GraphShow)
    }

    private fun calculateTotalWinrate(list: ArrayList<PlayerShipJson>): BigDecimal? {

        //TODO Round the winrate numbers This should be done in a nicer way
        for (i in 0 until finalData2.size){
            var unroundedWinrate = finalData2[i].playerShipJsonClassData.winRate
            val decimal = RoundTheNumber(unroundedWinrate.toFloat(), 5)
            finalData2[i].playerShipJsonClassData.winRate = decimal.toString()
        }

        var winPercentage  = 0.0F
        var totalWinPercentage = 0.0F
        for (i in 0 until list.size){
            winPercentage += list[i].playerShipJsonClassData.winRate.toFloat()
        }

        totalWinPercentage = winPercentage/list.size
        return RoundTheNumber(totalWinPercentage, 2)
    }

    fun RoundTheNumber(unRoundedNumber: Float, decimalPlaces: Int): BigDecimal? {
        var roundedNumber = BigDecimal(unRoundedNumber.toDouble()).setScale(
            decimalPlaces,
            RoundingMode.HALF_EVEN
        )
        return roundedNumber
    }

    //the function to actually fill the data and then draw the graph
    @SuppressLint("SetTextI18n")
    fun fillEntryListAndDrawGraph() {

        //http://api.worldofwarships.eu/wows/encyclopedia/ships/?application_id=4466360e1477d164feb2c0ce55c2d9d7&type=Destroyer&fields=-description%2C-modules_tree%2C-modules%2C-default_profile%2C-upgrades%2C-images&nation=
        //TODO TEMPORARY-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        val listOfCommonwealthPremiums = arrayListOf("Haida", "Vampire")
        val listOfEuropeanPremiums = arrayListOf("Friesland", "Småland", "Błyskawica", "Orkan", "Lappland")
        val listOfFrancePremiums = arrayListOf("Siroco", "Aigle", "Le Terrible", "Marceau")
        val listOfGermanyPremiums = arrayListOf("Z-44", "Z-35", "T-61", "Z-39")
        val listOfItalyPremiums = arrayListOf("Paolo Emilio",  "Leone")
        val listOfJapanPremiums = arrayListOf("Kamikaze R", "Kamikaze", "Yūdachi", "Tachibana Lima", "Asashio", "HSF Harekaze", "Fūjin", "Tachibana", "Asashio B", "AL Yukikaze", "Shinonome", "Arashi", "Hayate")
        val listOfPanAsiaPremiums = arrayListOf("Anshan", "Siliwangi", "Loyang")
        val listOfUsaPremiums = arrayListOf("Monaghan", "Black", "Sims", "Kidd", "Benham", "Smith", "Sims B", "Hill", "Somers")
        val listOfUkPremiums = arrayListOf("Campbeltown", "Gallant", "Cossack")
        val listOfUssrPremiums = arrayListOf("Gremyashchy", "Neustrashimy", "Okhotnik", "Leningrad", "DD R-10")

        val listOfJapanAlternateLineShips = arrayListOf("Minekaze", "Hatsuharu", "Shiratsuyu", "Akizuki", "Kitakaze", "Harugumo")
        val listOfUssrAlternateLineShips = arrayListOf( "Ognevoi", "Udaloi", "Grozovoi" )


        //playerId = intent.getStringExtra("playerID")
        val finalData = intent.extras?.getParcelableArrayList<PlayerShipJson>("ExtraData")
        finalData2 = intent.extras?.getParcelableArrayList<PlayerShipJson>("ExtraData") as ArrayList<PlayerShipJson>
        val colors = ArrayList<Int>()

        //if the data is not null the for loop may execute
        if (finalData != null) {

            var regularTechLineCounter = 0
            var premiumCounter = 0
            var alternateTechLineCounter = 0

            for (i in 0 until finalData.size){

                //declare a new Entry object
                var newEntry = Entry(
                    //set the X coordinate of the entry point to be the ship tier from the finalData list
                    finalData[i].playerShipJsonClassData.shipTier.toFloat(),
                    //set the Y coordinate of the entry point to be the win rate from the finalData list
                    finalData[i].playerShipJsonClassData.winRate.toFloat()
                )

                //TODO this is where the data is checked for premiums, specials and alternate lines. It is distributed accordingly
                //TODO there are 4 options
                //TODO Option 1 there are only techline ships
                //TODO Option 2 there are techline ships AND alternate line ships
                //TODO Option 3 There are techline ships and premium ships
                //TODO Option 4 there are techline ships, alternateline ships and premiums

                var listOfPremiumsToCheckAgainst = arrayListOf("")

                when(finalData[0].playerShipJsonClassData.nation){
                    "commonwealth" -> listOfPremiumsToCheckAgainst = listOfCommonwealthPremiums
                    "europe" -> listOfPremiumsToCheckAgainst = listOfEuropeanPremiums
                    "france" -> listOfPremiumsToCheckAgainst = listOfFrancePremiums
                    "germany" -> listOfPremiumsToCheckAgainst = listOfGermanyPremiums
                    "italy" -> listOfPremiumsToCheckAgainst = listOfItalyPremiums
                    "japan" -> listOfPremiumsToCheckAgainst = listOfJapanPremiums
                    "pan_asia" -> listOfPremiumsToCheckAgainst = listOfPanAsiaPremiums
                    "usa" -> listOfPremiumsToCheckAgainst = listOfUsaPremiums
                    "uk" -> listOfPremiumsToCheckAgainst = listOfUkPremiums
                    "ussr" -> listOfPremiumsToCheckAgainst = listOfUssrPremiums
                }



                if (listOfPremiumsToCheckAgainst.contains(finalData[i].shipName)){
                    premiumEntryList.add(premiumCounter, newEntry)
                    premiumCounter +=1
                }

                else if (listOfJapanAlternateLineShips.contains(finalData[i].shipName) || listOfUssrAlternateLineShips.contains(finalData[i].shipName)){
                    alternateTechLineEntryList.add(alternateTechLineCounter, newEntry)
                    alternateTechLineCounter += 1
                }

                else {
                    techLineEntryList.add(regularTechLineCounter, newEntry)
                    regularTechLineCounter += 1
                }


                //super Unicum winrate = purple
                if(finalData2[i].playerShipJsonClassData.winRate.toFloat() in 65.1..100.0){
                    colors.add(Color.argb(255, 225, 0, 255))
                }
                //unicum winrate = pink
                else if (finalData2[i].playerShipJsonClassData.winRate.toFloat() in 55.1..65.0){
                    colors.add(Color.argb(255, 255, 0, 157))
                }
                //good winrate = green
                else if (finalData2[i].playerShipJsonClassData.winRate.toFloat() in 50.1..55.0){
                    colors.add(Color.argb(255, 0, 252, 42))
                }
                //average winrate = yellow
                else if (finalData2[i].playerShipJsonClassData.winRate.toFloat() in 48.1..50.0){
                    colors.add(Color.argb(255, 233, 237, 2))
                }
                //below average winrate = orange
                else if (finalData2[i].playerShipJsonClassData.winRate.toFloat() in 45.1..48.0){
                    colors.add(Color.argb(255, 237, 163, 2))
                }
                //bad winrate = red
                else{
                    colors.add(Color.argb(255, 255, 0, 0))
                }
            }
        }

        //calculate the total win Percentage
        val testWinrate : BigDecimal  = finalData?.let { calculateTotalWinrate(it) }!!
        Text_WinRateText.text = "${testWinrate} %"
        TemporaryTextColorSettingFunction(testWinrate)
        Text_FixedWinRateText.setTextColor(Color.WHITE)


        //Turn a list of entries called premiumEntries into a premiumDataSet
        val premiumDataSet = LineDataSet(
            premiumEntryList, "Winrate of premium ships from: ${
                finalData?.get(
                    0
                )?.playerShipJsonClassData?.nation
            }"
        )

        //premiumDataSet parameters
        premiumDataSet.color = Color.YELLOW
        premiumDataSet.lineWidth = 3.0F
        premiumDataSet.setCircleColor(Color.YELLOW)
        premiumDataSet.circleHoleColor =Color.YELLOW
        premiumDataSet.circleHoleRadius = 3.0F
        premiumDataSet.circleRadius = 5.0F
        premiumDataSet.valueTextSize = 12.0F

        //Turn a list of entries called techLineOneEntries into a techLineDataSet
        val techLineDataSet = LineDataSet(
            techLineEntryList,
            "Winrate of ships from: ${finalData?.get(0)?.playerShipJsonClassData?.nation}"
        )

        //techLineDataSet parameters
        techLineDataSet.color = Color.WHITE
        techLineDataSet.lineWidth = 3.0F
        techLineDataSet.circleHoleRadius = 3.0F
        techLineDataSet.circleColors = colors
        techLineDataSet.circleRadius = 5.0F
        techLineDataSet.valueTextSize = 12.0F

        //highlighter
        techLineDataSet.isHighlightEnabled = true
        techLineDataSet.highLightColor = Color.RED
        techLineDataSet.highlightLineWidth = 2.0F

        //Turn a list of entries called alternateTechLineOneEntries into a alternateTechLineDataSet
        val alternateTechLineDataSet = LineDataSet(
            alternateTechLineEntryList,
            "Winrate of ships from: ${finalData?.get(0)?.playerShipJsonClassData?.nation}"
        )

        //alternateTechLineDataSet parameters
        alternateTechLineDataSet.color = Color.BLUE
        alternateTechLineDataSet.lineWidth = 3.0F
        alternateTechLineDataSet.circleHoleRadius = 3.0F
        alternateTechLineDataSet.circleColors = colors
        alternateTechLineDataSet.circleRadius = 5.0F
        alternateTechLineDataSet.valueTextSize = 12.0F

        val testLineDataSetWithPremiumAndTechline = mutableListOf(premiumDataSet, techLineDataSet)
        val testLineDataSetWithTechlineAndAlternate = mutableListOf(techLineDataSet, alternateTechLineDataSet)
        val testLineDataSetWithTechlineAndAlternateAndPremium = mutableListOf(techLineDataSet, premiumDataSet, alternateTechLineDataSet)

        println("premiums ${premiumEntryList.size}, alternates ${alternateTechLineEntryList.size}, techline ${techLineEntryList.size}" )

       //TODO Testcase of techline and premium ships European ships : 565421233 ---------------------------------PASSED---------------------------------------
       if (premiumEntryList.size > 0 && alternateTechLineEntryList.size == 0 && techLineEntryList.size > 0 ){
           val data = LineData(testLineDataSetWithPremiumAndTechline as List<ILineDataSet>?)
           chart.data = data

           println("theres ARE premiums,  and there ARE techlines")
       }
       //TODO testcase of techline and alternate ships playername: mar_pos USSR ships: 534657455 ---------------------------------PASSED---------------------------------------
       if (premiumEntryList.size == 0 && alternateTechLineEntryList.size > 0 && techLineEntryList.size > 0 ){
           val data = LineData(testLineDataSetWithTechlineAndAlternate as List<ILineDataSet>?)
           chart.data = data

           println("theres no premiums, there ARE alternates and there ARE techlines")
       }
       //TODO testcase of only premium ships Italian ships : 550614274 ---------------------------------PASSED---------------------------------------
       if(premiumEntryList.size > 0 && alternateTechLineEntryList.size == 0 && techLineEntryList.size == 0 ){
           val data = LineData(premiumDataSet)
           chart.data = data

           println("theres only premiums")
       }
       //TODO testcase of techline, alternate AND premiums: 557331936 ---------------------------------PASSED---------------------------------------
       if(premiumEntryList.size > 0 && alternateTechLineEntryList.size > 0 && techLineEntryList.size > 0 ){
           val data = LineData(testLineDataSetWithTechlineAndAlternateAndPremium as List<ILineDataSet>?)
           chart.data = data

           println("theres EVERYTHING")
       }
       //TODO testcase of only techline ships USA ship : 565421233 ---------------------------------PASSED---------------------------------------
       if(premiumEntryList.size == 0 && alternateTechLineEntryList.size == 0 && techLineEntryList.size > 0 ){
           val data = LineData(techLineDataSet)
           chart.data = data

           println("theres ONLY techline")
       }

        chart.setOnChartValueSelectedListener(this)

        chart.invalidate() // Updates the chart
    }

    //TODO implement a better solution this is practically double code
    fun TemporaryTextColorSettingFunction(winrate: BigDecimal?){

        if(winrate!!.toDouble() in 65.1..100.0){
            Text_WinRateText.setTextColor(Color.argb(255, 225, 0, 255))
        }
        //unicum winrate = pink
        else if (winrate!!.toDouble() in 55.1..65.0){
            Text_WinRateText.setTextColor(Color.argb(255, 255, 0, 157))
        }
        //good winrate = green
        else if (winrate!!.toDouble() in 50.1..55.0){
            Text_WinRateText.setTextColor(Color.argb(255, 0, 252, 42))
        }
        //average winrate = yellow
        else if (winrate!!.toDouble() in 48.1..50.0){
            Text_WinRateText.setTextColor(Color.argb(255, 233, 237, 2))
        }
        //below average winrate = orange
        else if (winrate!!.toDouble() in 45.1..48.0){
            Text_WinRateText.setTextColor(Color.argb(255, 237, 163, 2))
        }
        //bad winrate = red
        else{
            Text_WinRateText.setTextColor(Color.argb(255, 255, 0, 0))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e != null) {

            // TODO do not search for the e.x because the index of x is problematic due to the size of the array I think
            // TODO  Entry.x = ${e.x} is correct

            val particularWinrate = e.y
            val decimal = RoundTheNumber(particularWinrate, 5)
            var tempThing = finalData2.find { decimal.toString() == it.playerShipJsonClassData.winRate }

            if (tempThing != null) {
                Text_currentlyHighlightedShip.text = tempThing.shipName
            }
            else{
                Text_currentlyHighlightedShip.text = "Null"
                println("isNull")
            }
        }
    }

    override fun onNothingSelected() {
        Text_currentlyHighlightedShip.text = ""
    }
}

interface OnChartValueSelectedListener {
    /**
     * Called when a value has been selected inside the chart.
     *
     * @param e The selected Entry.
     * @param h The corresponding highlight object that contains information
     * about the highlighted position
     */
    fun onValueSelected(e: Entry?, h: Highlight?)

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    fun onNothingSelected()
}

