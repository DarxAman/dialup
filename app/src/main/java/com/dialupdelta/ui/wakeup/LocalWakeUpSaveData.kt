package com.dialupdelta.ui.wakeup

data class LocalWakeUpSaveData(
    var gender:Int? = 1,
    var program: Int? = 1,
    var thumbUrl:String? = "default value",
    var videoUrl:String? = "default value",
    var time:String? = "default value",
    var repeatDays:String? = "default value"
)