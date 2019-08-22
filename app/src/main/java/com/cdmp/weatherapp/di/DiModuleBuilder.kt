package com.cdmp.weatherapp.di

object DiModuleBuilder {
    fun buildModules() = listOf(
        DataModules.remoteServiceModule,
        DataModules.apiModule,
        DomainModules.useCaseModules,
        PresentationModules.mainModule
    )

}


