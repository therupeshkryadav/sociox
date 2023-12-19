package com.example.socialbundle.DataClasses

data class AddPost(
    var post_image_url: String,
    var caption: String
) {
    constructor() : this("", "")
}

