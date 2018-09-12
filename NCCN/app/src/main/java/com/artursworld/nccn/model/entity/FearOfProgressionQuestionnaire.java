package com.artursworld.nccn.model.entity;

import android.util.Log;

import com.artursworld.nccn.model.persistence.manager.EntityDbManager;

import org.json.JSONObject;

import java.util.Date;

public class FearOfProgressionQuestionnaire implements IQuestionnaire {

    private static String CLASS_NAME = FearOfProgressionQuestionnaire.class.getSimpleName();

    private Date creationDate_PK;
    private String userNameId_FK;
    private Date updateDate;
    private int progressInPercent;
    private int lastQuestionEditedNr;

    // selected Answers
    private int selectedAnswerIndexQ1 = -1;
    private int selectedAnswerIndexQ2 = -1;
    private int selectedAnswerIndexQ3 = -1;
    private int selectedAnswerIndexQ4 = -1;
    private int selectedAnswerIndexQ5 = -1;
    private int selectedAnswerIndexQ6 = -1;
    private int selectedAnswerIndexQ7 = -1;
    private int selectedAnswerIndexQ8 = -1;
    private int selectedAnswerIndexQ9 = -1;
    private int selectedAnswerIndexQ10 = -1;
    private int selectedAnswerIndexQ11 = -1;
    private int selectedAnswerIndexQ12 = -1;

    public FearOfProgressionQuestionnaire(String userNameId) {
        this.userNameId_FK = userNameId;
        this.creationDate_PK = new Date();
        this.updateDate = new Date();
    }

    public String toString() {
        return CLASS_NAME + "(user = " + userNameId_FK + "\n" +
                "CreationDate = " + EntityDbManager.dateFormat.format(getCreationDate_PK()) + "\n" +
                "Progress = " + getProgressInPercent() + "%" + "\n";
    }

    /**
     * @param questionNr the question number (1-12) of the questionnaire
     * @return the selected answer index (1 - 5), -1 if not selected yet.
     */
    public int getSelectedAnswerIndexByQuestionNr(int questionNr) {

        switch (questionNr) {
            case 1:
                return selectedAnswerIndexQ1;
            case 2:
                return selectedAnswerIndexQ2;
            case 3:
                return selectedAnswerIndexQ3;
            case 4:
                return selectedAnswerIndexQ4;
            case 5:
                return selectedAnswerIndexQ5;
            case 6:
                return selectedAnswerIndexQ6;
            case 7:
                return selectedAnswerIndexQ7;
            case 8:
                return selectedAnswerIndexQ8;
            case 9:
                return selectedAnswerIndexQ9;
            case 10:
                return selectedAnswerIndexQ10;
            case 11:
                return selectedAnswerIndexQ11;
            case 12:
                return selectedAnswerIndexQ12;
            default:
                return -1;
        }
    }

