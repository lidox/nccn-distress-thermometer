package com.artursworld.nccn.model.entity;

import android.util.Log;
import com.artursworld.nccn.controller.util.OperationType;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import java.util.Date;

public class MetaQuestionnaire {

    private Date updateDate = null;
    private Date creationDate = null;
    private Date operationDate = null;
    private OperationType operationType = null;
    private PsychoSocialSupportState psychoSocialSupportState = null;

    public MetaQuestionnaire(Date creationDate){
        if(creationDate == null){
            Log.e("", "Cannot creation meta data with creation date = null");
            return;
        }
        operationType = OperationType.PRE;
        this.creationDate = creationDate;
        updateDate = new Date();
        psychoSocialSupportState = PsychoSocialSupportState.NOT_ASKED;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
        this.updateDate = new Date();
    }

    public PsychoSocialSupportState getPsychoSocialSupportState() {
        return psychoSocialSupportState;
    }

    public void setPsychoSocialSupportState(PsychoSocialSupportState psychoSocialSupportState) {
        this.updateDate = new Date();
        this.psychoSocialSupportState = psychoSocialSupportState;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append("operationType: " + getOperationType().name() +"\n");
        if(getPsychoSocialSupportState() != null)
            ret.append("psychoSocialSupportState: " + getPsychoSocialSupportState().name() +"\n");
        if(operationDate!=null)
            ret.append("operationDate: "+EntityDbManager.dateFormat.format(operationDate) +"\n");
        if(updateDate!=null)
            ret.append("updateDate: "+EntityDbManager.dateFormat.format(updateDate) +"\n");
        if(creationDate!=null)
            ret.append("creationDate: "+EntityDbManager.dateFormat.format(creationDate) +"\n");
        return ret.toString();
    }

}
