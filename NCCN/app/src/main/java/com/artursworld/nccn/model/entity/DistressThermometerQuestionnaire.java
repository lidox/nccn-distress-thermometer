package com.artursworld.nccn.model.entity;


import android.util.Log;

import com.artursworld.nccn.controller.util.Bits;

import java.util.Date;

public class DistressThermometerQuestionnaire {

    private Date creationDate_PK;
    private String userNameId_FK;
    private Date updateDate;
    private byte[] answersToQuestionsBytes;

    // configuration
    private int questionByteCount = 6; // [10+5+2+6+2+21+(2)]/8= 6 bytes a 8 Bits
    private String CLASS_NAME = DistressThermometerQuestionnaire.class.getSimpleName();

    public DistressThermometerQuestionnaire(String userNameId){
        this.userNameId_FK = userNameId;
        this.creationDate_PK = new Date();
        this.updateDate = new Date();

        // initialize default answer bytes
        answersToQuestionsBytes = new byte[questionByteCount];
        byte defaultByte = Bits.getByteByString("00000000")[0];
        answersToQuestionsBytes[0] = defaultByte;
        answersToQuestionsBytes[1] = Bits.getByteByString("1000000")[0];
        answersToQuestionsBytes[2] = defaultByte;
        answersToQuestionsBytes[3] = defaultByte;
        answersToQuestionsBytes[4] = defaultByte;
        answersToQuestionsBytes[5] = defaultByte;
    }

    public String getBitsByQuestionNr(int questionNr){
        if (validateQuestionNr(questionNr)) return null;
        StringBuilder currentBinaryAnswerString = new StringBuilder(Bits.getStringByByte(answersToQuestionsBytes));
        int[] startEndIndices = getStartEndIndexByQuestionNr(questionNr);
        return currentBinaryAnswerString.substring(startEndIndices[0], startEndIndices[1]);
    }

    /**
     * Sets new bits to the question by question number (first nr. 1 and last 50)
     * @param questionNr the question number
     * @param newBits the new bits to set
     */
    public void setBitsByQuestionNr(int questionNr, String newBits) {
        if (validateInput(questionNr, newBits)) return;

        // get current byte string
        String binaryString = Bits.getStringByByte(answersToQuestionsBytes);
        StringBuilder result = new StringBuilder(binaryString);

        // get indices of bits pattern to replace
        int[] startEndIndices = getStartEndIndexByQuestionNr(questionNr);

        // replace bit pattern
        result.replace(startEndIndices[0], startEndIndices[1], newBits);

        // set new results to real byte array using the string of bytes
        byte[] byteByString = Bits.getByteByString(result.toString());
        //String s = Bits.getStringByByte(byteByString);
        this.setAnswersToQuestionsBytes(byteByString);
    }

    private boolean validateInput(int questionNr, String newBits) {
        if (validateQuestionNr(questionNr)) return true;

        if (isValidLength(questionNr, newBits, 1, 10))
            return true;

        else if(isValidLength(questionNr, newBits, 2, 5))
            return true;

        else if(isValidLength(questionNr, newBits, 3, 2))
            return true;

        else if(isValidLength(questionNr, newBits, 4, 6))
            return true;

        else if(isValidLength(questionNr, newBits, 5, 2))
            return true;

        else if(isValidLength(questionNr, newBits, 6, 21))
            return true;

        return false;
    }

    private boolean isValidLength(int questionNr, String newBits, int selectedQuestionNr, int answerLengthToFullFill) {
        if (questionNr == selectedQuestionNr)
            if (newBits.length() != answerLengthToFullFill) {
                Log.e(CLASS_NAME, "questionNr " + questionNr + " accepts only bits of length " + answerLengthToFullFill);
                return true;
            }
        return false;
    }

    /**
     * Accepts only question number between 1 and 6
     * @param questionNr the question number to validate
     * @return True if validation fails, otherwise false
     */
    private boolean validateQuestionNr(int questionNr) {
        if(questionNr < 1 || questionNr > 6){
            Log.e(CLASS_NAME, "validateQuestionNr accepts only numbers between [1 - 6]");
            return true;
        }
        return false;
    }

    /**
     * Calculates the beginning index, inclusive and the ending index, exclusive
     * of the question by question number
     * @param questionNr the question number
     * @return an array containing the beginning and ending index
     */
    private int[] getStartEndIndexByQuestionNr(int questionNr) {
        int [] indexStartEnd = new int[2];
        int beginningIndex = 0;
        int endingIndex = 0;

        if(questionNr == 1){
            beginningIndex = 0;
            endingIndex = 10;
        }
        else if( questionNr == 2){
            beginningIndex = 10;
            endingIndex =  15;
        }
        else if( questionNr == 3){
            beginningIndex = 15;
            endingIndex = 17;
        }
        else if(questionNr == 4){
            beginningIndex = 17;
            endingIndex = 23;
        }
        else if( questionNr == 5){
            beginningIndex = 23;
            endingIndex =  25;
        }
        else if( questionNr == 6){
            beginningIndex = 25;
            endingIndex =  46;
        }
        indexStartEnd[0] = beginningIndex;
        indexStartEnd[1] = endingIndex;
        return indexStartEnd;
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
