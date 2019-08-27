package com.cdmp.weatherapp.di

object DiModuleBuilder {
    fun buildModules() = listOf(
        DataModules.remoteServiceModule,
        DataModules.apiModule,
        DataModules.repoModule,
        DomainModules.useCaseModules,
        PresentationModules.mainModule
    )

}


