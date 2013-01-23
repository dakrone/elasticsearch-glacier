package com.sonian.elasticsearch.plugin.glacier;

import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;

/**
 * @author dakrone
 */
public class GlacierHandler extends AbstractComponent {
    private final boolean enabled = true;

    @Inject
    public GlacierHandler(Settings settings) {
        super(settings);
    }

    public void initialize() {
        logger.info("Initializing elasticsearch-glacier...");
        logger.info("Glacier enabled: {}", this.enabled);
    }
}
