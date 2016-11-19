package com.artursworld.nccn.controller.util;


import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

public class Questionnairy {

    /**
     * Generates a list of questions including its possible answers.
     * The qustions a coming from a resource file
     * @param resourceId the resource id of the file containing the questionnairy
     * @param ctx the app context to be able to access resources
     * @return a list of questions including its possible answers
     */
    public static List<TypedArray> getQuestionnairyListById(int resourceId, Context ctx) {
        List<TypedArray> questionAnswerList = new ArrayList<TypedArray>();
        TypedArray menuResources = ctx.getResources().obtainTypedArray(resourceId);
        TypedArray questionItem;
        for (int i = 0; i < menuResources.length(); i++) {
            int resId = menuResources.getResourceId(i, -1);
            if (resId < 0) {
                continue;
            }
            questionItem = ctx.getResources().obtainTypedArray(resId);
            questionAnswerList.add(questionItem);
        }
        return questionAnswerList;
    }

}
