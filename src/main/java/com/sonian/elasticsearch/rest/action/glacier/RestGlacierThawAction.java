package com.sonian.elasticsearch.rest.action.glacier;

import com.sonian.elasticsearch.action.glacier.GlacierThawRequest;
import com.sonian.elasticsearch.action.glacier.GlacierThawResponse;
import com.sonian.elasticsearch.action.glacier.TransportGlacierThawAction;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestXContentBuilder;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.unit.TimeValue.timeValueSeconds;
import static org.elasticsearch.rest.RestStatus.OK;
import static org.elasticsearch.rest.action.support.RestXContentBuilder.restContentBuilder;

public class RestGlacierThawAction extends BaseRestHandler {

    private final TransportGlacierThawAction thawAction;

    @Inject
    public RestGlacierThawAction(Settings settings, Client client, RestController controller,
                                 TransportGlacierThawAction thawAction) {
        super(settings, client);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/_thaw", this);
        this.thawAction = thawAction;
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel) {
        GlacierThawRequest eqRequest = new GlacierThawRequest(request.param("index"));
        final CountDownLatch latch = new CountDownLatch(1);
        boolean finished = false;

        thawAction.execute(eqRequest, new ActionListener<GlacierThawResponse>() {
            @Override
            public void onResponse(GlacierThawResponse thawResp) {
                // do nothing
                latch.countDown();
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

        try {
            finished = latch.await(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("Index freeze failed during thaw phase [" + request.param("index") + "]");
        }

        if (finished) {
            OpenIndexRequest openIndexRequest = new OpenIndexRequest(request.param("index"));
            openIndexRequest.listenerThreaded(false);
            openIndexRequest.timeout(request.paramAsTime("timeout", timeValueSeconds(10)));
            client.admin().indices().open(openIndexRequest, new ActionListener<OpenIndexResponse>() {
                @Override
                public void onResponse(OpenIndexResponse response) {
                    // do nothing, on purpose
                    try {
                        XContentBuilder builder = restContentBuilder(request);

                        builder.startObject();
                        builder.field("success", true);
                        builder.endObject();

                        channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));

                    } catch (IOException e) {
                    }

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
}
