package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class Register (@field:SerializedName("id")
                var id: Int,
                @field:SerializedName("exist")
                var exist: Boolean,
                @field:SerializedName("passwordset")
                var passwordSet: Boolean)