package com.bltech.moxtel.global.di

import android.content.Context
import androidx.room.Room
import com.bltech.moxtel.features.data.datasource.local.MovieDB
import com.bltech.moxtel.features.data.datasource.local.MovieDao
import com.bltech.moxtel.features.data.datasource.remote.MoxtelGitHubService
import com.bltech.moxtel.features.data.repository.MoviesRepository
import com.bltech.moxtel.features.domain.contract.IMovieRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MoxtelModule {
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
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

    private fun getGitHubRetrofit(baseUrl: String, client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun providesGitHubService(client: OkHttpClient, gson: Gson): MoxtelGitHubService {
        return getGitHubRetrofit("https://raw.githubusercontent.com/", client, gson)
            .create(MoxtelGitHubService::class.java)
    }

    @Provides
    fun providesGalleryRepository(repository: MoviesRepository): IMovieRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): MovieDB {
        return Room.databaseBuilder(
            appContext,
            MovieDB::class.java,
            "moxtel.db"
        ).build()
    }

    @Provides
    fun providesMovieDao(appDatabase: MovieDB): MovieDao {
        return appDatabase.movieDao()
    }

}
