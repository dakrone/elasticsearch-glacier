package com.sonian.elasticsearch.rest.action.glacier;

import com.sonian.elasticsearch.action.glacier.GlacierFreezeRequest;
import com.sonian.elasticsearch.action.glacier.GlacierFreezeResponse;
import com.sonian.elasticsearch.action.glacier.TransportGlacierFreezeAction;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestXContentBuilder;

import java.io.IOException;

import static org.elasticsearch.common.unit.TimeValue.timeValueSeconds;
import static org.elasticsearch.rest.RestStatus.OK;
import static org.elasticsearch.rest.action.support.RestXContentBuilder.restContentBuilder;

public class RestGlacierFreezeAction extends BaseRestHandler {

    private final TransportGlacierFreezeAction freezeAction;

    @Inject
    public RestGlacierFreezeAction(Settings settings, Client client, RestController controller,
                                   TransportGlacierFreezeAction freezeAction) {
        super(settings, client);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/_freeze", this);
        this.freezeAction = freezeAction;
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel) {
        CloseIndexRequest closeIndexRequest = new CloseIndexRequest(request.param("index"));
        closeIndexRequest.listenerThreaded(false);
        closeIndexRequest.timeout(request.paramAsTime("timeout", timeValueSeconds(10)));
        client.admin().indices().close(closeIndexRequest, new ActionListener<CloseIndexResponse>() {
            @Override
            public void onResponse(CloseIndexResponse response) {
                // do nothing, on purpose
            }

            @Override
            public void onFailure(Throwable e) {
                try {
                    channel.sendResponse(new XContentThrowableRestResponse(request, e));
                } catch (IOException e1) {
                    logger.error("Failed to send failure response", e1);
                }
            }
        });


        GlacierFreezeRequest eqRequest = new GlacierFreezeRequest(request.param("index"));

        freezeAction.execute(eqRequest, new ActionListener<GlacierFreezeResponse>() {
            @Override
            public void onResponse(GlacierFreezeResponse freezeResp) {
                try {
                    XContentBuilder builder = restContentBuilder(request);

                    builder.startObject();
                    builder.field("success", freezeResp.acknowledged());
                    builder.endObject();

                    channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));

                } catch (IOException e) { }
            }

            @Override
            public void onFailure(Throwable e) {
                try {
                    channel.sendResponse(new XContentThrowableRestResponse(request, e));
                } catch (IOException e1) {
                    logger.error("Failed to send failure response", e1);
                }
            }
        });
    }
}
