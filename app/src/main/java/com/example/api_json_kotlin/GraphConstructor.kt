package com.example.api_json_kotlin

import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import org.json.JSONObject

class GraphConstructor : AppCompatActivity() {

    var finalArrayPerNation = mutableListOf<String>()
    var listSortedByNationAndTier = mutableListOf<PlayerShipJson>()

    //is called from MainActivity
    suspend fun SortForNation(masterJSON: MasterJson, playerOwnedDestroyersJsonObject: JSONObject, nationsList: List<String>, nationName: String, shipsPerNationList: MutableList<JSONObject>) {

        //TODO implement an exception when a requested nation does not give data

        var JSONArray = playerOwnedDestroyersJsonObject.getJSONArray("ships")

        for (i in 0 until JSONArray.length()){
            var shipInQuestion = playerOwnedDestroyersJsonObject.getJSONArray("ships").getJSONObject(i).getJSONObject("playerShipJsonClassData").get("nation")

            if (shipInQuestion == nationName){
                finalArrayPerNation.add(playerOwnedDestroyersJsonObject.getJSONArray("ships").getJSONObject(i).toString())
            }
        }

        for (i in 0 until finalArrayPerNation.size){

            //convert back to object for sorting
            val testingObject = Gson().fromJson<PlayerShipJson>(finalArrayPerNation[i], PlayerShipJson::class.java)
            listSortedByNationAndTier.add(testingObject)
        }

        //sort by shipTier
        listSortedByNationAndTier.sortBy { it.playerShipJsonClassData.shipTier.toInt()}
    }
}



