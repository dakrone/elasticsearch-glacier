package com.sonian.elasticsearch.action.glacier;

import com.sonian.elasticsearch.plugin.glacier.GlacierService;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.support.master.TransportMasterNodeOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

public class TransportGlacierThawAction extends TransportMasterNodeOperationAction<GlacierThawRequest, GlacierThawResponse> {

    private final GlacierService freezeService;

    @Inject
    protected TransportGlacierThawAction(Settings settings, TransportService transportService, ClusterService clusterService, ThreadPool threadPool, GlacierService freezeService) {
        super(settings, transportService, clusterService, threadPool);
        this.freezeService = freezeService;
    }

    @Override
    protected String transportAction() {
        return "/sonian/freeze";
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.CACHE;
    }

    @Override
    protected GlacierThawRequest newRequest() {
        return new GlacierThawRequest(null);
    }

    @Override
    protected GlacierThawResponse newResponse() {
        return new GlacierThawResponse();
    }

    @Override
    protected GlacierThawResponse masterOperation(GlacierThawRequest request, ClusterState state) throws ElasticSearchException {
        return new GlacierThawResponse(this.freezeService.thaw(request.getIndex()));
    }
}
