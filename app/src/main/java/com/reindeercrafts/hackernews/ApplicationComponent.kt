package com.reindeercrafts.hackernews

import com.reindeercrafts.hackernews.modules.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(articleActivity: ArticleActivity)
}