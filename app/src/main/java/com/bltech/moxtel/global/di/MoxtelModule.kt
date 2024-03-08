package com.bltech.moxtel.global.di

import com.bltech.moxtel.utils.MoxtelDummyService
import com.bltech.moxtel.utils.MoxtelGitHubService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class MoxtelModule {
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    fun providesHTTPClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    private fun getGitHubRetrofit(baseUrl: String, client: OkHttpClient, gson: Gson): Retrofit{
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun providesGitHubService(client: OkHttpClient, gson: Gson): MoxtelGitHubService {
        return getGitHubRetrofit("https://github.com/", client, gson)
            .create(MoxtelGitHubService::class.java)
    }

    @Provides
    fun providesDummyService(client: OkHttpClient, gson: Gson): MoxtelDummyService {
        return getGitHubRetrofit("https://dummyapi.online/", client, gson)
            .create(MoxtelDummyService::class.java)
    }

}