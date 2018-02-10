package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by mohammad hosein on 09/02/2018.
 */
class Notification(@field:SerializedName("id")
                   var id: Int,
                   @field:SerializedName("title")
                   var title: String,
                   @field:SerializedName("content")
                   var content: String,
                   @field:SerializedName("timestamp")
                   var timestamp: String,
                   @field:SerializedName("link")
                   var link: String,
                   @field:SerializedName("type")
                   var type: String)