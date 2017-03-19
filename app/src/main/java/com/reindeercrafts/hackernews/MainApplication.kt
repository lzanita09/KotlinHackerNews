package com.reindeercrafts.hackernews

import android.app.Application
import com.reindeercrafts.hackernews.modules.ApplicationModule

class MainApplication : Application() {

    lateinit private var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

    fun getComponent(): ApplicationComponent {
        return component
    }
}