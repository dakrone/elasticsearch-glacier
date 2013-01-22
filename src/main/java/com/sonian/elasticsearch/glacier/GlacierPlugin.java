package com.sonian.elasticsearch.glacier;

import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.AbstractPlugin;

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

}
