package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class Update(@field:SerializedName("code")
             var code: Int,
             @field:SerializedName("status")
             var status: String,
             @field:SerializedName("version")
             var version: Int,
             @field:SerializedName("apk_url")
             var apkUrl: String?,
             @field:SerializedName("file_name")
             var fileName: String?,
             @field:SerializedName("file_size_byte")
             var fileSizeByte: String?,
             @field:SerializedName("change_log")
             var changeLog: List<String>?)