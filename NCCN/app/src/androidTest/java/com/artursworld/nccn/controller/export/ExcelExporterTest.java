package com.artursworld.nccn.controller.export;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Test;


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
}
