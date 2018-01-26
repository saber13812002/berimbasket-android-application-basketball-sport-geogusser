package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by mohammad hosein on 26/01/2018.
 */
class Question(@field:SerializedName("Title")
               var title: String?,
               @field:SerializedName("Schema")
               var schema: String?,
               @field:SerializedName("table")
               var table: String?)