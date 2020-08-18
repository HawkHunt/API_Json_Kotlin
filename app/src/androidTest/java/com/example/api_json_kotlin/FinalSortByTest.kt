package com.example.api_json_kotlin

import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun main() {

    var testJSONObject = JSONObject("{\"ships\":[{\"playerShipJsonClassData\":{\"nation\":\"usa\",\"shipIdNumber\":\"4292753392\",\"shipTier\":\"2\",\"winRate\":\"50.0\"},\"shipName\":\"Sampson\"},{\"playerShipJsonClassData\":{\"nation\":\"usa\",\"shipIdNumber\":\"4274927600\",\"shipTier\":\"4\",\"winRate\":\"33.33333333333333\"},\"shipName\":\"Clemson\"},{\"playerShipJsonClassData\":{\"nation\":\"usa\",\"shipIdNumber\":\"4266538992\",\"shipTier\":\"3\",\"winRate\":\"71.42857142857143\"},\"shipName\":\"Wickes\"},{\"playerShipJsonClassData\":{\"nation\":\"usa\",\"shipIdNumber\":\"3768465392\",\"shipTier\":\"2\",\"winRate\":\"66.66666666666666\"},\"shipName\":\"Smith\"}]}\n")

    SortForNation(testJSONObject, "usa")
}

fun SortForNation( playerOwnedDestroyersJsonObject: JSONObject, nationName: String) {

    var finalArrayPerNation2 = mutableListOf<String>()

    var testListToSort2 = mutableListOf<PlayerShipJson2>()
    //TODO implement an exception when a requested nation does not give data

    var JSONArray = playerOwnedDestroyersJsonObject.getJSONArray("ships")

    for (i in 0 until JSONArray.length()){
        var shipInQuestion = playerOwnedDestroyersJsonObject.getJSONArray("ships").getJSONObject(i).getJSONObject("playerShipJsonClassData").get("nation")

        if (shipInQuestion == nationName){
            finalArrayPerNation2.add(playerOwnedDestroyersJsonObject.getJSONArray("ships").getJSONObject(i).toString())
        }
    }

    for (i in 0 until finalArrayPerNation2.size){

        //convert back to object for sorting
        val testingObject = Gson().fromJson<PlayerShipJson2>(finalArrayPerNation2[i], PlayerShipJson2::class.java)
        testListToSort2.add(testingObject)
    }

    testListToSort2.sortBy { it.playerShipJsonClassData.shipTier}
}

data class PlayerShipJson2(val shipName: String = "default", val playerShipJsonClassData: DetailedShipStatistics2)

data class DetailedShipStatistics2(val shipIdNumber: String = "123456", val nation: String = "default nation", val shipTier: String , val winRate: String)
