package com.example.api_json_kotlin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class MainActivity : AppCompatActivity() {

    private val okHttpClientVar = OkHttpClient()
    private val playerOwnedShipIdList = mutableListOf<Any>()
    private val allPlayerOwnedShipsWinrateList = mutableListOf<Any>()
    private var allPlayerShipsThatAreDestroyersWinRateList = mutableListOf<Any>()
    private var shipsListedAsDestroyersObjectPerNation = JSONObject()
    private var shipsListedAsDestroyersObjectPerNationArray = mutableListOf<JSONObject>()
    private var shipsOwnedByPlayerArray = JSONArray()
    private var filteredShipsOwnedForTypeDestroyer = mutableListOf<Any>()
    private var filteredNamesForShipsOwnedOfTypeDestroyer = mutableListOf<Any?>()
    private var shipNameBodyArray = mutableListOf<String?>()
    private var shipNameBody: String? = ""
    private var shipIdBody: String? = ""
    private var listOfKeys = mutableListOf<String>()
    private var countDownLatch: CountDownLatch = CountDownLatch(11)
    private var shipCountryOfOriginList = arrayOf<String>("france", "pan_asia", "uk", "usa", "ussr", "europe", "japan", "germany", "commonwealth", "italy")
    private val shipNameHashMap = HashMap<Any, Any>()
    private val shipNationHashMap = HashMap<Any, Any>()
    private val shipTierHashMap = HashMap<Any, String>()
    private val playerShipWinNumberHashMap = HashMap<Any, Any>()
    private val playerShipBattlesNumberHashMap = HashMap<Any, Any>()
    private var playerId = ""
    private var playerChosenNationToSearchName = ""
    private var stringToConvertToJson : PlayerShipJson? = null

    //TODO MAKE EMPTY BEFORE COMMIT
    private val applicationID = ""

    var playerShipJson = ""
    val graphConstructor = GraphConstructor()
    var masterJsonObject = MasterJson()
    var jsonObject = JSONObject()
    var shipIdList = mutableListOf<Any>()


    // a testing function to make sure the activity is operating
    fun mainActivityTestFunction(A: Int, B:Int): Int {
        var result = A * B
        return result
    }

    //default on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       //Button listener
        Button_playerIdAccept.setOnClickListener(){
            handleInitialNationToSearchForByUser()
        }
    }

    //
    private fun handleInitialNationToSearchForByUser() {
        // a group of radio buttons in the UI
        var radioGroup = NationSelectRadioGroup
        // find the selected radio button from the UI
        var selectedRadioButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
        playerChosenNationToSearchName = selectedRadioButton.text.toString()

        when(playerChosenNationToSearchName){
            "Pan America" -> playerChosenNationToSearchName = "commonwealth"
            "Pan European" -> playerChosenNationToSearchName = "europe"
            "USA" -> playerChosenNationToSearchName = "usa"
            "USSR" -> playerChosenNationToSearchName = "ussr"
            "Japan" -> playerChosenNationToSearchName = "japan"
            "Pan Asia" -> playerChosenNationToSearchName = "pan_asia"
            "UK" -> playerChosenNationToSearchName = "uk"
            "Italy" -> playerChosenNationToSearchName = "italy"
            "France" -> playerChosenNationToSearchName = "france"
            "Germany" -> playerChosenNationToSearchName = "germany"
        }

        handleInitialPlayedIdInputByUser()
    }

    //handle the UI input for the input field asking the user for his player ID
    fun handleInitialPlayedIdInputByUser(){

        //playerInput from a text field in the UI
        playerId = editText_playerId.text.toString()

        //check for input length
        if (editText_playerId.text.length < 9){
            // userfeedback function in the UI
            giveUserInputFeedback("Incorrect playerID Length", 1)
        }

        //Correct length input
        if (editText_playerId.text.length == 9){

            fetchShipNameJson()

            fetchShipIDJson()

            getIteratorKeysForShipsListerAsDestroyerPerNation()

            // userfeedback function in the UI
            giveUserInputFeedback("Correct playerID Length" , 0)
        }
    }

    //Construct a REST call and call the WG server to get a JSON that contains all destroyers for a given nation
     fun fetchShipNameJson(){

        //there are 10 listed nations in the game to loop through
        for (shipCountryOfOrigin in shipCountryOfOriginList ){

            //the constructed URL to call per loop iteration
            val shipNameUrlPerCountry = "https://api.worldofwarships.eu/wows/encyclopedia/ships/?application_id=$applicationID&type=Destroyer&fields=-description%2C-modules_tree%2C-modules%2C-default_profile%2C-upgrades%2C-images&nation=$shipCountryOfOrigin"

            // the request builder
            val shipNamesRequest = Request.Builder().url(shipNameUrlPerCountry).build()

            //a new call to be made
            okHttpClientVar.newCall(shipNamesRequest).enqueue(object : Callback {
                //when the call fails
                override fun onFailure(call: Call, e: IOException) {
                    //onCompleted.FeedbackFunction("failed to execute ship name request, server is down or unresponsive", 1)
                    countDownLatch.countDown()
                    println("failed")
                }
                //when the call fails
                override fun onResponse(call: Call, response: Response) {
                    // returned response
                    shipNameBody = response?.body?.string()
                    shipNameBodyArray.add(shipNameBody)
                    //create a json object using the response
                    shipsListedAsDestroyersObjectPerNation = JSONObject(shipNameBody).getJSONObject("data")
                    //a list of JSONObjects of ships
                    shipsListedAsDestroyersObjectPerNationArray.add(shipsListedAsDestroyersObjectPerNation)

                    //TODO Replace with Callbacks
                    countDownLatch.countDown()
                }
            })
        }
    }

    // Goes to the API and fetches ALL ships currently owned by the player
    private fun fetchShipIDJson() {
        println("Attempting to fetch Ship name JSON")

        //a constructed URL
        val shipIdURL = "https://api.worldofwarships.eu/wows/ships/stats/?application_id=$applicationID&in_garage=1&account_id=$playerId"

        //another request
        val shipIdRequest = Request.Builder().url(shipIdURL).build()

        //Ship ID call
        okHttpClientVar.newCall(shipIdRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //TODO implement userfeedbackfunction
                //onCompleted.FeedbackFunction("Could not fetch ship name Json, server is down or unresponsive", 1)
            }

            override fun onResponse(call: Call, response: Response) {
                shipIdBody = response?.body?.string()

                //Incorrect player ID input gives Null data
                if (shipIdBody?.contains("\"$playerId\":null}")!! || shipIdBody?.contains("\"$playerId\": null}")!!) {

                    //TODO( This run on UI Thread does not fire)
                }

                //No player Id is specified
                else if (shipIdBody?.contains("\"ACCOUNT_ID_NOT_SPECIFIED\",\"code\":402,\"value\":\"\"")!!) {

                    //TODO( This run on UI Thread does not fire)
                }

                else{

                    shipsOwnedByPlayerArray = JSONObject(shipIdBody).getJSONObject("data").getJSONArray(playerId)

                    for (i in 0 until shipsOwnedByPlayerArray.length()) {
                        val shipOwnedByPlayerObject = shipsOwnedByPlayerArray.getJSONObject(i)
                        playerOwnedShipIdList.add(shipOwnedByPlayerObject.get("ship_id"))

                        playerShipWinNumberHashMap[i] =
                            shipOwnedByPlayerObject.getJSONObject("pvp").get("wins")
                        playerShipBattlesNumberHashMap[i] =
                            shipOwnedByPlayerObject.getJSONObject("pvp").get("battles")

                        val playerWinNumberString: String = playerShipWinNumberHashMap[i].toString()
                        val playerTotalGameNumberString: String =
                            playerShipBattlesNumberHashMap[i].toString()

                        var winrate: Double = 0.0

                        //calculate winrate percentage
                        if (playerTotalGameNumberString.toInt() != 0) {
                            winrate =
                                ((playerWinNumberString.toDouble() / playerTotalGameNumberString.toDouble()) * 100)
                            allPlayerOwnedShipsWinrateList.add(winrate)
                        } else {
                            winrate = 0.0
                            allPlayerOwnedShipsWinrateList.add(winrate)
                        }
                    }
                    countDownLatch.countDown()
                }
            }
        })
    }

    // get the all the needed parts from all the JSONObjects and put them in their respective hashmaps
    private fun getIteratorKeysForShipsListerAsDestroyerPerNation() {
        countDownLatch.await()

        for (i in 0 until  shipsListedAsDestroyersObjectPerNationArray.size){
            val iteratorObject = shipsListedAsDestroyersObjectPerNationArray[i].keys()
            while (iteratorObject.hasNext()){

                val iteratorKeyAsUnknownShipIdFromNationSpecificShipList = iteratorObject.next()

                //Add shipnames to the respective hashmap
                val shipNameJSONObject = shipsListedAsDestroyersObjectPerNationArray.get(i).getJSONObject(iteratorKeyAsUnknownShipIdFromNationSpecificShipList).get("name")
                shipNameHashMap[iteratorKeyAsUnknownShipIdFromNationSpecificShipList] = shipNameJSONObject

                //Add the nation to the specific hashmap
                val shipNationJSONObject = shipsListedAsDestroyersObjectPerNationArray.get(i).getJSONObject(iteratorKeyAsUnknownShipIdFromNationSpecificShipList).get("nation")
                shipNationHashMap[iteratorKeyAsUnknownShipIdFromNationSpecificShipList] = shipNationJSONObject

                //Add the tier to the specific hashmap
                val shipTierJSONObject = shipsListedAsDestroyersObjectPerNationArray.get(i).getJSONObject(iteratorKeyAsUnknownShipIdFromNationSpecificShipList).get("tier")
                shipTierHashMap[iteratorKeyAsUnknownShipIdFromNationSpecificShipList] = shipTierJSONObject.toString()

                listOfKeys.add(iteratorKeyAsUnknownShipIdFromNationSpecificShipList)
            }
        }
        filterShipsByTypeDestroyerFromPlayerOwnedShipIdList()
    }

    // All the JSONObjects from the previous function still contain ALL types of ships: Battleships, carriers, cruisers and destroyers.
    // This function filters out the destroyers from that data and puts them in a new list
    fun filterShipsByTypeDestroyerFromPlayerOwnedShipIdList() {

        val completedListOfShipsToConvertBackToJson = mutableListOf<PlayerShipJson>()

        for (i in 0 until playerOwnedShipIdList.size){
            //checks if the ship_id's (keys) are in the masterlist for destroyers
            if (listOfKeys.contains(playerOwnedShipIdList[i].toString())){
                filteredShipsOwnedForTypeDestroyer.add(playerOwnedShipIdList[i].toString())
                allPlayerShipsThatAreDestroyersWinRateList.add(allPlayerOwnedShipsWinrateList[i])
            }
        }

        //filter the shipNameBodyArray using the filteredShipsOwnedForTypeDestroyer keys from the logic above
        for (shipId in 0 until shipNameBodyArray.size){
            for (filteredDestroyer in 0 until filteredShipsOwnedForTypeDestroyer.size){
                if (shipNameBodyArray[shipId]?.contains(filteredShipsOwnedForTypeDestroyer[filteredDestroyer].toString())!!){

                    shipIdList.add(filteredShipsOwnedForTypeDestroyer[filteredDestroyer])

                    filteredNamesForShipsOwnedOfTypeDestroyer.add(shipNameHashMap.getValue(filteredShipsOwnedForTypeDestroyer[filteredDestroyer]))

                    stringToConvertToJson = PlayerShipJson(shipNameHashMap.getValue(filteredShipsOwnedForTypeDestroyer[filteredDestroyer].toString()) as String, DetailedShipStatistics(filteredShipsOwnedForTypeDestroyer[filteredDestroyer].toString(), shipNationHashMap.getValue(filteredShipsOwnedForTypeDestroyer[filteredDestroyer].toString()) as String, shipTierHashMap.getValue(filteredShipsOwnedForTypeDestroyer[filteredDestroyer].toString()), allPlayerShipsThatAreDestroyersWinRateList[filteredDestroyer].toString()))

                    completedListOfShipsToConvertBackToJson.add(stringToConvertToJson!!)
                }
            }
        }

        //Gson Library functionality to convert back to JSON
        masterJsonObject = MasterJson(completedListOfShipsToConvertBackToJson)
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        playerShipJson = gsonPretty.toJson(masterJsonObject)
        jsonObject = JSONObject(playerShipJson)

        Coroutinelauncher()
    }

    //Multithreading part using coroutines to offload some calculations to another thread
    fun Coroutinelauncher(){
        GlobalScope.launch {
            val deferred =  async { graphConstructor.SortForNation(masterJsonObject, jsonObject, shipCountryOfOriginList.toList(), playerChosenNationToSearchName, shipsListedAsDestroyersObjectPerNationArray) }
            deferred.await()
            startShowGraph()
        }
    }

    //starting the next activity. called only once the needed calculations are complete
    suspend fun startShowGraph() {
        //attempts to start the GraphShow Activity
        val i_GraphShow = Intent(this, GraphShow::class.java)
        val testArr2 = graphConstructor.listSortedByNationAndTier
        val testArrayList = ArrayList(testArr2)
        i_GraphShow.putParcelableArrayListExtra("ExtraData", testArrayList)
        i_GraphShow.putExtra("playerID", playerId)

        startActivity(i_GraphShow)
    }

    //a function to provide the user with feedback using a UI Textfield
    private fun giveUserInputFeedback(feedbackMessage : String, status: Int){

            //status 0: No Error
            //status 1: Error
            if (status == 0){
                TextView_InfoText.setTextColor(Color.GREEN)
            }
            else{
                TextView_InfoText.setTextColor(Color.RED)
            }
            TextView_InfoText.text = feedbackMessage
            editText_playerId.text.clear()

    }

    //an interface to be implemented
    interface UserFeedBackInterface{
        fun FeedbackFunction(Msg: String, Sts: Int)
    }

}

//Dataclasses to be used in the conversion
data class MasterJson(val ships: MutableList<PlayerShipJson> = ArrayList())

@Parcelize
data class PlayerShipJson(val shipName: String = "default", val playerShipJsonClassData: DetailedShipStatistics) : Parcelable

@Parcelize
data class DetailedShipStatistics(val shipIdNumber: String = "123456", val nation: String = "default nation", val shipTier: String, var winRate: String) : Parcelable