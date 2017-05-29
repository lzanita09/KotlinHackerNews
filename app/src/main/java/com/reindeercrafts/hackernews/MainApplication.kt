package com.reindeercrafts.hackernews

import android.app.Application
import com.reindeercrafts.hackernews.modules.ApplicationModule

/**
 * Main application class, used to provide [ApplicationComponent], which holds all dependencies of the app.
 */
class MainApplication : Application() {

    lateinit private var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

    /**
     * @return [ApplicationComponent] object that's going to be used in [MainActivity] and [ArticleActivity].
     */
    fun getComponent(): ApplicationComponent {
        return component
    }
}