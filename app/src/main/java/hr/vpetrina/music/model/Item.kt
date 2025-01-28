package hr.vpetrina.music.model

data class Item(
    var _id: Long?,
    val title: String,
    val picturePath: String,
)

data class Song(
    var _id: Long?,
    val title: String,
    val picturePath: String
)
