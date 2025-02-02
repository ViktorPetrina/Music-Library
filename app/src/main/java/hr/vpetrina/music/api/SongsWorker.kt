package hr.vpetrina.music.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

// ne koristim
class SongsWorker(private val context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    override fun doWork(): Result {
        SongsFetcher(context).fetchItems(1, "happy")
        return Result.success()
    }
}