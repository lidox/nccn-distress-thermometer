package com.artursworld.nccn.controller.export;

import android.content.Context;
import android.os.Environment;
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

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Cell;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.write.BoldStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelExporter {

    private static String CLASS_NAME = ExcelExporter.class.getName();

    public static void export() {
        try {
            // get SD environment
            File sd = Environment.getExternalStorageDirectory();

            // get file name to create
            StringBuilder builder = new StringBuilder();
            builder.append(Strings.getStringByRId(R.string.app_name));
            builder.append("-");
            builder.append(Dates.getFileNameDate(new Date()));
            builder.append(".xls");
            String fileName = builder.toString();

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
                File file = new File(directory, fileName);

                // create workbook containing sheets later on
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));
                WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);

                // create distress thermometer sheet
                WritableSheet distressSheet = workbook.createSheet(Strings.getStringByRId(R.string.nccn_distress_thermometer), 0);
                distressSheet = ExcelExporter.addDistressThermometerSheetHeader(distressSheet);

                // create HADS-D  questionnaire sheet
                WritableSheet hadsdSheet = workbook.createSheet(Strings.getStringByRId(R.string.hadsd_questionnaire), 1);
                hadsdSheet = ExcelExporter.addHadsdSheetHeader(hadsdSheet);

                // Quality of life questionnaire
                WritableSheet qolSheet = workbook.createSheet(Strings.getStringByRId(R.string.quality_of_life_questionnaire), 2);
                qolSheet = ExcelExporter.addQualityOfLifeSheetHeader(qolSheet);

                // Brain Cancer Module
                WritableSheet cancerSheet = workbook.createSheet(Strings.getStringByRId(R.string.brain_cancer_module), 3);
                cancerSheet = ExcelExporter.addBrainCancerModuleSheetHeader(cancerSheet);

                // Meta information
                WritableSheet metaSheet = workbook.createSheet(Strings.getStringByRId(R.string.meta_data), 4);
                metaSheet = ExcelExporter.addMetaSheetHeader(metaSheet);

                // go through all user in database
                for (User user : new UserManager().getAllUsers()) {

                    //add data to distress thermometer sheet
                    distressSheet = ExcelExporter.getDistressThermometerWorksheet(user, distressSheet, App.getAppContext());

                    // add HADS-D sheet
                    hadsdSheet = ExcelExporter.getHadsdWorksheet(user, hadsdSheet, App.getAppContext());

                    // add quality of life sheet
                    qolSheet = ExcelExporter.getQualityOfLifeWorksheet(user, qolSheet, App.getAppContext());

                    // add brain cancer module sheet
                    cancerSheet = ExcelExporter.getBrainCancerModuleWorksheet(user, cancerSheet, App.getAppContext());

                    // add meta data infos
                    metaSheet = ExcelExporter.getMetaQuestionnaireWorksheet(user, metaSheet);

                }

                // close workbook
                workbook.write();
                workbook.close();

                Log.d(CLASS_NAME, "excel created successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    private static void setHeader(WritableSheet sheet) {
        try {
            // Lets create a times font
            WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat cellFormatObj = new WritableCellFormat(font);
            Cell readCell = sheet.getCell(0, 0);

            CellFormat readFormat = readCell.getCellFormat() == null ? cellFormatObj : readCell.getCellFormat();
            readFormat = new WritableCellFormat(readFormat);
            newFormat.setAlignment(Alignment.CENTRE);


            //bla
            // Lets create a times font
            WritableFont font = new WritableFont(WritableFont.ARIAL, 10);

            // Define the cell format
            WritableCellFormat times = new WritableCellFormat(font);

            // Lets automatically wrap the cells
            //times.setWrap(true);
            font.setBoldStyle(WritableFont.NO_BOLD);


            CellView cv = new CellView();
            cv.setFormat(times);

            sheet.setColumnView(0, cv);

        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }
    }
    */

    /**
     * Adds first row containing header information about 'Meta information' into the sheet
     *
     * @param sheet the sheet to add the header
     * @return the sheet containing first row with header information for 'Meta information' questionnaire
     */
    private static WritableSheet addMetaSheetHeader(WritableSheet sheet) {
        if (sheet != null) {
            try {
                sheet.addCell(new Label(0, 0, "user-name"));
                sheet.addCell(new Label(1, 0, "creation-date"));
                sheet.addCell(new Label(2, 0, "update-date"));
                sheet.addCell(new Label(3, 0, "operation-type"));
                sheet.addCell(new Label(4, 0, "had-psychosocial-support"));
                sheet.addCell(new Label(5, 0, "need-psychosocial-support"));
            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    /**
     * Adds first row containing header information about 'Brain Cancer Module' into the sheet
     *
     * @param sheet the sheet to add the header
     * @return the sheet containing first row with header information for 'Brain Cancer Module' questionnaire
     */
    private static WritableSheet addBrainCancerModuleSheetHeader(WritableSheet sheet) {
        if (sheet != null) {
            try {
                sheet.addCell(new Label(0, 0, "user-name"));
                sheet.addCell(new Label(1, 0, "creation-date"));
                sheet.addCell(new Label(2, 0, "update-date"));
                sheet.addCell(new Label(3, 0, "future-uncertainty"));
                sheet.addCell(new Label(4, 0, "visual-disorder"));
                sheet.addCell(new Label(5, 0, "motor-dysfunction"));
                sheet.addCell(new Label(6, 0, "communication-deficit"));
                sheet.addCell(new Label(7, 0, "headaches"));
                sheet.addCell(new Label(8, 0, "seizures"));
                sheet.addCell(new Label(9, 0, "drowsiness"));
                sheet.addCell(new Label(10, 0, "hair-loss"));
                sheet.addCell(new Label(11, 0, "itchy-skin"));
                sheet.addCell(new Label(12, 0, "weakness-of-legs"));
                sheet.addCell(new Label(13, 0, "bladder-control"));

            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    /**
     * Adds first row containing header information into the sheet
     *
     * @param sheet the sheet to add the header
     * @return the sheet containing first row with header information for Quality of Life questionnaire
     */
    private static WritableSheet addQualityOfLifeSheetHeader(WritableSheet sheet) {
        if (sheet != null) {
            try {
                sheet.addCell(new Label(0, 0, "user-name"));
                sheet.addCell(new Label(1, 0, "creation-date"));
                sheet.addCell(new Label(2, 0, "update-date"));
                sheet.addCell(new Label(3, 0, "global-health-status"));
                sheet.addCell(new Label(4, 0, "physical-functioning"));
                sheet.addCell(new Label(5, 0, "role-functioning"));
                sheet.addCell(new Label(6, 0, "emotional-functioning"));
                sheet.addCell(new Label(7, 0, "cognitive-functioning"));
                sheet.addCell(new Label(8, 0, "social-functioning"));
                sheet.addCell(new Label(9, 0, "fatigue"));
                sheet.addCell(new Label(10, 0, "nausea-and-vomiting"));
                sheet.addCell(new Label(11, 0, "pain"));
                sheet.addCell(new Label(12, 0, "dyspnoea"));
                sheet.addCell(new Label(13, 0, "insomnia"));
                sheet.addCell(new Label(14, 0, "appetite-loss"));
                sheet.addCell(new Label(15, 0, "constipation"));
                sheet.addCell(new Label(16, 0, "diarrhoea"));
                sheet.addCell(new Label(17, 0, "financial-difficulties"));
            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }


    /**
     * Adds first row containing header information into the sheet
     *
     * @param sheet the sheet to add the header
     * @return the sheet containing first row with header information for HADS-D questionnaire
     */
    private static WritableSheet addHadsdSheetHeader(WritableSheet sheet) {
        if (sheet != null) {
            try {
                sheet.addCell(new Label(0, 0, "user-name"));
                sheet.addCell(new Label(1, 0, "creation-date"));
                sheet.addCell(new Label(2, 0, "update-date"));
                sheet.addCell(new Label(3, 0, "anxiety-score"));
                sheet.addCell(new Label(4, 0, "depression-score"));
                sheet.addCell(new Label(5, 0, "has-anxiety"));
                sheet.addCell(new Label(6, 0, "has-depression"));
            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
        }
        return sheet;
    }

    /**
     * Adds first row containing header information into the sheet
     *
     * @param sheet the sheet to add the header
     * @return the sheet containing first row with header information for distress thermometer questionnaire
     */
    private static WritableSheet addDistressThermometerSheetHeader(WritableSheet sheet) {
        if (sheet != null) {
            try {
                sheet.addCell(new Label(0, 0, "user-name"));
                sheet.addCell(new Label(1, 0, "creation-date"));
                sheet.addCell(new Label(2, 0, "update-date"));
                sheet.addCell(new Label(3, 0, "distress-score"));
                sheet.addCell(new Label(4, 0, "has-distress"));
            } catch (WriteException e) {
                Log.e(CLASS_NAME, e.getLocalizedMessage());
            }
            //setHeader(sheet);
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
    public static WritableSheet getDistressThermometerWorksheet(User user, WritableSheet sheet, Context ctx) {
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
                            sheet.addCell(new Label(0, rowSize, Security.getUserNameByEncryption(user)));
                            sheet.addCell(new Label(1, rowSize, Dates.getGermanDateByDate(questionnaire.getCreationDate_PK())));
                            sheet.addCell(new Label(2, rowSize, Dates.getGermanDateByDate(questionnaire.getUpdateDate())));
                            sheet.addCell(new Label(3, rowSize, String.valueOf(questionnaire.getDistressScore())));
                            sheet.addCell(new Label(4, rowSize, String.valueOf(questionnaire.hasDistress())));
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
    private static WritableSheet getHadsdWorksheet(User user, WritableSheet sheet, Context ctx) {
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
                            sheet.addCell(new Label(0, rowSize, Security.getUserNameByEncryption(user)));
                            sheet.addCell(new Label(1, rowSize, Dates.getGermanDateByDate(questionnaire.getCreationDate_PK())));
                            sheet.addCell(new Label(2, rowSize, Dates.getGermanDateByDate(questionnaire.getUpdateDate())));
                            sheet.addCell(new Label(3, rowSize, String.valueOf(questionnaire.getAnxietyScore())));
                            sheet.addCell(new Label(4, rowSize, String.valueOf(questionnaire.getDepressionScore())));
                            sheet.addCell(new Label(5, rowSize, String.valueOf(questionnaire.hasAnxiety())));
                            sheet.addCell(new Label(6, rowSize, String.valueOf(questionnaire.hasDepression())));
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
    private static WritableSheet getQualityOfLifeWorksheet(User user, WritableSheet sheet, Context ctx) {
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
                            sheet.addCell(new Label(1, rowSize, Dates.getGermanDateByDate(questionnaire.getCreationDate_PK())));
                            sheet.addCell(new Label(2, rowSize, Dates.getGermanDateByDate(questionnaire.getUpdateDate())));
                            sheet.addCell(new Label(3, rowSize, String.valueOf(questionnaire.getGlobalHealthScore())));
                            sheet.addCell(new Label(4, rowSize, String.valueOf(questionnaire.getPhysicalFunctioningScore())));
                            sheet.addCell(new Label(5, rowSize, String.valueOf(questionnaire.getRoleFunctioningScore())));
                            sheet.addCell(new Label(6, rowSize, String.valueOf(questionnaire.getEmotionalFunctioningScore())));
                            sheet.addCell(new Label(7, rowSize, String.valueOf(questionnaire.getCognitiveFunctioningScore())));
                            sheet.addCell(new Label(8, rowSize, String.valueOf(questionnaire.getSocialFunctioningScore())));
                            sheet.addCell(new Label(9, rowSize, String.valueOf(questionnaire.getFatigueScore())));
                            sheet.addCell(new Label(10, rowSize, String.valueOf(questionnaire.getNauseaAndVomitingScore())));
                            sheet.addCell(new Label(11, rowSize, String.valueOf(questionnaire.getPainScore())));
                            sheet.addCell(new Label(12, rowSize, String.valueOf(questionnaire.getDyspnoeaScore())));
                            sheet.addCell(new Label(13, rowSize, String.valueOf(questionnaire.getInsomniaScore())));
                            sheet.addCell(new Label(14, rowSize, String.valueOf(questionnaire.getAppetiteLossScore())));
                            sheet.addCell(new Label(15, rowSize, String.valueOf(questionnaire.getConstipationScore())));
                            sheet.addCell(new Label(16, rowSize, String.valueOf(questionnaire.getDiarrhoeaScore())));
                            sheet.addCell(new Label(17, rowSize, String.valueOf(questionnaire.getFinancialDifficultiesScore())));
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
    private static WritableSheet getBrainCancerModuleWorksheet(User user, WritableSheet sheet, Context ctx) {
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
                        sheet.addCell(new Label(1, rowSize, Dates.getGermanDateByDate(questionnaire.getCreationDate_PK())));
                        sheet.addCell(new Label(2, rowSize, Dates.getGermanDateByDate(questionnaire.getUpdateDate())));
                        sheet.addCell(new Label(3, rowSize, String.valueOf(questionnaire.getFutureUncertaintyScore())));
                        sheet.addCell(new Label(4, rowSize, String.valueOf(questionnaire.getVisualDisorderScore())));
                        sheet.addCell(new Label(5, rowSize, String.valueOf(questionnaire.getMotorDysfunctionScore())));
                        sheet.addCell(new Label(6, rowSize, String.valueOf(questionnaire.getCommunicationDeficitScore())));
                        sheet.addCell(new Label(7, rowSize, String.valueOf(questionnaire.getHeadachesScore())));
                        sheet.addCell(new Label(8, rowSize, String.valueOf(questionnaire.getSeizuresScore())));
                        sheet.addCell(new Label(9, rowSize, String.valueOf(questionnaire.getDrowsinessScore())));
                        sheet.addCell(new Label(10, rowSize, String.valueOf(questionnaire.getHairLossScore())));
                        sheet.addCell(new Label(11, rowSize, String.valueOf(questionnaire.getItchySkinScore())));
                        sheet.addCell(new Label(12, rowSize, String.valueOf(questionnaire.getWeaknessOfLegsScore())));
                        sheet.addCell(new Label(13, rowSize, String.valueOf(questionnaire.getBladderControlScore())));
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
    private static WritableSheet getMetaQuestionnaireWorksheet(User user, WritableSheet sheet) {

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
                    sheet.addCell(new Label(1, rowSize, Dates.getGermanDateByDate(meta.getCreationDate())));
                    sheet.addCell(new Label(2, rowSize, Dates.getGermanDateByDate(meta.getUpdateDate())));
                    sheet.addCell(new Label(3, rowSize, String.valueOf(meta.getOperationType())));
                    sheet.addCell(new Label(4, rowSize, String.valueOf(meta.getPastPsychoSocialSupportState())));
                    sheet.addCell(new Label(5, rowSize, String.valueOf(meta.getPsychoSocialSupportState().name())));
                }
            }

        } catch (WriteException e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }

        return sheet;
    }
}
