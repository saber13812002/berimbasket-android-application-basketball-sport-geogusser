package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class Mission(@field:SerializedName("id")
              var id: Int,
              @field:SerializedName("title")
              var title: String?,
              @field:SerializedName("link")
              var link: String?,
              @field:SerializedName("score")
              var score: Int?,
              @field:SerializedName("level")
              var level: Int?,
              @field:SerializedName("lock")
              var lock: Int)
