package tripleo.elijah_pancake

class a {
}

suspend fun fetchYoutubeVideos(channel: SendChannel<String>) {
    val videos = listOf("cat video", "food video")
    for (video in videos) {
        delay(100)
        channel.send(video)
    }
}

suspend fun fetchTweets(channel: SendChannel<String>) {
    val tweets = listOf("tweet: Earth is round", "tweet: Coroutines and channels are cool")
    for (tweet in tweets) {
        delay(100)
        channel.send(tweet)
    }
}

fun main() = runBlocking {
    val aggregate = Channel<String>()
    launch { fetchYoutubeVideos(aggregate) }
    launch { fetchTweets(aggregate) }

    repeat(4) {
        println(aggregate.receive())
    }

    coroutineContext.cancelChildren()
}

