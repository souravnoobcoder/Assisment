package com.example.assisment

import android.app.Application
import android.content.Context

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        if (appInstance==null){
            appInstance= this
        }
    }
    companion object{
        private var appInstance: App? =null
        fun getAppInstance() = appInstance
    }
}