package com.linear.intellij.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service
@State(
    name = "com.linear.intellij.settings.LinearSettings",
    storages = @Storage("LinearSettings.xml")
)
public final class LinearSettings implements PersistentStateComponent<LinearSettings> {
    private String apiKey = "";
    
    public static LinearSettings getInstance() {
        return ApplicationManager.getApplication().getService(LinearSettings.class);
    }
    
    @Nullable
    @Override
    public LinearSettings getState() {
        return this;
    }
    
    @Override
    public void loadState(@NotNull LinearSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}