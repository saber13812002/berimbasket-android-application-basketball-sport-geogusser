package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class VerifyBot(var id: Int,
                @field:SerializedName("SignupStatus")
                var status: Boolean?)