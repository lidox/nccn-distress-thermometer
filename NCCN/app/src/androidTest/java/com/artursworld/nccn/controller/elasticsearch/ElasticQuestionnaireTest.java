package com.artursworld.nccn.controller.elasticsearch;


import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import org.junit.Test;

public class ElasticQuestionnaireTest extends InstrumentationTestCase {

    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void testSyncAll(){
        ElasticQuestionnaire.syncAll(context);
    }
}
