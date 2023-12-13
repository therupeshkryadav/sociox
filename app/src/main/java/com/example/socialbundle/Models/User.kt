package com.example.socialbundle.Models

class User {
    var username : String ? = null
    var name : String ? = null
    var bio : String ? = null
    var emailId : String ? = null
    var imageurl : String ? = null

    constructor()

    constructor(
        username: String?,
        name: String?,
        bio: String?,
        emailId: String?,
        imageurl: String?
    ) {
        this.username = username
        this.name = name
        this.emailId = emailId
        this.imageurl = imageurl
        this.bio = bio
    }
}