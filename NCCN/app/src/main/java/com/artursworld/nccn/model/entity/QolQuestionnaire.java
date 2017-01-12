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

    public double getGlobalHealthScore() {
        String binaryString1 = getBitsByQuestionNr(29).substring(0, 7);
        String binaryString2 = getBitsByQuestionNr(30).substring(0, 7);
        int b = getScoreByBits(binaryString1);
        int b2 = getScoreByBits(binaryString2);
        return getSymptomScore(6, b, b2);
    }

    /**
     * Measures a range of a score from 0 to 100. A high score represents a high / healthy
     * level of functioning
     */
    public double getPhysicalFunctioningScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(1));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(2));
        int itemNUmber3 = getScoreByBits(getBitsByQuestionNr(3));
        int itemNUmber4 = getScoreByBits(getBitsByQuestionNr(4));
        int itemNUmber5 = getScoreByBits(getBitsByQuestionNr(5));
        return getFunctionalScore(3, itemNumber1, itemNUmber2, itemNUmber3, itemNUmber4, itemNUmber5);
    }

    //TODO: not implemented yet
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

    /**
     * Calculates score only for functional scales. Measures a range of a score from 0 to 100.
     * A high score represents a high / healthy level of functioning
     * @param range the item range is the difference between possible maximum and the
     *              minimum response to individual items; most items take values from 1 to 4,
     *              giving the range = 3.
     * @param rawValues some raw values for calculation
     * @return the functional score
     */
    private double getFunctionalScore(int range, double... rawValues){
        return (1 - getMiddleScore(getRawScore(rawValues), range)) * 100;
    }

    /**
     * Calculates the mean by multiple values.
     * @param rawValues some raw values for calculation
     * @return the raw score (mean) of the given values
     */
    public double getRawScore(double... rawValues) {
        double sum = 0;
        for (double value : rawValues) {
            sum += value;
        }
        return sum / rawValues.length;
    }

    /**
     * Get the score by binary string
     * @param bits the given binary string
     * @return the score of the binary string
     */
    private int getScoreByBits(String bits ) {
        return new StringBuilder(bits).reverse().indexOf("1") + 1;
    }

}
