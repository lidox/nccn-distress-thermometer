package com.artursworld.nccn.model.entity;


import android.support.annotation.NonNull;
import android.util.Log;
import android.util.StringBuilderPrinter;

import com.artursworld.nccn.controller.elasticsearch.ElasticQuestionnaire;
import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.controller.util.Security;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;

import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.artursworld.nccn.view.user.UserStartConfiguration.CLASS_NAME;

/**
 * HADS-D, Hospital Anxiety and Depression Scale
 */
public class HADSDQuestionnaire {

    private Date creationDate_PK;
    private String userNameId_FK;
    private Date updateDate;
    private List<byte[]> answerToQuestionList;
    private int questionCountOfHADSDQuestionnaire = 14;
    private byte[] defaultByte = Bits.getByteByString("00000001");
    private int progressInPercent;
    private int lastQuestionEditedNr;

    public int getProgressInPercent() {
        return progressInPercent;
    }

    public void setProgressInPercent(int progressInPercent) {
        this.progressInPercent = progressInPercent;
    }

    public int getLastQuestionEditedNr() {
        return lastQuestionEditedNr;
    }

    public void setLastQuestionEditedNr(int lastQuestionEditedNr) {
        this.lastQuestionEditedNr = lastQuestionEditedNr;
    }

    public HADSDQuestionnaire(String userNameId){
        this.userNameId_FK = userNameId;
        this.creationDate_PK = new Date();
        this.updateDate = new Date();

        answerToQuestionList = new ArrayList<>();
        for(int i = 0; i < questionCountOfHADSDQuestionnaire; i++){
            answerToQuestionList.add(defaultByte);
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
        this.setUpdateDate(new Date());
    }

    public String getCreationTimeStamp(){
        try {
            return EntityDbManager.dateFormat.format(this.getCreationDate_PK());
        }
        catch (Exception e){
            Log.e(CLASS_NAME, "could not parse date("+this.getCreationDate_PK()+")=" + e.getLocalizedMessage());
        }
        return null;
    }

    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append(HADSDQuestionnaire.class.getSimpleName());
        ret.append(" created on " + getCreationTimeStamp());
        int index = 1;
        for (byte[] item: answerToQuestionList){
            ret.append(", Answerbyte" +index +" = " + Bits.getStringByByte(item));
            index++;
        }
        return ret.toString();
    }

    public byte[] getDefaultByte(){
        return defaultByte;
    }

    public int getDepressionScore() {
        int startIndex = 7;
        int endIndex = 14;
        return getScoreByIndexRange(startIndex, endIndex);
    }

    public int getAnxietyScore() {
        int startIndex = 0;
        int endIndex = 7;
        return getScoreByIndexRange(startIndex, endIndex);
    }

    private int getScoreByIndexRange(int startIndex, int endIndex) {
        int score = 0;
        for(int i = startIndex; i< endIndex; i++){
            StringBuilder answerBits = new StringBuilder(Bits.getStringByByte(getAnswerByNr(i)).substring(4));

            boolean hasNotToReverse = i == 0 || i == 1 || i == 2 || i == 5 || i == 6 || i == 9 || i == 10 || i == 11;
            if(!hasNotToReverse)
                answerBits = answerBits.reverse();

            score += answerBits.indexOf("1");
        }
        return score;
    }

    public boolean hasDepression(){
        return (getDepressionScore() >= 11) ? true : false;
    }

    public boolean hasAnxiety(){
        return (getAnxietyScore() >= 11) ? true : false;
    }


    public int getQuestionCount() {
        return questionCountOfHADSDQuestionnaire;
    }

    public JSONObject getAsJSON() {
        JSONObject params = new JSONObject();
        try {
            String prefix = "HADS-D-";
            params.put(prefix+"anxiety-score", getAnxietyScore());
            params.put(prefix+"depression-score", getDepressionScore());
            params.put(prefix+"has-depression", hasDepression());
            params.put(prefix+"has-anxiety", hasAnxiety());
            params.put(prefix+"update-date", EntityDbManager.dateFormat.format(getUpdateDate()));

        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        } finally {
            return params;
        }
    }
    /**
     * Get the bulk for HADS-D, Hospital Anxiety and Depression Scale
     * @return a String containing upsert information
     */
    public String getBulk() {
        return ElasticQuestionnaire.getGenericBulk(getCreationDate_PK(), ElasticQuestionnaire.ES_TYPE, getAsJSON().toString());
    }
}
