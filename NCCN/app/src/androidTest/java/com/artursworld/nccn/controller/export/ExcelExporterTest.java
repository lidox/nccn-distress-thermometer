package com.artursworld.nccn.controller.export;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.artursworld.nccn.TestUtils;
import com.artursworld.nccn.controller.util.Generator;
import com.artursworld.nccn.model.entity.User;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.biff.WritableWorkbookImpl;


public class ExcelExporterTest extends InstrumentationTestCase {

    private RenamingDelegatingContext context;
    private static String CLASS_NAME = ExcelExporter.class.getSimpleName();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Context targetContext = getInstrumentation().getTargetContext();
        context = new RenamingDelegatingContext(targetContext, "test_");
    }

    @Test
    public void testContext() {
        if (context != null)
            Log.d(CLASS_NAME, "muhaha! This test context works like a charm. :)");

        ExcelExporter exporter = new ExcelExporter();
        exporter.export();

        Assert.assertNotNull("check if context works like a charm.", context);
    }

    @Test
    public void testgetDistressThermometerWorksheet() {

        if (context != null)
            Log.d(CLASS_NAME, "muhaha! This test context works like a charm. :)");


        // create a user
        User user = TestUtils.createUser(context);

        // create random amount of Distress Thermometer questionnaires
        double distressThermometerCount = Generator.getRandomInRange(1, 100);
        for (int i = 0; i < distressThermometerCount; i++) {
            TestUtils.createDistressThermometer(user, context);
        }

        try {


            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));

            // excel sheet
            WritableWorkbook workbook = new WritableWorkbookImpl(new OutputStream() {
                @Override
                public void write(int b) throws IOException {

                }
            }, true, wbSettings);
            WritableSheet sheet = workbook.createSheet("Distress Worksheet", 0);
            sheet = ExcelExporter.getDistressThermometerWorksheet(user, sheet, context);
            Assert.assertEquals("check sheet size", (int) distressThermometerCount, sheet.getRows());

        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void testExport1() {

        if (context != null)
            Log.d(CLASS_NAME, "muhaha! This test context works like a charm. :)");


        // create a user
        User user = TestUtils.createUser(context);

        // create random amount of Distress Thermometer questionnaires
        double distressThermometerCount = Generator.getRandomInRange(1, 100);
        for (int i = 0; i < distressThermometerCount; i++) {
            TestUtils.createDistressThermometer(user, context);
        }

        double qolCount = Generator.getRandomInRange(1, 100);
        for (int i = 0; i < qolCount; i++) {
            TestUtils.createQualityOfLifeQuestionnaires(user, context);
        }


        double hadsdCount = Generator.getRandomInRange(1, 100);
        for (int i = 0; i < hadsdCount; i++) {
            TestUtils.createHadsdByUser(user, context);
        }

        ExcelExporter.export();

    }

}
