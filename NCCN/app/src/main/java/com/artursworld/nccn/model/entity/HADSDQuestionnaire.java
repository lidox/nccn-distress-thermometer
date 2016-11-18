package com.artursworld.nccn.model.entity;


import android.util.Log;
import android.util.StringBuilderPrinter;

import com.artursworld.nccn.controller.util.Bits;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HADSDQuestionnaire {

    private Date creationDate_PK;
    private String userNameId_FK;
    private Date updateDate;
    private List<byte[]> answerToQuestionList;
    private int questionCountOfHADSDQuestionnaire = 14;

    public HADSDQuestionnaire(String userNameId){
        this.userNameId_FK = userNameId;
        this.creationDate_PK = new Date();
        this.updateDate = new Date();

        answerToQuestionList = new ArrayList<>();
        for(int i = 0; i < questionCountOfHADSDQuestionnaire; i++){
            answerToQuestionList.add(new byte[3]);
        }
    }

    public Date getCreationDate_PK() {
        return creationDate_PK;
    }

    public void setCreationDate_PK(Date creationDate_PK) {
        this.creationDate_PK = creationDate_PK;
    }

    public String getUserNameId_FK() {
        return userNameId_FK;
    }

    public void setUserNameId_FK(String userNameId_FK) {
        this.userNameId_FK = userNameId_FK;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<byte[]> getAnswerToQuestionList() {
        return answerToQuestionList;
    }

    public void setAnswerToQuestionList(List<byte[]> answerToQuestionList) {
        this.answerToQuestionList = answerToQuestionList;
    }

    public void addAnswerToQuestion(byte[] answer){
        this.answerToQuestionList.add(answer);
    }

    /**
     * Get the answer by the question number
     * @param questionNr the question number
     * @return the answer as byte array
     */
    public byte[] getAnswerByNr(int questionNr){
        return this.getAnswerToQuestionList().get(questionNr);
    }

    /**
     * Sets the answer by the question number
     * @param questionNr the question number
     * @param newByte the byte to set
     */
    public void setAnswerByNr(int questionNr, byte[] newByte){
        this.getAnswerToQuestionList().set(questionNr, newByte);
    }
}
