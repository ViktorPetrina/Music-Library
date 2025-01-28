package hr.vpetrina.music.factory

import android.content.Context
import hr.vpetrina.music.dao.SongsSqlHelper

fun getSongsRepository(context: Context?) = SongsSqlHelper(context)