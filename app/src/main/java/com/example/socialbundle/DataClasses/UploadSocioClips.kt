package com.example.socialbundle.DataClasses

data class UploadSocioClips(
    var uploaded_socio_clip_url: String,
    var profile_link:String,
    var caption: String
){
    constructor() : this("", "","")
}




