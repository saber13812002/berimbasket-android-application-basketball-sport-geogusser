package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Player (@field:SerializedName("id")
              var id: Int,
              @field:SerializedName("priority")
              var priority: Int,
              @field:SerializedName("username")
              var username: String?,
              @field:SerializedName("namefa")
              var name: String?,
              @field:SerializedName("address")
              var address: String?,
              @field:SerializedName("uImages")
              var profileImage: String?,
              @field:SerializedName("uInstagramId")
              var instagramId: String?,
              @field:SerializedName("uTelegramlId")
              var telegramId: String?,
              @field:SerializedName("height")
              var height: Int?,
              @field:SerializedName("weight")
              var weight: Int?,
              @field:SerializedName("city")
              var city: String?,
              @field:SerializedName("age")
              var age: Int?,
              @field:SerializedName("sex")
              var sex: String?,
              @field:SerializedName("coach")
              var coachName: String?,
              @field:SerializedName("teamname")
              var teamName: String?,
              @field:SerializedName("experience")
              var experience: String?,
              @field:SerializedName("post")
              var post: String?,
              @field:SerializedName("telegramphone")
              var phone: String?) : Serializable