    /**
     * Sets new answer index for a question by question number (first nr. 1 and last 12)
     *
     * @param questionNr the question number (1-12)
     * @param newIndex   the new index to set
     */
    public void setAnswerIndexByQuestionNr(int questionNr, int newIndex) {
        switch (questionNr) {
            case 1:
                selectedAnswerIndexQ1 = newIndex;
            case 2:
                selectedAnswerIndexQ2 = newIndex;
            case 3:
                selectedAnswerIndexQ3 = newIndex;
            case 4:
                selectedAnswerIndexQ4 = newIndex;
            case 5:
                selectedAnswerIndexQ5 = newIndex;
            case 6:
                selectedAnswerIndexQ6 = newIndex;
            case 7:
                selectedAnswerIndexQ7 = newIndex;
            case 8:
                selectedAnswerIndexQ8 = newIndex;
            case 9:
                selectedAnswerIndexQ9 = newIndex;
            case 10:
                selectedAnswerIndexQ10 = newIndex;
            case 11:
                selectedAnswerIndexQ11 = newIndex;
            case 12:
                selectedAnswerIndexQ12 = newIndex;
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

    public int getSelectedAnswerIndexQ1() {
        return selectedAnswerIndexQ1;
    }

    public void setSelectedAnswerIndexQ1(int selectedAnswerIndexQ1) {
        this.selectedAnswerIndexQ1 = selectedAnswerIndexQ1;
    }

    public int getSelectedAnswerIndexQ2() {
        return selectedAnswerIndexQ2;
    }

    public void setSelectedAnswerIndexQ2(int selectedAnswerIndexQ2) {
        this.selectedAnswerIndexQ2 = selectedAnswerIndexQ2;
    }

    public int getSelectedAnswerIndexQ3() {
        return selectedAnswerIndexQ3;
    }

    public void setSelectedAnswerIndexQ3(int selectedAnswerIndexQ3) {
        this.selectedAnswerIndexQ3 = selectedAnswerIndexQ3;
    }

    public int getSelectedAnswerIndexQ4() {
        return selectedAnswerIndexQ4;
    }

    public void setSelectedAnswerIndexQ4(int selectedAnswerIndexQ4) {
        this.selectedAnswerIndexQ4 = selectedAnswerIndexQ4;
    }

    public int getSelectedAnswerIndexQ5() {
        return selectedAnswerIndexQ5;
    }

    public void setSelectedAnswerIndexQ5(int selectedAnswerIndexQ5) {
        this.selectedAnswerIndexQ5 = selectedAnswerIndexQ5;
    }

    public int getSelectedAnswerIndexQ6() {
        return selectedAnswerIndexQ6;
    }

    public void setSelectedAnswerIndexQ6(int selectedAnswerIndexQ6) {
        this.selectedAnswerIndexQ6 = selectedAnswerIndexQ6;
    }

    public int getSelectedAnswerIndexQ7() {
        return selectedAnswerIndexQ7;
    }

    public void setSelectedAnswerIndexQ7(int selectedAnswerIndexQ7) {
        this.selectedAnswerIndexQ7 = selectedAnswerIndexQ7;
    }

    public int getSelectedAnswerIndexQ8() {
        return selectedAnswerIndexQ8;
    }

    public void setSelectedAnswerIndexQ8(int selectedAnswerIndexQ8) {
        this.selectedAnswerIndexQ8 = selectedAnswerIndexQ8;
    }

    public int getSelectedAnswerIndexQ9() {
        return selectedAnswerIndexQ9;
    }

    public void setSelectedAnswerIndexQ9(int selectedAnswerIndexQ9) {
        this.selectedAnswerIndexQ9 = selectedAnswerIndexQ9;
    }

    public int getSelectedAnswerIndexQ10() {
        return selectedAnswerIndexQ10;
    }

    public void setSelectedAnswerIndexQ10(int selectedAnswerIndexQ10) {
        this.selectedAnswerIndexQ10 = selectedAnswerIndexQ10;
    }

    public int getSelectedAnswerIndexQ11() {
        return selectedAnswerIndexQ11;
    }

    public void setSelectedAnswerIndexQ11(int selectedAnswerIndexQ11) {
        this.selectedAnswerIndexQ11 = selectedAnswerIndexQ11;
    }

    public int getSelectedAnswerIndexQ12() {
        return selectedAnswerIndexQ12;
    }

    public void setSelectedAnswerIndexQ12(int selectedAnswerIndexQ12) {
        this.selectedAnswerIndexQ12 = selectedAnswerIndexQ12;
    }

    /**
     * Get Fear of Progression in Partners values
     *
     * @return Fear of Progression in Partners values as JSON representation
     */
    public JSONObject getAsJSON() {
        JSONObject params = new JSONObject();
        try {
            String prefix = "FOP-";
            params.put(prefix + "score", getScore());
            params.put(prefix + "update-date", EntityDbManager.dateFormat.format(getUpdateDate()));
        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        } finally {
            return params;
        }
    }

    public double getScore() {

        return selectedAnswerIndexQ1 + selectedAnswerIndexQ2 + selectedAnswerIndexQ3 + selectedAnswerIndexQ4
                     + selectedAnswerIndexQ5 + selectedAnswerIndexQ6 + selectedAnswerIndexQ7 +selectedAnswerIndexQ8
                     + selectedAnswerIndexQ9 + selectedAnswerIndexQ10 + selectedAnswerIndexQ11 +selectedAnswerIndexQ12;
    }

    public static double getQuestionCount() {
        return 12.;
    }
}

