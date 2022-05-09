package com.bnyte.easyhd.core.pojo.hdfs;

import java.io.Serializable;

public class EmptyStateResult implements Serializable {

    private static final long serialVersionUID = 94265L;

    private Boolean mkdir;

    public EmptyStateResult() {
    }

    public EmptyStateResult(Boolean mkdir) {
        this.mkdir = mkdir;
    }

    public Boolean getMkdir() {
        return mkdir;
    }

    public void setMkdir(Boolean mkdir) {
        this.mkdir = mkdir;
    }
}
