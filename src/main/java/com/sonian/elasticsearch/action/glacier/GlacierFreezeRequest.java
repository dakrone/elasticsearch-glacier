package com.sonian.elasticsearch.action.glacier;

import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.master.MasterNodeOperationRequest;

public class GlacierFreezeRequest extends MasterNodeOperationRequest {

    private String index;

    public GlacierFreezeRequest(String index) {
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
