package com.artursworld.nccn.controller.export;


import android.content.Context;
import android.support.annotation.NonNull;

import com.artursworld.nccn.controller.util.Dates;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CsvExporter {

    private static String CLASS_NAME = CsvExporter.class.getSimpleName();

    /**
     * Creates an exportable List containing following records
     *
     * @return the exportable List
     * @throws JSONException throws Exception in case the Input-JSON cannot be parsed
     */
    public static List<String[]> getExportableCsvListByUser(User user, Context ctx) {
        List<String[]> csvToExport = new ArrayList<>();

        // get values as list
        List<String[]> hadsdValues = getHadsdQuestionnaireValues(user, ctx);
        List<String[]> qualityOfLifeValues = getQoLQuestionnaireValues(user, ctx);
        List<String[]> distressThermometerValues = getDistressThermometerValues(user, ctx);

        //TODO: add all or splitt in 3 files?
        csvToExport.addAll(distressThermometerValues);
        csvToExport.addAll(hadsdValues);
        csvToExport.addAll(qualityOfLifeValues);

        return csvToExport;
    }

    /**
     * Get 'Distress thermometer' values by user
     *
     * @param user the selected user
     * @param ctx  the database context
     * @return information about 'Distress thermometer' questionnaire values
     */
    @NonNull
    private static List<String[]> getDistressThermometerValues(User user, Context ctx) {
        List<String[]> retList = new ArrayList<>();

        // get 'Distress thermometer' questionnaire List
        DistressThermometerQuestionnaireManager db = new DistressThermometerQuestionnaireManager(ctx);
        List<DistressThermometerQuestionnaire> list = db.getDistressThermometerQuestionnaireList(user.getName());

        // 'Quality Of Life' questionnaire information
        for (int i = 0; i < list.size(); i++) {

            // values to return
            int valueCount = 2;
            String[] csvRecordRow = new String[valueCount];

            csvRecordRow[0] = getStringOrConstant(list.get(i).getUpdateDate(), "nil");
            csvRecordRow[1] = getStringOrConstant(list.get(i).getDistressScore(), "nil");

            retList.add(csvRecordRow);
        }

        return retList;
    }


    /**
     * Get 'Quality Of Life' questionnaire values by user
     *
     * @param user the selected user
     * @param ctx  the database context
     * @return information about 'Quality Of Life' questionnaire values
     */
    @NonNull
    private static List<String[]> getQoLQuestionnaireValues(User user, Context ctx) {
        List<String[]> retList = new ArrayList<>();

        // get 'Quality Of Life' questionnaire List
        QualityOfLifeManager db = new QualityOfLifeManager(ctx);
        List<QolQuestionnaire> list = db.getQolQuestionnaireList(user.getName());

        // 'Quality Of Life' questionnaire information
        for (int i = 0; i < list.size(); i++) {

            // values to return
            int valueCount = 29;
            String[] csvRecordRow = new String[valueCount];

            int index = 0;
            csvRecordRow[index] = Dates.getGermanDateByDate(list.get(i).getCreationDate_PK());
            csvRecordRow[++index] = Dates.getGermanDateByDate(list.get(i).getUpdateDate());

            // global health score
            csvRecordRow[++index] = String.valueOf(list.get(i).getGlobalHealthScore());

            // functional scale
            csvRecordRow[++index] = String.valueOf(list.get(i).getPhysicalFunctioningScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getRoleFunctioningScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getEmotionalFunctioningScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getCognitiveFunctioningScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getSocialFunctioningScore());

            // symptom scale
            csvRecordRow[++index] = String.valueOf(list.get(i).getFatigueScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getNauseaAndVomitingScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getPainScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getDyspnoeaScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getInsomniaScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getAppetiteLossScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getConstipationScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getDiarrhoeaScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getFinancialDifficultiesScore());

            // brain cancer module
            csvRecordRow[++index] = String.valueOf(list.get(i).getFutureUncertaintyScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getVisualDisorderScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getMotorDysfunctionScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getCommunicationDeficitScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getHeadachesScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getSeizuresScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getDrowsinessScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getHairLossScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getItchySkinScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getWeaknessOfLegsScore());
            csvRecordRow[++index] = String.valueOf(list.get(i).getBladderControlScore());

            retList.add(csvRecordRow);
        }

        return retList;
    }

    /**
     * Get HADS-D values by user
     *
     * @param user the selected user
     * @param ctx  the database context
     * @return information about HADS-D questionnaire values
     */
    @NonNull
    private static List<String[]> getHadsdQuestionnaireValues(User user, Context ctx) {
        List<String[]> retList = new ArrayList<>();

        // get HADS-D List
        HADSDQuestionnaireManager hadsDb = new HADSDQuestionnaireManager(ctx);
        List<HADSDQuestionnaire> list = hadsDb.getHadsdQuestionnaireListByUserName(user.getName());

        // hads-d information
        for (int i = 0; i < list.size(); i++) {

            // values to return
            int valueCount = 3;
            String[] questionnaireResultRow = new String[valueCount];

            questionnaireResultRow[0] = getStringOrConstant(list.get(i).getUpdateDate(), "nil");
            questionnaireResultRow[1] = getStringOrConstant(list.get(i).getDepressionScore(), "nil");
            questionnaireResultRow[2] = getStringOrConstant(list.get(i).getAnxietyScore(), "nil");

            retList.add(questionnaireResultRow);
        }

        return retList;
    }

    /**
     * Check if given value is not null. If null -> return constant
     *
     * @param value    the value to check
     * @param constant the constant to return
     * @return Check if given value is not null. If null -> return constant
     */
    @NonNull
    private static String getStringOrConstant(Object value, String constant) {
        if (value != null) {
            return value.toString();
        }
        return constant;
    }


    /**
     * Get the amount of the largest list
     *
     * @param list  the first list
     * @param list2 the second list
     * @param list3 the third list
     * @return the size of the biggest list
     */
     /*private static int getMaxListSize(List list, List list2, List list3) {
        int maxSize = 0;
        if (list != null)
            if (list.size() > maxSize)
                maxSize = list.size();

        if (list2 != null)
            if (list2.size() > maxSize)
                maxSize = list2.size();

        if (list3 != null)
            if (list3.size() > maxSize)
                maxSize = list3.size();

        return maxSize;
    }
    */

}
