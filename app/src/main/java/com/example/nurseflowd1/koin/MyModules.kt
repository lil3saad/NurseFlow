package com.example.nurseflowd1.koin

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nurseflowd1.datalayer.room.MedicineDao
import com.example.nurseflowd1.datalayer.room.NurseNoteDao
import com.example.nurseflowd1.datalayer.room.PatientCardDao
import com.example.nurseflowd1.datalayer.room.RoomDB
import com.example.nurseflowd1.domain.AppVM
import com.example.nurseflowd1.domain.usecases.AWStorageUseCase
import com.example.nurseflowd1.domain.usecases.NurseNoteUC
import com.example.nurseflowd1.domain.usecases.RoomMediUC
import com.example.nurseflowd1.domain.usecases.RoomPatientUC
import io.appwrite.Client
import io.appwrite.services.Storage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val roomModule = module {
    single<Context> { androidContext() }
    single<RoomDB> {
            Room.databaseBuilder( get() , RoomDB::class.java , "MyRoomDatabase").build()
    }
    single { get<RoomDB>().getpatientcardDAO() }
    single { get<RoomDB>().getmedicinedDAO() }
    single { get<RoomDB>().getnoteDAO() }
}

val usecasesModule = module {
           single <NurseNoteUC> {
               NurseNoteUC( get<NurseNoteDao>()  )
           }
           single <RoomMediUC> {
               RoomMediUC ( get<MedicineDao>() )
           }
           single <RoomPatientUC> {
               RoomPatientUC( get<PatientCardDao>() )
           }
           single <AWStorageUseCase> {
               AWStorageUseCase( get<Client>(), androidContext() )
           }
}
val viewmodelModule = module {
        // Koin Creates the View Model Factory by Default
        viewModel {
            AppVM( get() , get() , get() , get() )
        }
}
