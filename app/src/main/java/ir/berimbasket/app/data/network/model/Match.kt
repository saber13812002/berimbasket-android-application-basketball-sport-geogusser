package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class Match(@field:SerializedName("id")
            var id: Int,
            @field:SerializedName("status")
            var status: String?,
            @field:SerializedName("teamTitleA")
            var homeName: String?,
            @field:SerializedName("teamTitleB")
            var awayName: String?,
            @field:SerializedName("logoTitleA")
            var homeLogo: String?,
            @field:SerializedName("logoTitleB")
            var awayLogo: String?,
            @field:SerializedName("scoreDate")
            var scoreDate: String?,
            @field:SerializedName("scoreA")
            var homeScore: Int?,
            @field:SerializedName("scoreB")
            var awayScore: Int?,
            @field:SerializedName("link")
            var link: String?)