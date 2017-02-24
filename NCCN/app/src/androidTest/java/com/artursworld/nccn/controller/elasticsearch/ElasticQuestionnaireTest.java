package com.artursworld.nccn.controller.elasticsearch;


import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.artursworld.nccn.controller.util.Dates;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Date;

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
    public void testBulk1(){
        StringBuilder b = new StringBuilder();
        /**
         * { "update" : {"_id" : "2", "_type" : "tweet", "_index" : "twitter"} }
         * { "doc" : {"field" : "value"}, "doc_as_upsert" : true }
         * { "update" : {"_id" : "3", "_type" : "tweet", "_index" : "twitter"} }
         * { "doc" : {"field" : "value"}, "doc_as_upsert" : true }
         * { "update" : {"_id" : "4", "_type" : "tweet", "_index" : "twitter"} }
         * { "doc" : {"field" : "value"}, "doc_as_upsert" : true }
         */
        b.append("{ \"update\" : {\"_id\" : \"21\", \"_type\" : \"tweet\", \"_index\" : \"twitter\"} }" + "\n");
        b.append("{ \"doc\" : {\"field\" : \"value22\"}, \"doc_as_upsert\" : true }"+ "\n");
        b.append("{ \"update\" : {\"_id\" : \"22\", \"_type\" : \"tweet\", \"_index\" : \"twitter\"} }"+ "\n");
        b.append("{ \"doc\" : {\"field\" : \"value22\"}, \"doc_as_upsert\" : true }"+ "\n");
        b.append("{ \"update\" : {\"_id\" : \"1\", \"_type\" : \"tweet\", \"_index\" : \"twitter\"} }"+ "\n");
        b.append("{ \"doc\" : {\"super\" : \"value23\"}, \"doc_as_upsert\" : true }"+ "\n");
        //b.append(""+ "\n");
        ElasticQuestionnaire.bulk(b.toString());
    }


    @Test
    public void testParseDateToLongAndViceVersa(){
        Date newDate = new Date();
        long newDateAsLong = newDate.getTime();
        Date secondDate = new Date(newDateAsLong);
        //Date three = Dates.getDateByNumber(Dates.getLongByDate(newDate));
        assertEquals("Check how to get Date by number and vice versa",newDate.toString(), secondDate.toString());
    }

    @Test
    public void testShouldBe(){
        String jsonValues = "{\"field\" : \"value\"}";
        DistressThermometerQuestionnaire bla = new DistressThermometerQuestionnaire("Merkel");
        String iss = bla.getAsJSON().toString();
        int i = 0;
        //assertEquals("Check how to get Date by number and vice versa",newDate.toString(), secondDate.toString());
    }



    @Test
    public void testSyncAll(){
        ElasticQuestionnaire.syncAll(context);
    }
}
