package com.artursworld.nccn.controller.util;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.controller.export.ExcelExporter;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Date;


public class DatesTest extends InstrumentationTestCase {

    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Context targetContext = getInstrumentation().getTargetContext();
        context = new RenamingDelegatingContext(targetContext, "test_");
    }

    @Test
    public void testDate1() {

        String fileName = Dates.getFileNameDate(new Date());

        Assert.assertNotNull("check that file name is not null.", fileName);
    }

}
