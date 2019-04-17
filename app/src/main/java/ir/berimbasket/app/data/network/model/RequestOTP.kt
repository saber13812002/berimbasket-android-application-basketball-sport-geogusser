package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class RequestOTP(@field:SerializedName("status")
                 var status: Int,
                 @field:SerializedName("msg")
                 var msg: String)