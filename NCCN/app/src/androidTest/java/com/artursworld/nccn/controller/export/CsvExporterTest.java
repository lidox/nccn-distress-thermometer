package com.artursworld.nccn.controller.export;

import android.content.Context;
import android.support.annotation.NonNull;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.artursworld.nccn.TestUtils;
import com.artursworld.nccn.controller.util.Bits;
import com.artursworld.nccn.controller.util.Generator;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.Gender;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Date;
import java.util.List;


public class CsvExporterTest extends InstrumentationTestCase {

    private RenamingDelegatingContext context;
    private static String CLASS_NAME = CsvExporterTest.class.getSimpleName();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Context targetContext = getInstrumentation().getTargetContext();
        context = new RenamingDelegatingContext(targetContext, "test_");
    }

    @Test
    public void testContext() {
        if (context != null)
            Log.d(CLASS_NAME, "hahaha this test works like a charm! :)");

        Assert.assertNotNull("test if context works like a charm", context);
    }

    @Test
    public void testCreateUserAndQuestionnairesForExport() {

        // create a user
        User user = TestUtils.createUser(context);

        // create random amount of HADS-D questionnaires
        double hadsdCount = Generator.getRandomInRange(1, 100);
        for (int i = 0; i < hadsdCount; i++) {
            TestUtils.createHadsdByUser(user, context);
        }

        // create random amount of Distress Thermometer questionnaires
        double distressThermometerCount = Generator.getRandomInRange(1, 100);
        for (int i = 0; i < distressThermometerCount; i++) {
            TestUtils.createDistressThermometer(user, context);
        }

        // create random amount of 'quality of life' questionnaires
        double qualityOfLifeCount = Generator.getRandomInRange(1, 100);
        for (int i = 0; i < qualityOfLifeCount; i++) {
            TestUtils.createQualityOfLifeQuestionnaires(user, context);
        }


        // make check if database contains questionnaires
        HADSDQuestionnaireManager db = new HADSDQuestionnaireManager(context);
        List<HADSDQuestionnaire> list = db.getHadsdQuestionnaireListByUserName(user.getName());
        Assert.assertTrue("check if list contains elements", list.size() > 0);

        List<String[]> testList = CsvExporter.getExportableCsvListByUser(user, context);
        assertTrue(testList != null);
    }

}
