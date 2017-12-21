package ir.berimbasket.app.api.model

import com.google.gson.annotations.SerializedName

class Register (var id: Int,
                var exist: Boolean,
                @field:SerializedName("passwordset")
                var passwordSet: Boolean)