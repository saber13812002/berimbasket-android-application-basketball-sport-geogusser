package ir.berimbasket.app.data.network.model

import com.google.gson.annotations.SerializedName

class Stadium(@field:SerializedName("id")
              var id: Int,
              @field:SerializedName("title")
              var title: String?,
              @field:SerializedName("PlaygroundLatitude")
              var latitude: String?,
              @field:SerializedName("PlaygroundLongitude")
              var longitude: String?,
              @field:SerializedName("PlaygroundType")
              var type: String?,
              @field:SerializedName("ZoomLevel")
              var zoomLevel: Int?,
              @field:SerializedName("address")
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
              @field:SerializedName("roof")
              var roof: String?,
              @field:SerializedName("distance2parking")
              var distance2parking: String?,
              @field:SerializedName("rimHeight")
              var rimHeight: String?,
              @field:SerializedName("rimNumber")
              var rimNumber: String?,
              @field:SerializedName("spotlight")
              var spotlight: String?,
              @field:SerializedName("fence")
              var fence: String?,
              @field:SerializedName("parking")
              var parking: String?,
              @field:SerializedName("basketnet")
              var basketNet: String?,
              @field:SerializedName("scoreline")
              var scoreline: String?,
              @field:SerializedName("lines")
              var lines: String?)