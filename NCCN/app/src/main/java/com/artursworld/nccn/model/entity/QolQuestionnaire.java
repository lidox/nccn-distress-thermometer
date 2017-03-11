package com.artursworld.nccn.model.entity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.controller.elasticsearch.ElasticQuestionnaire;
import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.controller.util.Dates;
import com.artursworld.nccn.controller.util.Security;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import com.google.common.collect.Iterables;

import org.json.JSONObject;

import java.util.Date;

/**
 * Represents the Quality Of Life (Qol) Questionnaire
 */
public class QolQuestionnaire  implements IQuestionnaire{

    private static String CLASS_NAME = QolQuestionnaire.class.getSimpleName();

    private Date creationDate_PK;
    private String userNameId_FK;
    private Date updateDate;
    private byte[] answersToQuestionsBytes;
    private int progressInPercent;
    private int lastQuestionEditedNr;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("QolQuestionnaire(user = " + userNameId_FK + "\n");
        sb.append("CreationDate = " + EntityDbManager.dateFormat.format(getCreationDate_PK())+ "\n");
        sb.append("Progress = " + getProgressInPercent() + "%" + "\n");
        return sb.toString();
    }

    @Override
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
        String itemNumber1 = getBitsByQuestionNr(29).substring(0, 7);
        String itemNUmber2 = getBitsByQuestionNr(30).substring(0, 7);
        int itemScore1 = getScoreByBits(itemNumber1);
        int itemScore2 = getScoreByBits(itemNUmber2);
        return getSymptomScore(6, itemScore1, itemScore2);
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

    public double getRoleFunctioningScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(6));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(7));
        return getFunctionalScore(3, itemNumber1, itemNUmber2);
    }

    public double getEmotionalFunctioningScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(21));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(22));
        int itemNUmber3 = getScoreByBits(getBitsByQuestionNr(23));
        int itemNUmber4 = getScoreByBits(getBitsByQuestionNr(24));
        return getFunctionalScore(3, itemNumber1, itemNUmber2, itemNUmber3, itemNUmber4);
    }

    public double getCognitiveFunctioningScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(20));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(25));
        return getFunctionalScore(3, itemNumber1, itemNUmber2);
    }

    public double getSocialFunctioningScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(26));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(27));
        return getFunctionalScore(3, itemNumber1, itemNUmber2);
    }


    public double getFatigueScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(10));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(12));
        int itemNUmber3 = getScoreByBits(getBitsByQuestionNr(18));
        return getSymptomScore(3, itemNumber1, itemNUmber2, itemNUmber3);
    }

    public double getNauseaAndVomitingScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(14));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(15));
        return getSymptomScore(3, itemNumber1, itemNUmber2);
    }

    public double getPainScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(9));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(19));
        return getSymptomScore(3, itemNumber1, itemNUmber2);
    }

    public double getDyspnoeaScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(8));
        return getSymptomScore(3, itemNumber1);
    }

    public double getInsomniaScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(11));
        return getSymptomScore(3, itemNumber1);
    }

    public double getAppetiteLossScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(13));
        return getSymptomScore(3, itemNumber1);
    }

    public double getConstipationScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(16));
        return getSymptomScore(3, itemNumber1);
    }

    public double getDiarrhoeaScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(17));
        return getSymptomScore(3, itemNumber1);
    }

    public double getFinancialDifficultiesScore() {
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(28));
        return getSymptomScore(3, itemNumber1);
    }

    private double getMiddleScore(double rawScore, double range){
        return (rawScore - 1) / range;
    }

    private double getSymptomScore(int range, double... rawValues){
        return Math.floor(getMiddleScore(getRawScore(rawValues), range) * 100);
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
        return Math.floor((1 - getMiddleScore(getRawScore(rawValues), range)) * 100);
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

    // Brain Cancer Module (BN20)
    public double getFutureUncertaintyScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(31));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(32));
        int itemNUmber3 = getScoreByBits(getBitsByQuestionNr(33));
        int itemNUmber4 = getScoreByBits(getBitsByQuestionNr(34));
        int itemNUmber5 = getScoreByBits(getBitsByQuestionNr(35));
        return getSymptomScore(3, itemNumber1, itemNUmber2, itemNUmber3, itemNUmber4, itemNUmber5);
    }

    public double getVisualDisorderScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(36));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(37));
        int itemNUmber3 = getScoreByBits(getBitsByQuestionNr(38));
        return getSymptomScore(3, itemNumber1, itemNUmber2, itemNUmber3);
    }

    public double getMotorDysfunctionScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(40));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(45));
        int itemNUmber3 = getScoreByBits(getBitsByQuestionNr(49));
        return getSymptomScore(3, itemNumber1, itemNUmber2, itemNUmber3);
    }

    public double getCommunicationDeficitScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(41));
        int itemNUmber2 = getScoreByBits(getBitsByQuestionNr(42));
        int itemNUmber3 = getScoreByBits(getBitsByQuestionNr(43));
        return getSymptomScore(3, itemNumber1, itemNUmber2, itemNUmber3);
    }

    public double getHeadachesScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(34));
        return getSymptomScore(3, itemNumber1);
    }

    public double getSeizuresScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(39));
        return getSymptomScore(3, itemNumber1);
    }

    public double getDrowsinessScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(44));
        return getSymptomScore(3, itemNumber1);
    }

    public double getHairLossScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(46));
        return getSymptomScore(3, itemNumber1);
    }

    public double getItchySkinScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(47));
        return getSymptomScore(3, itemNumber1);
    }

    public double getWeaknessOfLegsScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(48));
        return getSymptomScore(3, itemNumber1);
    }

    public double getBladderControlScore(){
        int itemNumber1 = getScoreByBits(getBitsByQuestionNr(50));
        return getSymptomScore(3, itemNumber1);
    }

    /**
     * Get brain cancer module values
     * @return brain cancer module values as JSON representation
     */
    public JSONObject getBN20AsJSON() {
        JSONObject params = new JSONObject();
        try {
            String prefix = "BN20-";
            params.put(prefix+"future-uncertainty", getFutureUncertaintyScore());
            params.put(prefix+"visual-disorder", getVisualDisorderScore());
            params.put(prefix+"motor-dysfunction", getMotorDysfunctionScore());
            params.put(prefix+"communication-deficit", getCommunicationDeficitScore());
            params.put(prefix+"headaches", getHeadachesScore());
            params.put(prefix+"seizures", getSeizuresScore());
            params.put(prefix+"drowsiness", getDrowsinessScore());
            params.put(prefix+"hair-loss", getHairLossScore());
            params.put(prefix+"itchy-skin", getItchySkinScore());
            params.put(prefix+"weakness-of-legs", getWeaknessOfLegsScore());
            params.put(prefix+"bladder-control", getBladderControlScore());
            params.put(prefix+"update-date", EntityDbManager.dateFormat.format(getCreationDate_PK()));
        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        } finally {
            return params;
        }
    }

    /**
     * Get QLQ-C30 values
     * @return QLQ-C30 values as JSON representation
     */
    public JSONObject getQLQC30AsJSON() {
        JSONObject params = new JSONObject();
        try {
            String prefix = "QLQC30-";
            params.put(prefix+"global-health-status", getGlobalHealthScore());
            params.put(prefix+"physical-functioning", getPhysicalFunctioningScore());
            params.put(prefix+"role-functioning", getRoleFunctioningScore());
            params.put(prefix+"emotional-functioning", getEmotionalFunctioningScore());
            params.put(prefix+"cognitive-functioning", getCognitiveFunctioningScore());
            params.put(prefix+"social-functioning", getSocialFunctioningScore());
            params.put(prefix+"fatigue", getFatigueScore());
            params.put(prefix+"nausea-and-vomiting", getNauseaAndVomitingScore());
            params.put(prefix+"pain", getPainScore());
            params.put(prefix+"dyspnoea", getDyspnoeaScore());
            params.put(prefix+"insomnia", getInsomniaScore());
            params.put(prefix+"appetite-loss", getAppetiteLossScore());
            params.put(prefix+"constipation", getConstipationScore());
            params.put(prefix+"diarrhoea", getDiarrhoeaScore());
            params.put(prefix+"financial-difficulties", getFinancialDifficultiesScore());
            params.put(prefix+"update-date", EntityDbManager.dateFormat.format(getUpdateDate()));
        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        } finally {
            return params;
        }
    }

    /**
     * Get the bulk for Quality of Life (QLQC30)
     * @return a String containing upsert information
     */
    public String getBulkQLQC30() {
        return ElasticQuestionnaire.getGenericBulk(getCreationDate_PK(), ElasticQuestionnaire.getType(), getQLQC30AsJSON().toString());
    }

    /**
     * Get the bulk for Brain Cancer Module (BN20)
     * @return a String containing upsert information
     */
    public String getBulkBN20() {
        return ElasticQuestionnaire.getGenericBulk(getCreationDate_PK(), ElasticQuestionnaire.getType(), getBN20AsJSON().toString());
    }

}
