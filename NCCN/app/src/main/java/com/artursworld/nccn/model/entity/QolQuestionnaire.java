package com.artursworld.nccn.model.entity;

import com.artursworld.nccn.controller.util.Bits;

import java.util.Date;
import java.util.Map;

/**
 * Represents the Quality Of Life (Qol) Questionnaire
 */
public class QolQuestionnaire {

    private Date creationDate_PK;
    private String userNameId_FK;
    private Date updateDate;
    private byte[] answersToQuestionsBytes;

    // configuration
    private int questionCount = 50;
    public static String defaultByte = "00010001";
    public static String exceptionalByte = "00000001";

    public QolQuestionnaire(String userNameId){
        this.userNameId_FK = userNameId;
        this.creationDate_PK = new Date();
        this.updateDate = new Date();

        // init answer bytes
        answersToQuestionsBytes = new byte[questionCount];
        for(int i = 0; i < questionCount; i++){
            if(i == 28 || i == 29) // question 29 and 30 are different
                answersToQuestionsBytes[i] = Bits.getByteByString(exceptionalByte)[0];
            else
                answersToQuestionsBytes[i] = Bits.getByteByString(defaultByte)[0];
        }
    }

    //TODO: get Bits by question Nr. and set Bits by quest Nr.
    public String getBitsByQuestionNr(int questionNr){
        StringBuilder result = new StringBuilder(Bits.getStringByByte(answersToQuestionsBytes));
        int byteStartIndex = 0;
        int byteEndIndex = 0;
        if(questionNr < 29){
            byteStartIndex = (questionNr * 4) - 1;
        }
        else if( questionNr == 29){
            byteStartIndex = (28 * 4) -1;
        }

        //Entry<String,Integer> e = new Entry<String, Integer>("w",3);
        /*
        for(int i = 0; i < answersToQuestionsBytes.length; i++){
            if(i == 28 || i == 29) // question 29 and 30 are different
                result = Bits.getStringByByte(answersToQuestionsBytes);
            else
                result = answersToQuestionsBytes[i] = defaultByte[0];
        }
        */
        return result.substring(byteStartIndex, byteEndIndex);
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

    public byte[] getAnswersToQuestionsBytes() {
        return answersToQuestionsBytes;
    }

    public String getAnswersToQuestionsAsString(){
        return Bits.getStringByByte(this.answersToQuestionsBytes);
    }

    public void setAnswersToQuestionsBytes(byte[] answersToQuestionsBytes) {
        this.answersToQuestionsBytes = answersToQuestionsBytes;
    }
}
