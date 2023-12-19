package com.example.socialbundle.DataClasses

data class Notification(
    var userid: String = "",
    var text: String = "",
    var postid: String = "",
    @field:JvmField
    var isPost: Boolean = false,
)
