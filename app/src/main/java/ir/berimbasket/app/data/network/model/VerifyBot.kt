package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class VerifyBot(@field:SerializedName("id")
                var id: Int,
                @field:SerializedName("SignupStatus")
                var status: Boolean?)