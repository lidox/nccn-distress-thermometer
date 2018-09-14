package com.artursworld.nccn.controller.elasticsearch;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v4.util.Pair;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.controller.util.Dates;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Questionnairy;
import com.artursworld.nccn.controller.util.Security;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.DistressThermometerQuestionnaire;
import com.artursworld.nccn.model.entity.FearOfProgressionQuestionnaire;
import com.artursworld.nccn.model.entity.HADSDQuestionnaire;
import com.artursworld.nccn.model.entity.MetaQuestionnaire;
import com.artursworld.nccn.model.entity.PsychoSocialSupportState;
import com.artursworld.nccn.model.entity.QolQuestionnaire;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.DistressThermometerQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import com.artursworld.nccn.model.persistence.manager.FearOfProgressionManager;
import com.artursworld.nccn.model.persistence.manager.HADSDQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.MetaQuestionnaireManager;
import com.artursworld.nccn.model.persistence.manager.QualityOfLifeManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ElasticQuestionnaire {

    private static final String CLASS_NAME = ElasticQuestionnaire.class.getSimpleName();

    /**
     * Make a HTTP GET request to elastic search
     *
     * @return the requests response
     */
    public static String getAllRecords() {
        Log.i(CLASS_NAME, "getAllRecords");
        final String apiString = "_search?pretty=true&q=*:*";
        return ElasticRestClient.get(getIndex(), getType(), apiString, "{ \"size\" : \"1000\"}");
    }


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
        return ElasticRestClient.post("you-can-let", "this-here", apiString, updateBulkAsString);
    }

    /**
     * Getting ElasticSearch Index by Preferences
     *
     * @return the ElasticSearch Index
     */
    private static String getIndex() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String defaultValue = Strings.getStringByRId(R.string.c_elastic_search_index_name_default_value);
        String value = Strings.getStringByRId(R.string.c_select_elastic_search_index_name);
        String index = sp.getString(value, defaultValue);
        return index;
    }

    /**
     * Getting ElasticSearch Type by Preferences
     *
     * @return the ElasticSearch Type
     */
    public static String getType() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String defaultValue = Strings.getStringByRId(R.string.c_elastic_search_type_name_default_value);
        String value = Strings.getStringByRId(R.string.c_select_elastic_search_type_name);
        String type = sp.getString(value, defaultValue);
        return type;
    }

    /**
     * Synchronizes all questionnaire for all user in the list with elastic search
     * For each user a bulk will be created and sent to the elastic search
     *
     * @param ctx      the application context
     * @param userList the list of users to synchronize
     * @return the response
     */
    public static String syncAll(final Context ctx, final List<User> userList) {
        final List<String> responseList = new ArrayList<>();


        // open dialog
        MaterialDialog.Builder b = new MaterialDialog.Builder(ctx)
                .title(R.string.synchronisation)
                .customView(R.layout.dialog_sync_elasticsearch, true);
        final MaterialDialog dialog = b.show();

        if (dialog != null) {
            final AnimatedCircleLoadingView animatedCircleLoadingView = (AnimatedCircleLoadingView) dialog.getView().findViewById(R.id.circle_loading_view);
            if (animatedCircleLoadingView != null) {
                animatedCircleLoadingView.startDeterminate();

                new AsyncTask<Void, Double, List<String>>() {
                    @Override
                    protected List<String> doInBackground(Void... params) {

                        List<User> userList = new UserManager(ctx).getAllUsers();
                        double userCount = userList.size();
                        double userLoadedCount = 0;
                        for (User user : userList) {
                            StringBuilder bulk = new StringBuilder();
                            bulk.append(getUpsertBulkByUser(ctx, user));
                            String upsertCommand = bulk.toString();
                            boolean isBulkEmpty = upsertCommand.equals("");
                            if (!isBulkEmpty) {
                                Log.i(CLASS_NAME, "Fire upsert: " + upsertCommand);
                                String singleResponse = ElasticQuestionnaire.bulk(upsertCommand);
                                responseList.add(singleResponse);
                                if (userCount > 0) {
                                    userLoadedCount++;
                                    double progress = userLoadedCount / userCount * 100;
                                    publishProgress(progress);
                                }
                            }
                        }
                        return responseList;
                    }


                    @Override
                    protected void onProgressUpdate(Double... progress) {
                        int progressValue = (int) Math.floor(progress[0]);
                        Log.i(CLASS_NAME, "progess: " + progressValue + "%");
                        animatedCircleLoadingView.setPercent(progressValue);
                    }

                    @Override
                    protected void onPostExecute(List<String> resultList) {
                        Log.i(CLASS_NAME, "finished: " + resultList);
                        boolean hasErrors = false;
                        for (String userResponse : resultList) {
                            try {
                                JSONObject a = new JSONObject(userResponse);
                                if (a != null)
                                    hasErrors = a.getBoolean("errors");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (hasErrors) {
                            animatedCircleLoadingView.stopFailure();
                        } else {
                            animatedCircleLoadingView.stopOk();
                        }

                        // dialog.dismiss();
                    }
                }.execute();
            }
        }


        //ALGORITHM:
        // for each user
        // for each questionnaire
        // update
        // if could not update: create
        return responseList.toString();
    }

    /**
     * Generates an 'upsert' (update/insert) bulk String for a specific user
     *
     * @param ctx  the database context
     * @param user the user
     * @return a bulk containing all upsert information
     */
    private static String getUpsertBulkByUser(Context ctx, User user) {
        StringBuilder bulk = new StringBuilder();
        List<Date> dates = new UserManager().getQuestionnaireDateListByUserName(user.getName());
        if (dates != null) {
            for (Date date : dates) {
                JSONObject params = new JSONObject();
                try {
                    MetaQuestionnaire meta = new MetaQuestionnaireManager().getMetaDataByCreationDate(date);
                    if (meta != null) {
                        params.put("operation-type", meta.getOperationType());
                        PsychoSocialSupportState psychoSocialSupportState = meta.getPsychoSocialSupportState();
                        params.put("need-psychosocial-support", psychoSocialSupportState == PsychoSocialSupportState.ACCEPTED);
                        params.put("had-psychosocial-support", meta.getPastPsychoSocialSupportState());
                    }
                    params.put("creation-date", EntityDbManager.dateFormat.format(date));
                    String userName = Security.getUserNameByEncryption(user);
                    params.put("user-name", userName);

                } catch (Exception e) {
                    Log.e(CLASS_NAME, e.getLocalizedMessage());
                }


                DistressThermometerQuestionnaire distressThermometerQuestionnaire = new DistressThermometerQuestionnaireManager().getDistressThermometerQuestionnaireByDate(user.getName(), date);
                if (distressThermometerQuestionnaire != null) {
                    if (Questionnairy.canStatisticsBeDisplayed(distressThermometerQuestionnaire.getProgressInPercent()))
                        params = addAllKeyValuePairs(distressThermometerQuestionnaire.getAsJSON(), params);
                }


                QolQuestionnaire qol = new QualityOfLifeManager().getQolQuestionnaireByDate(user.getName(), date);
                if (qol != null) {
                    if (Questionnairy.canStatisticsBeDisplayed(qol.getProgressInPercent())) {
                        if (qol.getGlobalHealthScore() > 0) {
                            params = addAllKeyValuePairs(qol.getQLQC30AsJSON(), params);
                            params = addAllKeyValuePairs(qol.getBN20AsJSON(), params);
                        }
                    }
                }


                HADSDQuestionnaire hads = new HADSDQuestionnaireManager().getHADSDQuestionnaireByDate_PK(user.getName(), date);
                if (hads != null) {
                    if (Questionnairy.canStatisticsBeDisplayed(hads.getProgressInPercent()))
                        if(hads.getAnxietyScore() > 0 ){
                            params = addAllKeyValuePairs(hads.getAsJSON(), params);
                        }
                }


                FearOfProgressionQuestionnaire fearQuestionnaire = new FearOfProgressionManager().getQuestionnaireByDate(user.getName(), date);
                if (fearQuestionnaire != null) {
                    if (Questionnairy.canStatisticsBeDisplayed(fearQuestionnaire.getProgressInPercent()))
                        if (fearQuestionnaire.getScore() >= 12 && fearQuestionnaire.getScore() <=60) {
                            params = addAllKeyValuePairs(fearQuestionnaire.getAsJSON(), params);
                        }

                }


                bulk.append(ElasticQuestionnaire.getGenericBulk(date, getType(), params.toString()));
            }

        }
        return bulk.toString();
    }

    /**
     * Adds all key-value pairs from source into destination
     *
     * @param source      the source JSON object
     * @param destination the destination JSON object
     * @return a JSON object containing key-value pairs of both source and destination
     */
    private static JSONObject addAllKeyValuePairs(JSONObject source, JSONObject destination) {
        Iterator<?> keys = source.keys();
        while (keys.hasNext()) {
            try {
                String key = (String) keys.next();
                String value = source.get(key).toString();
                destination.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return destination;
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
        bulk.append("{ \"update\" : {\"_id\" : \"" + questionnaireId + "\", \"_type\" : \"" + ES_TYPE + "\", \"_index\" : \"" + getIndex() + "\"} }" + "\n");
        bulk.append("{ \"doc\" : " + jsonValues + ", \"doc_as_upsert\" : true }" + "\n");
        return bulk.toString();
    }
}