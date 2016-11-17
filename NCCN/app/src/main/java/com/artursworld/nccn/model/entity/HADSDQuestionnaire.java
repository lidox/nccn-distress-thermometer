package com.artursworld.nccn.model.entity;


import android.util.Log;

import java.math.BigInteger;
import java.util.Date;

public class HADSDQuestionnaire {

    private Date creationDate_PK;
    private String userNameId_FK;
    private Date updateDate;

    private byte[] ANSWER_TO_QUESTION1;
    private byte[] ANSWER_TO_QUESTION2;

    public HADSDQuestionnaire(String userNameId){
        this.userNameId_FK = userNameId;
        this.creationDate_PK = new Date();
        this.updateDate = new Date();
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

    public byte[] getANSWER_TO_QUESTION1() {
        return ANSWER_TO_QUESTION1;
    }

    public void setANSWER_TO_QUESTION1(byte[] ANSWER_TO_QUESTION1) {
        this.ANSWER_TO_QUESTION1 = ANSWER_TO_QUESTION1;
    }

    public byte[] getANSWER_TO_QUESTION2() {
        return ANSWER_TO_QUESTION2;
    }

    public void setANSWER_TO_QUESTION2(byte[] ANSWER_TO_QUESTION2) {
        this.ANSWER_TO_QUESTION2 = ANSWER_TO_QUESTION2;
    }

    public static byte[] getByteByString(String byteString){
        if(byteString.length() % 8 == 0){
            Log.e(HADSDQuestionnaire.class.getSimpleName(), "the byteString must be modolo 8 = 0");
            return null;
        }

        return new BigInteger(byteString, 2).toByteArray();
    }

    public static String getStringByByte(byte[] bytes){
        String ret  = "";
        for (byte b : bytes) {
            ret += Integer.toBinaryString(b & 255 | 256).substring(1);
        }
        return ret;
    }

}
