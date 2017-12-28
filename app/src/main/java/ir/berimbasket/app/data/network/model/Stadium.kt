package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class Stadium(var id: Int,
              var title: String?,
              @field:SerializedName("PlaygroundLatitude")
              var latitude: String?,
              @field:SerializedName("PlaygroundLongitude")
              var longitude: String?,
              @field:SerializedName("PlaygroundType")
              var type: String?,
              @field:SerializedName("ZoomLevel")
              var zoomLevel: Int?,
              var address: String?,
              @field:SerializedName("PgImages")
              var images: String?,
              @field:SerializedName("PgInstagramId")
              var instagramId: String?,
              @field:SerializedName("PgTlgrmChannelId")
              var telegramChannelId: String?,
              @field:SerializedName("PgTlgrmGroupJoinLink")
              var telegramGroupId: String?,
              @field:SerializedName("PgTlgrmGroupAdminId")
              var telegramAdminId: String?,
              var roof: String?,
              var distance2parking: String?,
              var rimHeight: String?,
              var rimNumber: String?,
              var spotlight: String?,
              var fence: String?,
              var parking: String?,
              @field:SerializedName("basketnet")
              var basketNet: String?,
              var scoreline: String?,
              var lines: String?)