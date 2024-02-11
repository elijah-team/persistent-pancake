package tripleo.elijah_pancake

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// TODO am i suppose3d to be using assertj??
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

import java.util.ArrayList

//import okhttp3.internal.wait

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

class KotlinCoroutineTest {
    @Test
    fun aa() {
//        main()
        runBlocking {
            val aggregate = Channel<String>()
            launch { fetchYoutubeVideos(aggregate) }
            launch { fetchTweets(aggregate) }

            val l = ArrayList<String>()

            repeat(4) {
                val element = aggregate.receive()
                l.add(element)
            }

//            coroutineContext.wait()

            coroutineContext.cancelChildren()

            assertThat(l).containsExactly(
                    "cat video",
                    "tweet: Earth is round",
                    "food video",
                    "tweet: Coroutines and channels are cool"
            )

        }
    }
}
