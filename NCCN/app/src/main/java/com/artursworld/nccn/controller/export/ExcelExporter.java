package com.artursworld.nccn.controller.export;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.controller.util.Dates;
import com.artursworld.nccn.controller.util.Questionnairy;
import com.artursworld.nccn.controller.util.Security;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.MetaQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.MetaQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import jxl.Cell;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelExporter {

    private static String CLASS_NAME = ExcelExporter.class.getName();

    public static File export(JSONArray questionnaireData) {

        File file = null;
        try {
            // get SD environment
            File sd = Environment.getExternalStorageDirectory();
            String fileName = getExcelFileName();

            // get directory
            File baseDirectory = new File(sd.getAbsolutePath());
            File directory = new File(baseDirectory, Strings.getStringByRId(R.string.app_name));

            //create directory if not exist
            boolean isDirectoryCreated = directory.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = directory.mkdir();
            }

            if (isDirectoryCreated) {

                // create file path
                file = new File(directory, fileName);

                // create workbook containing sheets later on
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));
                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                WritableCellFormat headerCellFormat = getWritableCellFormat();

                // create sheet
                WritableSheet sheet = workbook.createSheet(Strings.getStringByRId(R.string.app_name), 0);


                sheet = getSheetHeader(sheet, headerCellFormat);

                // cell format
                WritableCellFormat cellFormat = new WritableCellFormat();
                cellFormat.setAlignment(Alignment.CENTRE);

                //add data to sheet
                sheet = addDataToWorksheet(questionnaireData, sheet, cellFormat);
                autoFitColumnsByWritableSheets(sheet);

                // close workbook
                workbook.write();
                workbook.close();

                Log.d(CLASS_NAME, "excel created successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static WritableSheet addDataToWorksheet(JSONArray questionnaireData, WritableSheet sheet, WritableCellFormat cellFormat) {
        if (sheet != null) {
            try {

                // for each questionnaire
                for (int index = 0; index < questionnaireData.length(); index++) { // int index = 0; index < questionnaireData.length(); index++

                    // save row size
                    int rowSize = sheet.getRows();

                    // get questionnaire
                    JSONObject singleQuestionnaire = questionnaireData.getJSONObject(index).getJSONObject("_source");

                    // get all key of a single questionnaire
                    Iterator<String> singleQuestionnaireKeys = singleQuestionnaire.keys();

                    // for each key
                    while (singleQuestionnaireKeys.hasNext()) {
                        String key = singleQuestionnaireKeys.next();
                        String value = String.valueOf(singleQuestionnaire.get(key));

                        // add a cell at the place
                        sheet.addCell(new Label(getExcelHeaderKeyList().indexOf(key), rowSize, value, cellFormat));
                    }

                }

            } catch (Exception e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    private static WritableSheet getSheetHeader(WritableSheet sheet, WritableCellFormat headerCellFormat) {
        if (sheet != null) {
            try {

                List<String> headerKeyList = getExcelHeaderKeyList();
                for (int index = 0; index < headerKeyList.size(); index++) {
                    sheet.addCell(new Label(index, 0, headerKeyList.get(index), headerCellFormat));
                }

            } catch (Exception e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    @NonNull
    private static WritableCellFormat getWritableCellFormat() throws WriteException {
        // cell format
        WritableCellFormat headerCellFormat = new WritableCellFormat();
        headerCellFormat.setAlignment(Alignment.CENTRE);
        headerCellFormat.setFont(
                new WritableFont(
                        WritableFont.ARIAL,
                        10, WritableFont.BOLD,
                        false,
                        UnderlineStyle.NO_UNDERLINE,
                        jxl.format.Colour.BLACK));
        return headerCellFormat;
    }

    @NonNull
    private static String getExcelFileName() {
        // get file name to create
        StringBuilder builder = new StringBuilder();
        builder.append(Strings.getStringByRId(R.string.app_name));
        builder.append("-");
        builder.append(Dates.getFileNameDate(new Date()));
        builder.append(".xls");
        return builder.toString();
    }

    public static File export() {
        File file = null;
        try {
            // get SD environment
            File sd = Environment.getExternalStorageDirectory();

            // get file name to create
            String fileName = getExcelFileName();

            // get directory
            File baseDirectory = new File(sd.getAbsolutePath());
            File directory = new File(baseDirectory, Strings.getStringByRId(R.string.app_name));

            //create directory if not exist
            boolean isDirectoryCreated = directory.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = directory.mkdir();
            }

            if (isDirectoryCreated) {

                // create file path
                file = new File(directory, fileName);

                // create workbook containing sheets later on
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));
                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);

                // cell format
                WritableCellFormat headerCellFormat = getWritableCellFormat();


                // create distress thermometer sheet
                WritableSheet distressSheet = workbook.createSheet(Strings.getStringByRId(R.string.nccn_distress_thermometer), 0);
                distressSheet = ExcelExporter.addDistressSheetHeader(distressSheet, headerCellFormat);

                // create HADS-D  questionnaire sheet
                WritableSheet hadsdSheet = workbook.createSheet(Strings.getStringByRId(R.string.hadsd_questionnaire), 1);
                hadsdSheet = ExcelExporter.addHadsdSheetHeader(hadsdSheet, headerCellFormat);

                // Quality of life questionnaire
                WritableSheet qolSheet = workbook.createSheet(Strings.getStringByRId(R.string.quality_of_life_questionnaire), 2);
                qolSheet = ExcelExporter.addQualityOfLifeSheetHeader(qolSheet, headerCellFormat);

                // Brain Cancer Module
                WritableSheet cancerSheet = workbook.createSheet(Strings.getStringByRId(R.string.brain_cancer_module), 3);
                cancerSheet = ExcelExporter.addBrainCancerModuleSheetHeader(cancerSheet, headerCellFormat);

                // Meta information
                WritableSheet metaSheet = workbook.createSheet(Strings.getStringByRId(R.string.meta_data), 4);
                metaSheet = ExcelExporter.addMetaSheetHeader(metaSheet, headerCellFormat);

                // go through all user in database
                for (User user : new UserManager().getAllUsers()) {

                    // cell format
                    WritableCellFormat cellFormat = new WritableCellFormat();
                    cellFormat.setAlignment(Alignment.CENTRE);

                    //add data to distress thermometer sheet
                    distressSheet = ExcelExporter.getDistressThermometerWorksheet(user, distressSheet, App.getAppContext(), cellFormat);

                    // add HADS-D sheet
                    hadsdSheet = ExcelExporter.getHadsdWorksheet(user, hadsdSheet, App.getAppContext(), cellFormat);

                    // add quality of life sheet
                    qolSheet = ExcelExporter.getQualityOfLifeWorksheet(user, qolSheet, App.getAppContext(), cellFormat);

                    // add brain cancer module sheet
                    cancerSheet = ExcelExporter.getBrainCancerModuleWorksheet(user, cancerSheet, App.getAppContext(), cellFormat);

                    // add meta data infos
                    metaSheet = ExcelExporter.getMetaQuestionnaireWorksheet(user, metaSheet, cellFormat);

                    autoFitColumnsByWritableSheets(distressSheet, hadsdSheet, qolSheet, cancerSheet, metaSheet);
                }

                // close workbook
                workbook.write();
                workbook.close();

                Log.d(CLASS_NAME, "excel created successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * Auto fits every column in the sheets
     *
     * @param sheets the sheets to use
     */
    private static void autoFitColumnsByWritableSheets(WritableSheet... sheets) {
        for (WritableSheet sheet : sheets) {

            //
            WritableCellFormat newFormat;
            for (int j = 0; j < sheet.getColumns(); j++) {
                for (int k = 0; k < sheet.getRows(); k++) {
                    Cell readCell = sheet.getCell(j, k);
                    WritableCellFormat cellFormatObj = new WritableCellFormat();
                    CellFormat readFormat = readCell.getCellFormat() == null ? cellFormatObj : readCell.getCellFormat();
                    newFormat = new WritableCellFormat(readFormat);
                    try {
                        newFormat.setAlignment(Alignment.CENTRE);
                    } catch (WriteException e) {
                        Log.e(CLASS_NAME, e.getLocalizedMessage());
                    }
                }
            }
            //

            for (int i = 0; i < sheet.getColumns(); i++) {
                Cell[] cells = sheet.getColumn(i);
                int longestStrLen = -1;

                if (cells.length == 0)
                    continue;

                /* Find the widest cell in the column. */
                for (int j = 0; j < cells.length; j++) {
                    if (cells[j].getContents().length() > longestStrLen) {
                        String str = cells[j].getContents();
                        if (str == null || str.isEmpty())
                            continue;
                        longestStrLen = str.trim().length();
                    }
                }

                // If not found, skip the column.
                if (longestStrLen == -1)
                    continue;

                // If wider than the max width, crop width
                if (longestStrLen > 255)
                    longestStrLen = 255;
                CellView cv = sheet.getColumnView(i);

                // Every character is 256 units wide, so scale it
                cv.setSize(longestStrLen * 256 + 100);
                sheet.setColumnView(i, cv);
            }
        }
    }


    /**
     * Adds first row containing header information about 'Meta information' into the sheet
     *
     * @param sheet the sheet to add the header
     * @return the sheet containing first row with header information for 'Meta information' questionnaire
     */
    private static WritableSheet addMetaSheetHeader(WritableSheet sheet, WritableCellFormat cellFormat) {
        if (sheet != null) {
            try {
                sheet.addCell(new Label(0, 0, "user-name", cellFormat));
                sheet.addCell(new Label(1, 0, "creation-date", cellFormat));
                sheet.addCell(new Label(2, 0, "update-date", cellFormat));
                sheet.addCell(new Label(3, 0, "operation-type", cellFormat));
                sheet.addCell(new Label(4, 0, "had-psychosocial-support", cellFormat));
                sheet.addCell(new Label(5, 0, "need-psychosocial-support", cellFormat));
            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    /**
     * Adds first row containing header information about 'Brain Cancer Module' into the sheet
     *
     * @param sheet      the sheet to add the header
     * @param cellFormat
     * @return the sheet containing first row with header information for 'Brain Cancer Module' questionnaire
     */
    private static WritableSheet addBrainCancerModuleSheetHeader(WritableSheet sheet, WritableCellFormat cellFormat) {
        if (sheet != null) {
            try {
                sheet.addCell(new Label(0, 0, "user-name", cellFormat));
                sheet.addCell(new Label(1, 0, "creation-date", cellFormat));
                sheet.addCell(new Label(2, 0, "update-date", cellFormat));
                sheet.addCell(new Label(3, 0, "future-uncertainty", cellFormat));
                sheet.addCell(new Label(4, 0, "visual-disorder", cellFormat));
                sheet.addCell(new Label(5, 0, "motor-dysfunction", cellFormat));
                sheet.addCell(new Label(6, 0, "communication-deficit", cellFormat));
                sheet.addCell(new Label(7, 0, "headaches", cellFormat));
                sheet.addCell(new Label(8, 0, "seizures", cellFormat));
                sheet.addCell(new Label(9, 0, "drowsiness", cellFormat));
                sheet.addCell(new Label(10, 0, "hair-loss", cellFormat));
                sheet.addCell(new Label(11, 0, "itchy-skin", cellFormat));
                sheet.addCell(new Label(12, 0, "weakness-of-legs", cellFormat));
                sheet.addCell(new Label(13, 0, "bladder-control", cellFormat));

            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    /**
     * Adds first row containing header information into the sheet
     *
     * @param sheet      the sheet to add the header
     * @param cellFormat
     * @return the sheet containing first row with header information for Quality of Life questionnaire
     */
    private static WritableSheet addQualityOfLifeSheetHeader(WritableSheet sheet, WritableCellFormat cellFormat) {
        if (sheet != null) {
            try {
                sheet.addCell(new Label(0, 0, "user-name", cellFormat));
                sheet.addCell(new Label(1, 0, "creation-date", cellFormat));
                sheet.addCell(new Label(2, 0, "update-date", cellFormat));
                sheet.addCell(new Label(3, 0, "global-health-status", cellFormat));
                sheet.addCell(new Label(4, 0, "physical-functioning", cellFormat));
                sheet.addCell(new Label(5, 0, "role-functioning", cellFormat));
                sheet.addCell(new Label(6, 0, "emotional-functioning", cellFormat));
                sheet.addCell(new Label(7, 0, "cognitive-functioning", cellFormat));
                sheet.addCell(new Label(8, 0, "social-functioning", cellFormat));
                sheet.addCell(new Label(9, 0, "fatigue", cellFormat));
                sheet.addCell(new Label(10, 0, "nausea-and-vomiting", cellFormat));
                sheet.addCell(new Label(11, 0, "pain", cellFormat));
                sheet.addCell(new Label(12, 0, "dyspnoea", cellFormat));
                sheet.addCell(new Label(13, 0, "insomnia", cellFormat));
                sheet.addCell(new Label(14, 0, "appetite-loss", cellFormat));
                sheet.addCell(new Label(15, 0, "constipation", cellFormat));
                sheet.addCell(new Label(16, 0, "diarrhoea", cellFormat));
                sheet.addCell(new Label(17, 0, "financial-difficulties", cellFormat));
            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }


    /**
     * Adds first row containing header information into the sheet
     *
     * @param sheet      the sheet to add the header
     * @param cellFormat
     * @return the sheet containing first row with header information for HADS-D questionnaire
     */
    private static WritableSheet addHadsdSheetHeader(WritableSheet sheet, WritableCellFormat cellFormat) {
        if (sheet != null) {
            try {
                sheet.addCell(new Label(0, 0, "user-name", cellFormat));
                sheet.addCell(new Label(1, 0, "creation-date", cellFormat));
                sheet.addCell(new Label(2, 0, "update-date", cellFormat));
                sheet.addCell(new Label(3, 0, "anxiety-score", cellFormat));
                sheet.addCell(new Label(4, 0, "depression-score", cellFormat));
                sheet.addCell(new Label(5, 0, "has-anxiety", cellFormat));
                sheet.addCell(new Label(6, 0, "has-depression", cellFormat));
            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    /**
     * Adds first row containing header information into the sheet
     *
     * @param sheet      the sheet to add the header
     * @param cellFormat
     * @return the sheet containing first row with header information for distress thermometer questionnaire
     */
    private static WritableSheet addDistressSheetHeader(WritableSheet sheet, WritableCellFormat cellFormat) {
        if (sheet != null) {
            try {
                sheet.addCell(new Label(0, 0, "user-name", cellFormat));
                sheet.addCell(new Label(1, 0, "creation-date", cellFormat));
                sheet.addCell(new Label(2, 0, "update-date", cellFormat));
                sheet.addCell(new Label(3, 0, "distress-score", cellFormat));
                sheet.addCell(new Label(4, 0, "has-distress", cellFormat));
            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    /**
     * Fills excel sheet with distress thermometer data
     *
     * @param user  the user selected
     * @param sheet the sheet to fill with data
     * @param ctx   the context to use
     * @return the excel sheet containing distress thermometer data
     */
    public static WritableSheet getDistressThermometerWorksheet(User user, WritableSheet sheet, Context ctx, WritableCellFormat cellFormat) {
        if (sheet != null) {
            try {

                // get all questionnaire date for a single user
                List<Date> dates = new UserManager().getQuestionnaireDateListByUserName(user.getName());
                Log.d(CLASS_NAME, "distress thermometer questionnaires count: " + dates.size());

                // for each questionnaire
                for (int index = 0; index < dates.size(); index++) {

                    // get questionnaire by date
                    DistressThermometerQuestionnaireManager db = new DistressThermometerQuestionnaireManager(ctx);
                    DistressThermometerQuestionnaire questionnaire = db.getDistressThermometerQuestionnaireByDate(user.getName(), dates.get(index));

                    if (questionnaire != null) {
                        // put data into sheet
                        if (Questionnairy.canStatisticsBeDisplayed(questionnaire.getProgressInPercent())) {
                            int rowSize = sheet.getRows();
                            sheet.addCell(new Label(0, rowSize, Security.getUserNameByEncryption(user), cellFormat));
                            sheet.addCell(new Label(1, rowSize, Dates.getGermanDateByDate(questionnaire.getCreationDate_PK()), cellFormat));
                            sheet.addCell(new Label(2, rowSize, Dates.getGermanDateByDate(questionnaire.getUpdateDate()), cellFormat));
                            sheet.addCell(new Label(3, rowSize, String.valueOf(questionnaire.getDistressScore()), cellFormat));
                            sheet.addCell(new Label(4, rowSize, String.valueOf(questionnaire.hasDistress()), cellFormat));
                        }
                    }

                }

            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    /**
     * Fills excel sheet with HADS-D data
     *
     * @param user  the user selected
     * @param sheet the sheet to fill with data
     * @param ctx   the context to use
     * @return the excel sheet containing 'HADS-D' data
     */
    private static WritableSheet getHadsdWorksheet(User user, WritableSheet sheet, Context ctx, WritableCellFormat cellFormat) {
        if (sheet != null) {
            try {

                // get all questionnaire date for a single user
                List<Date> dates = new UserManager().getQuestionnaireDateListByUserName(user.getName());
                Log.d(CLASS_NAME, "HADS-D questionnaires count: " + dates.size());

                // for each questionnaire
                for (int index = 0; index < dates.size(); index++) {

                    // get questionnaire by date
                    HADSDQuestionnaireManager db = new HADSDQuestionnaireManager(ctx);
                    HADSDQuestionnaire questionnaire = db.getHADSDQuestionnaireByDate_PK(user.getName(), dates.get(index));

                    if (questionnaire != null) {
                        // put data into sheet
                        if (Questionnairy.canStatisticsBeDisplayed(questionnaire.getProgressInPercent())) {
                            int rowSize = sheet.getRows();
                            sheet.addCell(new Label(0, rowSize, Security.getUserNameByEncryption(user), cellFormat));
                            sheet.addCell(new Label(1, rowSize, Dates.getGermanDateByDate(questionnaire.getCreationDate_PK()), cellFormat));
                            sheet.addCell(new Label(2, rowSize, Dates.getGermanDateByDate(questionnaire.getUpdateDate()), cellFormat));
                            sheet.addCell(new Label(3, rowSize, String.valueOf(questionnaire.getAnxietyScore()), cellFormat));
                            sheet.addCell(new Label(4, rowSize, String.valueOf(questionnaire.getDepressionScore()), cellFormat));
                            sheet.addCell(new Label(5, rowSize, String.valueOf(questionnaire.hasAnxiety()), cellFormat));
                            sheet.addCell(new Label(6, rowSize, String.valueOf(questionnaire.hasDepression()), cellFormat));
                        }
                    }

                }

            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    /**
     * Fills excel sheet with 'Quality of Life' data
     *
     * @param user  the user selected
     * @param sheet the sheet to fill with data
     * @param ctx   the context to use
     * @return the excel sheet containing 'Quality of Life' data
     */
    private static WritableSheet getQualityOfLifeWorksheet(User user, WritableSheet sheet, Context ctx, WritableCellFormat cellFormat) {
        if (sheet != null) {
            try {

                // get all questionnaire date for a single user
                List<Date> dates = new UserManager().getQuestionnaireDateListByUserName(user.getName());
                Log.d(CLASS_NAME, "Quality of Life questionnaires count: " + dates.size());

                // for each questionnaire
                for (int index = 0; index < dates.size(); index++) {

                    // get questionnaire by date
                    QualityOfLifeManager db = new QualityOfLifeManager(ctx);
                    QolQuestionnaire questionnaire = db.getQolQuestionnaireByDate(user.getName(), dates.get(index));

                    if (questionnaire != null) {
                        // put data into sheet
                        if (Questionnairy.canStatisticsBeDisplayed(questionnaire.getProgressInPercent())) {
                            int rowSize = sheet.getRows();
                            sheet.addCell(new Label(0, rowSize, Security.getUserNameByEncryption(user)));
                            sheet.addCell(new Label(1, rowSize, Dates.getGermanDateByDate(questionnaire.getCreationDate_PK()), cellFormat));
                            sheet.addCell(new Label(2, rowSize, Dates.getGermanDateByDate(questionnaire.getUpdateDate()), cellFormat));
                            sheet.addCell(new Label(3, rowSize, String.valueOf(questionnaire.getGlobalHealthScore()), cellFormat));
                            sheet.addCell(new Label(4, rowSize, String.valueOf(questionnaire.getPhysicalFunctioningScore()), cellFormat));
                            sheet.addCell(new Label(5, rowSize, String.valueOf(questionnaire.getRoleFunctioningScore()), cellFormat));
                            sheet.addCell(new Label(6, rowSize, String.valueOf(questionnaire.getEmotionalFunctioningScore()), cellFormat));
                            sheet.addCell(new Label(7, rowSize, String.valueOf(questionnaire.getCognitiveFunctioningScore()), cellFormat));
                            sheet.addCell(new Label(8, rowSize, String.valueOf(questionnaire.getSocialFunctioningScore()), cellFormat));
                            sheet.addCell(new Label(9, rowSize, String.valueOf(questionnaire.getFatigueScore()), cellFormat));
                            sheet.addCell(new Label(10, rowSize, String.valueOf(questionnaire.getNauseaAndVomitingScore()), cellFormat));
                            sheet.addCell(new Label(11, rowSize, String.valueOf(questionnaire.getPainScore()), cellFormat));
                            sheet.addCell(new Label(12, rowSize, String.valueOf(questionnaire.getDyspnoeaScore()), cellFormat));
                            sheet.addCell(new Label(13, rowSize, String.valueOf(questionnaire.getInsomniaScore()), cellFormat));
                            sheet.addCell(new Label(14, rowSize, String.valueOf(questionnaire.getAppetiteLossScore()), cellFormat));
                            sheet.addCell(new Label(15, rowSize, String.valueOf(questionnaire.getConstipationScore()), cellFormat));
                            sheet.addCell(new Label(16, rowSize, String.valueOf(questionnaire.getDiarrhoeaScore()), cellFormat));
                            sheet.addCell(new Label(17, rowSize, String.valueOf(questionnaire.getFinancialDifficultiesScore()), cellFormat));
                        }
                    }

                }

            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    /**
     * Fills excel sheet with 'Brain Cancer Module' data
     *
     * @param user  the user selected
     * @param sheet the sheet to fill with data
     * @param ctx   the context to use
     * @return the excel sheet containing 'Brain Cancer Module' data
     */
    private static WritableSheet getBrainCancerModuleWorksheet(User user, WritableSheet sheet, Context ctx, WritableCellFormat cellFormat) {
        if (sheet != null) try {

            // get all questionnaire date for a single user
            List<Date> dates = new UserManager().getQuestionnaireDateListByUserName(user.getName());
            Log.d(CLASS_NAME, "'Brain Cancer Module' questionnaires count: " + dates.size());

            // for each questionnaire
            for (int index = 0; index < dates.size(); index++) {

                // get questionnaire by date
                QualityOfLifeManager db = new QualityOfLifeManager(ctx);
                QolQuestionnaire questionnaire = db.getQolQuestionnaireByDate(user.getName(), dates.get(index));

                if (questionnaire != null) {
                    // put data into sheet
                    if (Questionnairy.canStatisticsBeDisplayed(questionnaire.getProgressInPercent())) {
                        int rowSize = sheet.getRows();
                        sheet.addCell(new Label(0, rowSize, Security.getUserNameByEncryption(user)));
                        sheet.addCell(new Label(1, rowSize, Dates.getGermanDateByDate(questionnaire.getCreationDate_PK()), cellFormat));
                        sheet.addCell(new Label(2, rowSize, Dates.getGermanDateByDate(questionnaire.getUpdateDate()), cellFormat));
                        sheet.addCell(new Label(3, rowSize, String.valueOf(questionnaire.getFutureUncertaintyScore()), cellFormat));
                        sheet.addCell(new Label(4, rowSize, String.valueOf(questionnaire.getVisualDisorderScore()), cellFormat));
                        sheet.addCell(new Label(5, rowSize, String.valueOf(questionnaire.getMotorDysfunctionScore()), cellFormat));
                        sheet.addCell(new Label(6, rowSize, String.valueOf(questionnaire.getCommunicationDeficitScore()), cellFormat));
                        sheet.addCell(new Label(7, rowSize, String.valueOf(questionnaire.getHeadachesScore()), cellFormat));
                        sheet.addCell(new Label(8, rowSize, String.valueOf(questionnaire.getSeizuresScore()), cellFormat));
                        sheet.addCell(new Label(9, rowSize, String.valueOf(questionnaire.getDrowsinessScore()), cellFormat));
                        sheet.addCell(new Label(10, rowSize, String.valueOf(questionnaire.getHairLossScore()), cellFormat));
                        sheet.addCell(new Label(11, rowSize, String.valueOf(questionnaire.getItchySkinScore()), cellFormat));
                        sheet.addCell(new Label(12, rowSize, String.valueOf(questionnaire.getWeaknessOfLegsScore()), cellFormat));
                        sheet.addCell(new Label(13, rowSize, String.valueOf(questionnaire.getBladderControlScore()), cellFormat));
                    }
                }

            }

        } catch (WriteException e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }

        return sheet;
    }

    /**
     * Fills excel sheet with Meta information
     *
     * @param user  the user selected
     * @param sheet the sheet to fill with data
     * @return the excel sheet containing meta information
     */
    private static WritableSheet getMetaQuestionnaireWorksheet(User user, WritableSheet sheet, WritableCellFormat cellFormat) {

        if (sheet != null) try {

            // get all questionnaire date for a single user
            List<Date> dates = new UserManager().getQuestionnaireDateListByUserName(user.getName());
            Log.d(CLASS_NAME, "'Meta information' questionnaires count: " + dates.size());

            // for each questionnaire
            for (int index = 0; index < dates.size(); index++) {

                // get questionnaire by date
                MetaQuestionnaire meta = new MetaQuestionnaireManager().getMetaDataByCreationDate(dates.get(index));

                if (meta != null) {
                    // put data into sheet
                    int rowSize = sheet.getRows();
                    sheet.addCell(new Label(0, rowSize, Security.getUserNameByEncryption(user)));
                    sheet.addCell(new Label(1, rowSize, Dates.getGermanDateByDate(meta.getCreationDate()), cellFormat));
                    sheet.addCell(new Label(2, rowSize, Dates.getGermanDateByDate(meta.getUpdateDate()), cellFormat));
                    sheet.addCell(new Label(3, rowSize, String.valueOf(meta.getOperationType()), cellFormat));
                    sheet.addCell(new Label(4, rowSize, String.valueOf(meta.getPastPsychoSocialSupportState()), cellFormat));
                    sheet.addCell(new Label(5, rowSize, String.valueOf(meta.getPsychoSocialSupportState().name()), cellFormat));
                }
            }

        } catch (WriteException e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }

        return sheet;
    }

    public static List<String> getExcelHeaderKeyList() {
        List<String> keys = new ArrayList<>();
        keys.add("user-name");
        keys.add("creation-date");
        keys.add("operation-type");
        keys.add("need-psychosocial-support");
        keys.add("had-psychosocial-support");
        keys.add("DT-distress-score");
        keys.add("DT-has-distress");
        keys.add("DT-update-date");
        keys.add("QLQC30-global-health-status");
        keys.add("QLQC30-physical-functioning");
        keys.add("QLQC30-role-functioning");
        keys.add("QLQC30-emotional-functioning");
        keys.add("QLQC30-cognitive-functioning");
        keys.add("QLQC30-social-functioning");
        keys.add("QLQC30-fatigue");
        keys.add("QLQC30-nausea-and-vomiting");
        keys.add("QLQC30-pain");
        keys.add("QLQC30-dyspnoea");
        keys.add("QLQC30-insomnia");
        keys.add("QLQC30-appetite-loss");
        keys.add("QLQC30-constipation");
        keys.add("QLQC30-diarrhoea");
        keys.add("QLQC30-financial-difficulties");
        keys.add("QLQC30-update-date");
        keys.add("BN20-future-uncertainty");
        keys.add("BN20-visual-disorder");
        keys.add("BN20-motor-dysfunction");
        keys.add("BN20-communication-deficit");
        keys.add("BN20-headaches");
        keys.add("BN20-seizures");
        keys.add("BN20-drowsiness");
        keys.add("BN20-hair-loss");
        keys.add("BN20-itchy-skin");
        keys.add("BN20-weakness-of-legs");
        keys.add("BN20-bladder-control");
        keys.add("BN20-update-date");
        keys.add("HADS-D-anxiety-score");
        keys.add("HADS-D-depression-score");
        keys.add("HADS-D-has-depression");
        keys.add("HADS-D-has-anxiety");
        keys.add("HADS-D-update-date");
        keys.add("FOP-score");
        keys.add("FOP-update-date");
        return keys;
    }

}
