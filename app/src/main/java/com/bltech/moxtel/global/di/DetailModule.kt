package com.bltech.moxtel.global.di

import com.bltech.moxtel.domain.usecase.FetchSimilarMoviesUseCase
import com.bltech.moxtel.domain.usecase.FetchTheMovieUseCase
import com.bltech.moxtel.domain.usecase.IFetchSimilarMoviesUseCase
import com.bltech.moxtel.domain.usecase.IFetchTheMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class DetailModule {
    @Provides
    @ActivityRetainedScoped
    fun providesFetchSimilarMoviesUseCase(useCase: FetchSimilarMoviesUseCase): IFetchSimilarMoviesUseCase {
        return useCase
    }

    @Provides
    @ActivityRetainedScoped
    fun providesFetchTheMovieUseCase(useCase: FetchTheMovieUseCase): IFetchTheMovieUseCase {
        return useCase
    }
}


