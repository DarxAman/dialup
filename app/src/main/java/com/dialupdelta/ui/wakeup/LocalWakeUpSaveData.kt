package com.dialupdelta.ui.wakeup

data class LocalWakeUpSaveData(
    var gender:Int? = 1,
    var program: Int? = 1,
    var thumbUrl:String? = "",
    var videoUrl:String? = "",
    var time:String? = "8:00",
    var repeatDays:String? = "never"
)