package com.sonian.elasticsearch.plugin.glacier;

import clojure.lang.Keyword;
import clojure.lang.RT;
import clojure.lang.Symbol;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;

import java.util.Random;

/**
 * @author dakrone
 */
public class GlacierHandler extends AbstractComponent {
    private final boolean enabled = true;

    private final Keyword portKW;

    @Inject
    public GlacierHandler(Settings settings) {
        super(settings);
        this.portKW = (Keyword) RT.var("clojure.core", "keyword").invoke("port");
    }

    public void initialize() {
        Random r = new Random(System.currentTimeMillis());
        int portNum = r.nextInt(1000) + 9400;
        logger.info("startup Glacier {} {}", this.enabled);
        logger.info("starting Glacier on {}", portNum);

        RT.var("clojure.core", "require").invoke(Symbol.intern("glacier.core"));
        RT.var("glacier.core", "foo").invoke(portNum);
    }
}
