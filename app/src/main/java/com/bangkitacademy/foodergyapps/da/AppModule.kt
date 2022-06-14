package com.bangkitacademy.foodergyapps.da

import android.content.Context
import androidx.room.Room
import com.bangkitacademy.foodergyapps.data.api.ApiService
import com.bangkitacademy.foodergyapps.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Provides
//    fun provideDatabase(@ApplicationContext  context: Context) : StoryDatabase {
//        return Room.databaseBuilder(
//            context,
//            StoryDatabase::class.java,
//            "Anjay"
//        ).build()
//
//    }

//    @Provides
//    fun providesStoryDao(database: StoryDatabase) = database.storyDao()

    @Provides
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        val api: ApiService by lazy { retrofit.create(ApiService::class.java) }
        return api
    }


}