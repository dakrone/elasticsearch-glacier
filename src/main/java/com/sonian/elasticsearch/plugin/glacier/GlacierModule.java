package com.sonian.elasticsearch.plugin.glacier;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.settings.Settings;

/**
 * @author dakrone
 */
public class GlacierModule extends AbstractModule {


    private final Settings componentSettings;

    public GlacierModule(Settings settings) {
        this.componentSettings = settings.getComponentSettings(this.getClass());
    }

    @Override
    protected void configure() {
        bind(GlacierHandler.class).asEagerSingleton();

        GlacierHandler handler = new GlacierHandler(this.componentSettings);
        handler.initialize();
    }
}
