package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class VerifyOTP(@field:SerializedName("status")
                var status: Int,
                @field:SerializedName("msg")
                var msg: String,
                @field:SerializedName("token")
                var token: String,
                @field:SerializedName("user_email")
                var email: String,
                @field:SerializedName("user_nicename")
                var nickName: String,
                @field:SerializedName("user_display_name")
                var displayName: String)