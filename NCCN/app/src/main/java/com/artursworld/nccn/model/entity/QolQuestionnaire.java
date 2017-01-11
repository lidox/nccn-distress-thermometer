package com.artursworld.nccn.model.entity;

import android.util.Log;

import com.artursworld.nccn.controller.util.Bits;

import java.util.Date;

/**
 * Represents the Quality Of Life (Qol) Questionnaire
 */
public class QolQuestionnaire {

    private Date creationDate_PK;
    private String userNameId_FK;
    private Date updateDate;
    private byte[] answersToQuestionsBytes;
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

    // configuration
    private int questionByteCount = 26;
    private static String defaultByte = "00010001";
    private static String exceptionalByte = "00000001";



    public QolQuestionnaire(String userNameId){
        this.userNameId_FK = userNameId;
        this.creationDate_PK = new Date();
        this.updateDate = new Date();

        // init answer bytes
        answersToQuestionsBytes = new byte[questionByteCount];
        int index = 0;
        for(; index < 14 ; index++){
            answersToQuestionsBytes[index] = Bits.getByteByString(defaultByte)[0];
        }
        answersToQuestionsBytes[index++] = Bits.getByteByString(exceptionalByte)[0];
        answersToQuestionsBytes[index++] = Bits.getByteByString(exceptionalByte)[0];
        for(; index < 26 ; index++){
            answersToQuestionsBytes[index] = Bits.getByteByString(defaultByte)[0];
        }
    }

    public String getBitsByQuestionNr(int questionNr){
        if (validateQuestionNr(questionNr)) return null;
        StringBuilder result = new StringBuilder(Bits.getStringByByte(answersToQuestionsBytes));
        int[] startEndIndices = getStartEndIndexByQuestionNr(questionNr);
        return result.substring(startEndIndices[0], startEndIndices[1]);
    }

    /**
     * Sets new bits to the question by question number (first nr. 1 and last 50)
     * @param questionNr the question number
     * @param newBits the new bits to set
     */
    public void setBitsByQuestionNr(int questionNr, String newBits) {
        if (validateInput(questionNr, newBits)) return;

        // get current byte string
        StringBuilder result = new StringBuilder(Bits.getStringByByte(answersToQuestionsBytes));

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

        if(questionNr == 29 || questionNr == 30)
            if (newBits.length() != 8){
                Log.e(QolQuestionnaire.class.getSimpleName(), "setBitsByQuestionNr accepts only bits of length 8 if quesitonNr = 29 or 30");
                return true;
            }

        if(questionNr != 29 && questionNr !=30)
            if (newBits.length() !=4){
                Log.e(QolQuestionnaire.class.getSimpleName(), "setBitsByQuestionNr accepts only bits of length 4 if quesitonNr != 29 or 30");
                return true;
            }
        return false;
    }

    /**
     * Accepts only question number between 1 and 50
     * @param questionNr the question number to validate
     * @return True if validation fails, otherwise false
     */
    private boolean validateQuestionNr(int questionNr) {
        if(questionNr < 1 || questionNr > 50){
            Log.e(QolQuestionnaire.class.getSimpleName(), "getBitsByQuestionNr accepts only numbers between [1 - 50]");
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
        if(questionNr < 29){
            questionNr = questionNr -1;
            beginningIndex = (questionNr * 4);
            endingIndex = beginningIndex + 4;
        }
        else if( questionNr == 29){
            beginningIndex = (28 * 4);
            endingIndex = beginningIndex + 8;
        }
        else if( questionNr == 30){
            beginningIndex = (28 * 4) + 8;
            endingIndex = beginningIndex + 8;
        }
        else if(questionNr > 30){
            questionNr = questionNr -1;
            int restIndex = ((questionNr - 28) * 4) -  8;
            beginningIndex = (28 * 4) + (2 * 8) + restIndex;
            endingIndex = beginningIndex + 4;
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

    //TODO: not implemented yet
    public double getGlobalHealthScore() {
        int b = getScoreByBits(getBitsByQuestionNr(29).substring(0,7));
        int b2 = getScoreByBits(getBitsByQuestionNr(30).substring(0,7));
        return getSymptomScore(6, 8);
    }

    private int getScoreByBits(String bits ) {
        int score = 0;
        StringBuilder answerBits = new StringBuilder(bits);
            // boolean hasNotToReverse = i == 0 || i == 1 || i == 2 || i == 5 || i == 6 || i == 9 || i == 10 || i == 11;
            // if(!hasNotToReverse)
        answerBits = answerBits.reverse();
        score = answerBits.indexOf("1");

        return score;
    }

    public double getPhysicalFunctioningScore() {
        return -1;
    }

    public double getRoleFunctioningScore() {
        return -1;
    }

    public double getEmotionalFunctioningScore() {
        return -1;
    }

    public double getCognitiveFunctioningScore() {
        return -1;
    }

    public double getSocialFunctioningScore() {
        return -1;
    }


    public double getFatigueScore() {

        return -1;
    }

    public double getNauseaAndVomitingScore() {
        return -1;
    }

    public double getDyspnoeaScore() {
        return -1;
    }

    public double getInsomniaScore() {
        return -1;
    }

    public double getAppetiteLossScore() {
        return -1;
    }

    public double getConstipationScore() {
        return -1;
    }

    public double getDiarrhoeaScore() {
        return -1;
    }

    public double getFinancialDifficultiesScore() {
        return -1;
    }

    public double getPainScore() {
        return -1;
    }

    private double getMiddleScore(double rawScore, double range){
        return (rawScore - 1) / range;
    }

    private double getSymptomScore(int range, double... rawValues){
        return getMiddleScore(getRawScore(rawValues), range) * 100;
    }

    private double getFunctionalScore(int range, double... rawValues){
        return (1 - getMiddleScore(getRawScore(rawValues), range)) * 100;
    }

    public double getRawScore(double... rawValues) {
        double sum = 0;
        for (double value : rawValues) {
            sum += value;
        }
        return sum / rawValues.length;
    }
}
