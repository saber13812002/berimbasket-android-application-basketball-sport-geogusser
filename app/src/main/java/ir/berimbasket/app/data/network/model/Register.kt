package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class Register (var id: Int,
                var exist: Boolean,
                @field:SerializedName("passwordset")
                var passwordSet: Boolean)