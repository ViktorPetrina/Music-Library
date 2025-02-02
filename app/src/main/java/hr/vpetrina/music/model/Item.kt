package hr.vpetrina.music.model

data class Item(
    var _id: Long?,
    val title: String,
    val picturePath: String,
    val trackUrl: String,
    val artist: String,
    val album: String
) {
    val details: String
        get() = buildString {
            append("Title: $title\n\n")
            append("Album: $album\n\n")
            append("Artist: $artist")
        }
}