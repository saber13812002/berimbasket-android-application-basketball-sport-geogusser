package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class Match(var id: Int,
            var status: String?,
            @field:SerializedName("teamTitleA")
            var homeName: String?,
            @field:SerializedName("teamTitleB")
            var awayName: String?,
            @field:SerializedName("logoTitleA")
            var homeLogo: String?,
            @field:SerializedName("logoTitleB")
            var awayLogo: String?,
            var scoreDate: String?,
            @field:SerializedName("scoreA")
            var homeScore: Int?,
            @field:SerializedName("scoreB")
            var awayScore: Int?,
            var link: String?)