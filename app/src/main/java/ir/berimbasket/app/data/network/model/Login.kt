package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class Login (@field:SerializedName("id")
             var id: Int,
             @field:SerializedName("login")
             var login: Boolean)