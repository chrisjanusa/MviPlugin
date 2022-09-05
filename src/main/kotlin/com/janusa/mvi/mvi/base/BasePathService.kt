package com.janusa.mvi.mvi.base

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "MviBasePath", storages = [Storage("MviBasePath.xml")])
class BasePathService : PersistentStateComponent<BasePathService> {
    var basePath: String = ""

    override fun getState() = this

    override fun loadState(newPath: BasePathService) {
        XmlSerializerUtil.copyBean(newPath, this)
    }

    companion object {
        fun getService(project: Project): BasePathService = project.getService(BasePathService::class.java)
    }
}