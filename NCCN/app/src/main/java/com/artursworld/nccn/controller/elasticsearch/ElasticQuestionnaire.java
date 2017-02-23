package com.artursworld.nccn.controller.elasticsearch;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;

import com.artursworld.nccn.controller.util.Dates;
import com.artursworld.nccn.controller.util.Security;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class ElasticQuestionnaire {

    private static final String CLASS_NAME = ElasticQuestionnaire.class.getSimpleName();

    public void post(final String apiString, final Pair<String, String>... pairs) {
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
                ElasticRestClient.postOrPut(METHOD.POST, apiString, params);
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
    public static String update(final String apiString, final Pair<String, String>... pairs) {
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

    /**
     * POST /customer/external/1/_update?pretty
     * {
     * "doc": { "name": "Jane Doe" }
     * }
     */
    public static String update(final String apiString, final JSONObject params) {
        return updateByJSON(apiString, params);
    }

    @Nullable
    private static String updateByJSON(String apiString, JSONObject params) {
        String response = null;
        try {
            JSONObject docObject = new JSONObject();
            docObject.put("doc", params);
            response = ElasticRestClient.postOrPut(METHOD.POST, apiString, docObject);
        } catch (JSONException e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        } finally {
            return response;
        }
    }

    //TODO: not implemented yet

    /**
     * Synchronizes all questionnaire for all user with elastic search
     */
    public static void syncAll(Context ctx) {
        //ALGORITHM:
        // for each user
        // for each questionnaire
        // update
        // if could not update: create
        List<User> userList = new UserManager(ctx).getAllUsers();
        for (User user : userList) {
            List<Date> dates = new UserManager().getQuestionnaireDateListByUserName(user.getName());
            for (Date date : dates) {
                //Log.i(CLASS_NAME, "user '" + user.getName() + "' date:" + date.toString());
                //new A
                DistressThermometerQuestionnaire distressQuestionnaire = new DistressThermometerQuestionnaireManager(ctx).getDistressThermometerQuestionnaireByDate(user.getName(), date);
                //Log.i(CLASS_NAME, distressQuestionnaire.toString());
                // TODO: Make bulk update
                String idByMD5  = Security.getMD5ByString(distressQuestionnaire.getCreationDate_PK().toString());
                JSONObject distressQuestionnaireAsJSON = distressQuestionnaire.getAsJSON(user.getCreationDate());
                String response = ElasticQuestionnaire.update(idByMD5 +"/_update?pretty", distressQuestionnaireAsJSON);
                Log.i(CLASS_NAME, "" + response);
            }

        }
    }
}