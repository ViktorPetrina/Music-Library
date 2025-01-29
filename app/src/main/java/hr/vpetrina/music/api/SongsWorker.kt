package hr.vpetrina.music.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SongsWorker(private val context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    override fun doWork(): Result {
        SongsFetcher(context).fetchItems(10, "happy")
        return Result.success()
    }
}