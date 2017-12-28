package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Player (var id: Int,
              var username: String?,
              @field:SerializedName("namefa")
              var name: String?,
              var address: String?,
              @field:SerializedName("uImages")
              var profileImage: String?,
              @field:SerializedName("uInstagramId")
              var instagramId: String?,
              @field:SerializedName("uTelegramlId")
              var telegramId: String?,
              var height: Int?,
              var weight: Int?,
              var city: String?,
              var age: Int?,
              var sex: String?,
              @field:SerializedName("coach")
              var coachName: String?,
              @field:SerializedName("teamname")
              var teamName: String?,
              var experience: String?,
              var post: String?,
              @field:SerializedName("telegramphone")
              var phone: String?) : Serializable