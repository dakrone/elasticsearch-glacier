package com.sonian.elasticsearch.action.glacier;

import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.master.MasterNodeOperationRequest;

public class GlacierThawRequest extends MasterNodeOperationRequest {

    private String index;

    public GlacierThawRequest(String index) {
        this.index = index;
    }

    public String getIndex() {
        return this.index;
    }

    @Override
    public ActionRequestValidationException validate() {
        return null;
    }
}
