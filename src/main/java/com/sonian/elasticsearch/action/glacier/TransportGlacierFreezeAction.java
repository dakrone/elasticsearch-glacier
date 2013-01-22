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

public class TransportGlacierFreezeAction extends TransportMasterNodeOperationAction<GlacierFreezeRequest, GlacierFreezeResponse> {

    private final GlacierService freezeService;

    @Inject
    protected TransportGlacierFreezeAction(Settings settings, TransportService transportService, ClusterService clusterService, ThreadPool threadPool, GlacierService freezeService) {
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
    protected GlacierFreezeRequest newRequest() {
        return new GlacierFreezeRequest(null);
    }

    @Override
    protected GlacierFreezeResponse newResponse() {
        return new GlacierFreezeResponse();
    }

    @Override
    protected GlacierFreezeResponse masterOperation(GlacierFreezeRequest request, ClusterState state) throws ElasticSearchException {
        return new GlacierFreezeResponse(this.freezeService.freeze(request.getIndex()));
    }
}
