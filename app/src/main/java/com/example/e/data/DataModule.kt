package com.example.e.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context) = Room
        .databaseBuilder(applicationContext, DataBase::class.java, "general_database")
        .build()

    @Singleton
    @Provides
    fun provideParticpantDao(db: DataBase) = db.getParticipantDao()

    @Singleton
    @Provides
    fun provideGroupDao(db: DataBase) = db.getGroupDao()

    @Singleton
    @Provides
    fun provideUserDao(db: DataBase) = db.getUserDao()

    @Singleton
    @Provides
    fun provideMemberDao(db: DataBase) = db.getMemberDao()

    @Singleton
    @Provides
    fun provideExpenseDao(db: DataBase) = db.getExpenseDao()
}
