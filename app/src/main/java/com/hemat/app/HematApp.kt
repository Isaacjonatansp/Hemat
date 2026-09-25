package com.hemat.app

import android.app.Application
import androidx.room.Room
import com.hemat.app.data.LanguageStore
import com.hemat.app.data.Repository
import com.hemat.app.data.local.HematDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HematApp : Application() {
    val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var db: HematDatabase
        private set
    lateinit var repo: Repository
        private set
    lateinit var lang: LanguageStore
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, HematDatabase::class.java, "hemat.db")
            .fallbackToDestructiveMigration()
            .build()
        repo = Repository(db)
        lang = LanguageStore(this)

        appScope.launch { repo.seedDefaults() }
    }
}
