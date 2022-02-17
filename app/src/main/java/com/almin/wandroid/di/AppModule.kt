package com.almin.wandroid.di

import com.almin.arch.middleware.ExceptionHandler
import com.almin.arch.middleware.MiddleWareProvider
import com.almin.arch.viewmodel.HolderViewModel
import com.almin.wandroid.data.repository.ArticleRepository
import com.almin.wandroid.data.repository.UserRepository
import com.almin.arch.middleware.ResourceProvider
import com.almin.wandroid.data.db.AppDataBase
import com.almin.wandroid.data.repository.ProjectRepository
import com.almin.wandroid.middleware.ApiExceptionHandler
import com.almin.wandroid.middleware.ResourceProviderImpl
import com.almin.wandroid.ui.AppViewModel
import com.almin.wandroid.ui.module.home.HomeViewModel
import com.almin.wandroid.ui.module.login.LoginViewModel
import com.almin.wandroid.ui.module.mine.MineTabViewModel
import com.almin.wandroid.ui.module.project.ProjectViewModel
import com.almin.wandroid.ui.module.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Almin on 2021/12/31.
 */
val appModule = module {

    single { AppDataBase.build(get()) }
    single { get<AppDataBase>().user }
    single { get<AppDataBase>().article }

    single<ExceptionHandler> { ApiExceptionHandler() }
    single<ResourceProvider>{ ResourceProviderImpl(get()) }
    single<MiddleWareProvider> { object : MiddleWareProvider{
        override fun resourceProvider(): ResourceProvider {
            return get()
        }

        override fun exceptionHandlerProvider(): ExceptionHandler {
            return get()
        }
    }}

    single { ArticleRepository(get(), get()) }
    single { UserRepository(get(), get()) }
    single { ProjectRepository(get()) }


    viewModel { AppViewModel(get(), get()) }
    viewModel { HolderViewModel() }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { ProjectViewModel(get()) }
    viewModel { MineTabViewModel(get()) }

}