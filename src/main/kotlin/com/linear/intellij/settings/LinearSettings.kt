package com.linear.intellij.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@Service
@State(
    name = "com.linear.intellij.settings.LinearSettings",
    storages = [Storage("LinearSettings.xml")]
)
class LinearSettings : PersistentStateComponent<LinearSettings> {
    var apiKey: String = ""
    
    companion object {
        fun getInstance(): LinearSettings {
            return ApplicationManager.getApplication().getService(LinearSettings::class.java)
        }
    }
    
    override fun getState(): LinearSettings? {
        return this
    }
    
    override fun loadState(state: LinearSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
}