package com.example.e.sharedpreferences

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {
    @Singleton
    @Provides
    internal fun provideSharedPreferences(@ApplicationContext applicationContext: Context) =
        applicationContext.getSharedPreferences(
            SHARED_PREFERENCES_DEST,
            Context.MODE_PRIVATE
        )

    companion object {
        private const val SHARED_PREFERENCES_DEST = "SP"
    }
}
