package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class CheckUsername (@field:SerializedName("id")
                     var id: Int,
                     @field:SerializedName("exist")
                     var exist: Boolean)