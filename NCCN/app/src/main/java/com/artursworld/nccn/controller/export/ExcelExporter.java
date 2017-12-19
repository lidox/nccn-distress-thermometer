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
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelExporter {

    private static String CLASS_NAME = ExcelExporter.class.getName();

    public static void export() {
        try {
            // get SD environment
            File sd = Environment.getExternalStorageDirectory();

            // get file to create
            String csvFile = "yourFile.xls";
            File directory = new File(sd.getAbsolutePath());

            //create directory if not exist
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }

            // create file path
            File file = new File(directory, csvFile);

            // create workbook containing sheets later on
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);

            // create distress thermometer sheet
            WritableSheet distressSheet = workbook.createSheet(Strings.getStringByRId(R.string.nccn_distress_thermometer), 0);
            distressSheet = ExcelExporter.addDistressThermometerSheetHeader(distressSheet);

            // go through all user in database
            for (User user : new UserManager().getAllUsers()) {

                //add data to distress thermometer sheet
                distressSheet = ExcelExporter.getDistressThermometerWorksheet(user, distressSheet, App.getAppContext());
                //TODO: add the other worksheets here
            }

            /*
            //Excel sheetB represents second sheet
            WritableSheet sheetB = workbook.createSheet("sheet B", 1);

            // column and row titles
            sheetB.addCell(new Label(0, 0, "sheet B 1"));
            sheetB.addCell(new Label(1, 0, "sheet B 2"));
            sheetB.addCell(new Label(0, 1, "sheet B 3"));
            sheetB.addCell(new Label(1, 1, "sheet B 4"));
            */

            // close workbook
            workbook.write();
            workbook.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

                if (dates != null) {

                    // for each questionnaire
                    for (int index = 0; index < dates.size(); index++) {

                        // get questionnaire by date
                        DistressThermometerQuestionnaireManager db = new DistressThermometerQuestionnaireManager(ctx);
                        DistressThermometerQuestionnaire questionnaire = db.getDistressThermometerQuestionnaireByDate(user.getName(), dates.get(index));

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
}
