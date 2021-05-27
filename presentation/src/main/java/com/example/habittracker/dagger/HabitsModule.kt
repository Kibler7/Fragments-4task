package com.example.habittracker.dagger

import android.content.Context
import androidx.room.Room
import com.example.data.HabitMap
import com.example.data.HabitRepositoryImpl
import com.example.data.db.AppDataBase
import com.example.data.web.HabitService
import com.example.data.web.HabitTypeAdapter
import com.example.data.web.SearchRepository
import com.example.domain.HabitRepository
import com.example.domain.entities.Habit
import com.example.domain.useCases.*
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class HabitsModule {

    @Provides
    fun provideAddHabitUseCase(habitRepository: HabitRepository) : AddHabitUseCase{
        return AddHabitUseCase(habitRepository, Dispatchers.IO)
    }

    @Provides
    fun providePostHabitUseCase(habitRepository: HabitRepository) : PostHabitsUseCase{
        return PostHabitsUseCase(habitRepository, Dispatchers.IO)
    }

    @Provides
    fun provideUpdateHabitUseCase(habitRepository: HabitRepository) : UpdateHabitUseCase {
        return UpdateHabitUseCase(habitRepository, Dispatchers.IO)
    }

    @Provides
    fun provideDeleteHabitUseCase(habitRepository: HabitRepository) : DeleteHabitUseCase{
        return DeleteHabitUseCase(habitRepository, Dispatchers.IO)
    }


    @Provides
    fun provideGetHabitsUseCase(habitRepository: HabitRepository) : GetHabitsUseCase {
        return GetHabitsUseCase(habitRepository, Dispatchers.IO)
    }

    @Provides
    fun provideHabitRepository(dataBase: AppDataBase,
                               searchRepository: SearchRepository) : HabitRepository{
        return HabitRepositoryImpl(dataBase, searchRepository)
    }

    @Provides
    fun provideSearchRepository(api : HabitService): SearchRepository{
        return SearchRepository(api)
    }

    @Provides
    fun provideHabitService(retrofit: Retrofit) : HabitService{
        return retrofit.create(HabitService::class.java)
    }

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient().newBuilder()
            .build()

        val gSon = GsonBuilder().registerTypeAdapter(HabitMap::class.java, HabitTypeAdapter()).create()

        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .baseUrl("https://droid-test-server.doubletapp.ru/").build()
    }

    @Singleton
    @Provides
    fun provideDataBase(context: Context): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java, "database"
        ).build()
    }
}