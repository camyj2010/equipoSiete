package com.example.equipoSiete.di

import android.content.Context
import com.example.equipoSiete.data.InventoryDB
import com.example.equipoSiete.data.InventoryDao
import com.example.equipoSiete.utils.Constants.BASE_URL
import com.example.equipoSiete.webservice.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
object Module {

    @Singleton
    @Provides
    fun provideInventoryDB(@ApplicationContext context: Context):InventoryDB{
        return InventoryDB.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)

    }

    @Singleton
    @Provides
    fun provideDaoReto(inventoryDB:InventoryDB): InventoryDao {
        return inventoryDB.inventoryDao()
    }
    @Singleton
    @Provides
    fun provideFirestoreDB(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuthDB(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

}