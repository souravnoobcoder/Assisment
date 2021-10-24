package com.example.assisment.di

import android.content.Context
import androidx.room.Room
import com.example.assisment.api.YoutubeApi
import com.example.assisment.room.VideoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object YoutubeModule {

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(YoutubeApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApi(
        retrofit : Retrofit
    ) : YoutubeApi =
        retrofit.create(YoutubeApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
       @ApplicationContext context : Context
    ) =
        Room.databaseBuilder(
            context.applicationContext,
            VideoDatabase::class.java,
            "VideosDatabase"
        ).build()
}