package com.artursworld.nccn.model.entity;


import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.controller.elasticsearch.ElasticQuestionnaire;
import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.controller.util.Dates;
import com.artursworld.nccn.controller.util.Security;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class DistressThermometerQuestionnaire {

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
    private int questionCount = 6; // [10+5+2+6+2+21+(2)]/8= 6 bytes a 8 Bits
    private String CLASS_NAME = DistressThermometerQuestionnaire.class.getSimpleName();

    public DistressThermometerQuestionnaire(String userNameId) {
        this.userNameId_FK = userNameId;
        this.creationDate_PK = new Date();
        this.updateDate = new Date();

        // initialize default answer bytes
        answersToQuestionsBytes = new byte[questionCount];
        byte defaultByte = Bits.getByteByString("00000000")[0];
        answersToQuestionsBytes[0] = defaultByte;
        answersToQuestionsBytes[1] = Bits.getByteByString("00100000")[0];
        answersToQuestionsBytes[2] = defaultByte;
        answersToQuestionsBytes[3] = defaultByte;
        answersToQuestionsBytes[4] = defaultByte;
        answersToQuestionsBytes[5] = defaultByte;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Questionnaires(user = " + userNameId_FK + "\n");
        for (int i = 0; i < questionCount; i++) {
            sb.append("Answer to question " + (i + 1) + "(" + getBitsByQuestionNr(i + 1) + ")" + "\n");
        }
        sb.append("Progress = " + getProgressInPercent() + "%" + "\n");
        sb.append("MD5-HASH = " + getMD5Hash());
        return sb.toString();
    }

    /**
     * Get the MD5 Hash of the user
     *
     * @return the MD5 Hash of the user
     */
    public String getMD5Hash() {
        return Security.getMD5ByString(getUserNameId_FK() + getCreationDate_PK());
    }

    public String getBitsByQuestionNr(int questionNr) {
        if (validateQuestionNr(questionNr)) return null;
        StringBuilder currentBinaryAnswerString = new StringBuilder(Bits.getStringByByte(answersToQuestionsBytes));
        int[] startEndIndices = getStartEndIndexByQuestionNr(questionNr);
        return currentBinaryAnswerString.substring(startEndIndices[0], startEndIndices[1]);
    }

    /**
     * Sets new bits to the question by question number (first nr. 1)
     *
     * @param questionNr the question number
     * @param newBits    the new bits to set
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

        if (isValidLength(questionNr, newBits, 1, 11))
            return true;

        else if (isValidLength(questionNr, newBits, 2, 5))
            return true;

        else if (isValidLength(questionNr, newBits, 3, 2))
            return true;

        else if (isValidLength(questionNr, newBits, 4, 6))
            return true;

        else if (isValidLength(questionNr, newBits, 5, 2))
            return true;

        else if (isValidLength(questionNr, newBits, 6, 21))
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
     *
     * @param questionNr the question number to validate
     * @return True if validation fails, otherwise false
     */
    private boolean validateQuestionNr(int questionNr) {
        if (questionNr < 1 || questionNr > 6) {
            Log.e(CLASS_NAME, "validateQuestionNr accepts only numbers between [1 - 6]");
            return true;
        }
        return false;
    }

    /**
     * Calculates the beginning index, inclusive and the ending index, exclusive
     * of the question by question number
     *
     * @param questionNr the question number
     * @return an array containing the beginning and ending index
     */
    private int[] getStartEndIndexByQuestionNr(int questionNr) {
        int[] indexStartEnd = new int[2];
        int beginningIndex = 0;
        int endingIndex = 0;

        if (questionNr == 1) {
            beginningIndex = 0;
            endingIndex = 11;
        } else if (questionNr == 2) {
            beginningIndex = 11;
            endingIndex = 16;
        } else if (questionNr == 3) {
            beginningIndex = 16;
            endingIndex = 18;
        } else if (questionNr == 4) {
            beginningIndex = 18;
            endingIndex = 24;
        } else if (questionNr == 5) {
            beginningIndex = 24;
            endingIndex = 26;
        } else if (questionNr == 6) {
            beginningIndex = 26;
            endingIndex = 47;
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

    public String getAnswersToQuestionsAsString() {
        return Bits.getStringByByte(this.answersToQuestionsBytes);
    }

    public void setAnswersToQuestionsBytes(byte[] answersToQuestionsBytes) {
        this.answersToQuestionsBytes = answersToQuestionsBytes;
    }

    public int getDistressScore() {
        StringBuilder bits = new StringBuilder(getBitsByQuestionNr(1));
        int score = bits.reverse().indexOf("1");
        return score;
    }

    public boolean hasDistress() {
        boolean scoreValueTooHigh = getDistressScore() >= 6;
        return scoreValueTooHigh ? true : false;
    }

    public JSONObject getAsJSON() {
        JSONObject params = new JSONObject();
        try {
            params.put("distress-score", getDistressScore());
            params.put("has-distress", hasDistress());
            params.put("creation-date", EntityDbManager.dateFormat.format(getCreationDate_PK()));
            params.put("update-date", EntityDbManager.dateFormat.format(getCreationDate_PK()));
            params.put("user-name", Security.getMD5ByString(getUserNameId_FK()));
            params.put("operation-type", getOperationType());
        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        } finally {
            return params;
        }
    }

    @NonNull
    //TODO getOperationType: pre, post op
    private String getOperationType() {
        return "unkown";
    }
    /**
     * Get the bulk for Distress Thermometer
     * @return a String containing upsert information
     */
    public String getBulk() {
        return ElasticQuestionnaire.getGenericBulk(getCreationDate_PK(), "distress-thermometer", getAsJSON().toString());
    }
}
