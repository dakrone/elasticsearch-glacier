package com.sonian.elasticsearch.action.glacier;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

public class GlacierFreezeResponse extends ActionResponse {

    private boolean acknowledged;

    GlacierFreezeResponse() {
    }

    GlacierFreezeResponse(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public boolean acknowledged() {
        return acknowledged;
    }

    public boolean getAcknowledged() {
        return acknowledged();
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        acknowledged = in.readBoolean();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeBoolean(acknowledged);
    }
}
