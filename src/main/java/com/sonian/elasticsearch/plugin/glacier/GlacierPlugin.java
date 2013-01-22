package com.sonian.elasticsearch.plugin.glacier;

import com.sonian.elasticsearch.rest.action.glacier.RestGlacierFreezeAction;
import com.sonian.elasticsearch.rest.action.glacier.RestGlacierThawAction;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.AbstractPlugin;
import org.elasticsearch.rest.RestModule;

import java.util.Collection;

/**
 * @author dakrone
 */
public class GlacierPlugin extends AbstractPlugin {
    private final Settings settings;

    public GlacierPlugin(Settings settings) {
        this.settings = settings;
    }

    @Override
    public String name() {
        return "glacier";
    }

    @Override
    public String description() {
        return "Glacier.. For ES.";
    }

    @Override public Settings additionalSettings() {
        return super.additionalSettings();
    }

    @Override
    public Collection<Class<? extends Module>> modules() {
        return ImmutableList.<Class<? extends Module>>of(GlacierModule.class);
    }

    @Override
    public void processModule(Module module) {
        if (module instanceof RestModule) {
            ((RestModule) module).addRestAction(RestGlacierFreezeAction.class);
            ((RestModule) module).addRestAction(RestGlacierThawAction.class);
        }
    }
}
