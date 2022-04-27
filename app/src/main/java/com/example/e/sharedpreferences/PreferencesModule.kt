package com.example.e.sharedpreferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {
    @Singleton
    @Provides
    @Named(SHARED_PREFERENCES)
    internal fun provideSharedPreferences(@ApplicationContext applicationContext: Context) =
        applicationContext.getSharedPreferences(
            SHARED_PREFERENCES_DEST,
            Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    @Named(ENCRYPTED_PREFERENCES)
    internal fun provideEncryptedSharedPreferences(@ApplicationContext applicationContext: Context) =
        EncryptedSharedPreferences.create(
            applicationContext,
            AUTH,
            MasterKey.Builder(applicationContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM

        )

    companion object {
        const val SHARED_PREFERENCES = "SHARED_PREFERENCES"
        const val ENCRYPTED_PREFERENCES = "ENCRYPTED_PREFERENCES"
        private const val SHARED_PREFERENCES_DEST = "SP"
        private const val AUTH = "AUTH"
    }
}
