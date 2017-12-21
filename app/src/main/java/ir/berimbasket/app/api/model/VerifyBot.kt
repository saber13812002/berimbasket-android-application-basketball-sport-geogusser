package ir.berimbasket.app.api.model

import com.google.gson.annotations.SerializedName

class VerifyBot(var id: Int,
                @field:SerializedName("SignupStatus")
                var status: Boolean?)