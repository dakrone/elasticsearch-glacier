package com.sonian.elasticsearch.plugin.glacier;

import clojure.lang.RT;
import clojure.lang.Symbol;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.ClusterStateUpdateTask;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class GlacierService extends AbstractComponent {

    private final ClusterService clusterService;

    @Inject
    public GlacierService(Settings settings, ClusterService clusterService) {
        super(settings);
        this.clusterService = clusterService;
    }

    public boolean freeze(String index) {
        final AtomicReference<Throwable> failureRef = new AtomicReference<Throwable>();
        final CountDownLatch latch = new CountDownLatch(1);
        final String i = index;
        boolean finished = false;

        clusterService.submitStateUpdateTask("glacier-service",new ClusterStateUpdateTask() {
            @Override
            public ClusterState execute(ClusterState clusterState) {
                try {
                    logger.info("--- Freezing [" + i + "]");
                    // TODO: actually freeze
                    RT.var("clojure.core", "require").invoke(Symbol.intern("glacier.core"));
                    RT.var("glacier.core", "freeze").invoke(i);
                    return clusterState;
                } catch (Exception e) {
                    logger.warn("failed to swap shards", e);
                    failureRef.set(e);
                    return clusterState;
                } finally {
                    latch.countDown();
                }
            }
        });

        try {
            finished = latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            failureRef.set(e);
        }

        if (failureRef.get() != null) {
            if (failureRef.get() instanceof ElasticSearchException) {
                throw (ElasticSearchException) failureRef.get();
            } else {
                throw new ElasticSearchException(failureRef.get().getMessage(), failureRef.get());
            }
        }

        return finished;
    }

    public boolean thaw(String index) {
        final AtomicReference<Throwable> failureRef = new AtomicReference<Throwable>();
        final CountDownLatch latch = new CountDownLatch(1);
        final String i = index;
        boolean finished = false;

        clusterService.submitStateUpdateTask("glacier-service",new ClusterStateUpdateTask() {
            @Override
            public ClusterState execute(ClusterState clusterState) {
                try {
                    logger.info("+++ Thawing [" + i + "]");
                    // TODO: actually thaw
                    RT.var("clojure.core", "require").invoke(Symbol.intern("glacier.core"));
                    RT.var("glacier.core", "thaw").invoke(i);
                    return clusterState;
                } catch (Exception e) {
                    logger.warn("failed to swap shards", e);
                    failureRef.set(e);
                    return clusterState;
                } finally {
                    latch.countDown();
                }
            }
        });

        try {
            finished = latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            failureRef.set(e);
        }

        if (failureRef.get() != null) {
            if (failureRef.get() instanceof ElasticSearchException) {
                throw (ElasticSearchException) failureRef.get();
            } else {
                throw new ElasticSearchException(failureRef.get().getMessage(), failureRef.get());
            }
        }

        return finished;
    }
}
