package com.artursworld.nccn.controller.elasticsearch;


import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;

import com.artursworld.nccn.controller.util.Dates;
import com.artursworld.nccn.controller.util.Security;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class ElasticQuestionnaire {

    private static final String CLASS_NAME = ElasticQuestionnaire.class.getSimpleName();
    public static final String ES_INDEX = "questionnaire-app";
    private static final String ES_TYPE = "tweet";

    /**
     * Does a bulk operation to be faster
     *
     * @param updateBulkAsString contains all operations forming a big bulk
     * @return the requests response
     */
    public static String bulk(String updateBulkAsString) {
        /**
         * This is a BULK could look like
         * { "update" : {"_id" : "2", "_type" : "tweet", "_index" : "twitter"} }
         * { "doc" : {"field" : "value"}, "doc_as_upsert" : true }
         * { "update" : {"_id" : "3", "_type" : "tweet", "_index" : "twitter"} }
         * { "doc" : {"field" : "value"}, "doc_as_upsert" : true }
         * { "update" : {"_id" : "4", "_type" : "tweet", "_index" : "twitter"} }
         * { "doc" : {"field" : "value"}, "doc_as_upsert" : true }
         */
        final String apiString = "_bulk";
        return ElasticRestClient.post("can-let", "this-here", apiString, updateBulkAsString);
    }

    public void post(final String es_type, final String apiString, final Pair<String, String>... pairs) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... something) {
                JSONObject params = new JSONObject();
                for (Pair<String, String> pair : pairs) {
                    try {
                        params.put(pair.first, pair.second);
                    } catch (JSONException e) {
                        Log.e(CLASS_NAME, e.getLocalizedMessage());
                    }
                }
                ElasticRestClient.post(ES_INDEX, es_type, apiString, params);
                return null;
            }
        }.execute();
    }

    /**
     * POST /customer/external/1/_update?pretty
     * {
     * "doc": { "name": "Jane Doe" }
     * }
     */
    /**
     public static String update(final String apiString, final Pair<String, String>... pairs) {
     //TODO: check this implementation
     JSONObject params = new JSONObject();
     for (Pair<String, String> pair : pairs) {
     try {
     params.put(pair.first, pair.second);
     } catch (JSONException e) {
     Log.e(CLASS_NAME, e.getLocalizedMessage());
     }
     }
     return updateByJSON(apiString, params);
     }
     **/

    /**
     * POST /customer/external/1/_update?pretty
     * {
     * "doc": { "name": "Jane Doe" }
     * }
     */
    //TODO: check this implementation
    public static String update(final String es_type, final String apiString, final JSONObject params) {
        return updateByJSON(es_type, apiString, params);
    }

    @Nullable
    private static String updateByJSON(String es_type, String apiString, JSONObject params) {
        //TODO: not implemented yet
        String response = null;
        try {
            JSONObject docObject = new JSONObject();
            docObject.put("doc", params);
            response = ElasticRestClient.post(ES_INDEX, es_type, apiString, docObject);
        } catch (JSONException e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        } finally {
            return response;
        }
    }


    /**
     * Synchronizes all questionnaire for all user with elastic search
     * For each user a bulk will be created and sent to the elastic search
     */
    public static void syncAll(Context ctx) {
        //ALGORITHM:
        // for each user
        // for each questionnaire
        // update
        // if could not update: create
        List<User> userList = new UserManager(ctx).getAllUsers();
        for (User user : userList) {
            StringBuilder bulk = new StringBuilder();
            bulk.append(getUpsertBulkByUser(ctx, user));
            ElasticQuestionnaire.bulk(bulk.toString());
        }
    }

    /**
     * Generates an 'upsert' (update/insert) bulk String for a specific user
     * @param ctx the database context
     * @param user the user
     * @return a bulk containing all upsert information
     */
    private static String getUpsertBulkByUser(Context ctx, User user) {
        StringBuilder bulk = new StringBuilder();
        List<Date> dates = new UserManager().getQuestionnaireDateListByUserName(user.getName());
        for (Date date : dates) {
            DistressThermometerQuestionnaire thermo = new DistressThermometerQuestionnaireManager(ctx).getDistressThermometerQuestionnaireByDate(user.getName(), date);
            bulk.append(thermo.getBulk());

            QolQuestionnaire qol = new QualityOfLifeManager(ctx).getQolQuestionnaireByDate(user.getName(), date);
            bulk.append(qol.getBulkQLQC30());
            bulk.append(qol.getBulkBN20());

            HADSDQuestionnaire hads = new HADSDQuestionnaireManager(ctx).getHADSDQuestionnaireByDate_PK(user.getName(), date);
            bulk.append(hads.getBulk());
        }
        return bulk.toString();
    }

    /**
     * Generates a upsert bulk for a specific questionnaire
     *
     * @param creationDate the creation date of the questionnaire
     * @param ES_TYPE      the type of the questionnaire e.g. distress-thermometer
     * @param jsonValues   contains parameters e.g. the scores and meta data
     * @return a bulk representation for elastic search
     */
    @NonNull
    public static String getGenericBulk(Date creationDate, String ES_TYPE, String jsonValues) {
        /* E.g. of a bulk
        { "update" : {"_id" : "3", "_type" : "tweet", "_index" : "twitter"} }
        { "doc" : {"field" : "value"}, "doc_as_upsert" : true }
        * */
        StringBuilder bulk = new StringBuilder();
        long questionnaireId = Dates.getLongByDate(creationDate);
        bulk.append("{ \"update\" : {\"_id\" : \"" + questionnaireId + "\", \"_type\" : \"" + ES_TYPE + "\", \"_index\" : \"" + ES_INDEX + "\"} }" + "\n");
        bulk.append("{ \"doc\" : " + jsonValues + ", \"doc_as_upsert\" : true }" + "\n");
        return bulk.toString();
    }
}