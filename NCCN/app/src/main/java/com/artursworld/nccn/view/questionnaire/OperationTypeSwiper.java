package com.artursworld.nccn.view.questionnaire;


import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.OperationType;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.MetaQuestionnaire;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import com.artursworld.nccn.model.persistence.manager.MetaQuestionnaireManager;
import com.roughike.swipeselector.OnSwipeItemSelectedListener;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import java.util.Date;

/**
 * Swiper Module to select the operation type. E.g. pre operation or post operation type
 */
public class OperationTypeSwiper {

    private String CLASS_NAME = OperationTypeSwiper.class.getSimpleName();
    private SwipeSelector swipeSelector;
    private int checkOnItemSelectedListener = 0;

    public OperationTypeSwiper(View rootView, int resourceId) {
        initSwipeSelector(rootView, resourceId);
        //TODO: async
        if(swipeSelector != null){
            OperationType type = getOperationType();
            if(type != null)
                swipeSelector.selectItemWithValue(type);
        }


        insertIfNowExists();


        if(swipeSelector != null)
            swipeSelector.setOnItemSelectedListener(getSwipeListener());
    }

    private OperationType getOperationType() {
        MetaQuestionnaire meta = new MetaQuestionnaireManager().getMetaDataByCreationDate(Global.getSelectedQuestionnaireDate());
        if(meta != null){
            OperationType operationType = meta.getOperationType();
            Log.i(CLASS_NAME, "set operation type selection to: " + operationType + ". It has been loaded from database");
            return operationType;
        }
            return null;
    }

    private void insertIfNowExists() {
        Date creationDate = Global.getSelectedQuestionnaireDate();
        MetaQuestionnaire meta = new MetaQuestionnaireManager().getMetaDataByCreationDate(creationDate);
        if (meta != null) {
            Log.i(CLASS_NAME, "create meta data, because not existing yet for creation date: " + EntityDbManager.dateFormat.format(creationDate));
            new MetaQuestionnaireManager().insert(new MetaQuestionnaire(creationDate));
        }
    }

    @NonNull
    private OnSwipeItemSelectedListener getSwipeListener() {
        return new OnSwipeItemSelectedListener() {
            @Override
            public void onItemSelected(final SwipeItem selectedOperationTypeItem) {
                Log.i(CLASS_NAME, "on click listener set selected new operation type in the UI: "+ selectedOperationTypeItem.value + ". CreationDate: " + Global.getSelectedQuestionnaireDate());
                MetaQuestionnaire meta = getSelectedMetaQuestionnaire(selectedOperationTypeItem);
                if(meta != null){
                    meta.setCreationDate(Global.getSelectedQuestionnaireDate());
                    MetaQuestionnaireManager manager = new MetaQuestionnaireManager();
                    manager.update(meta);
                }
            }
        };
    }

    /**
     * Get the selected type by UI
     * @param item
     * @return
     */
    @NonNull
    private MetaQuestionnaire getSelectedMetaQuestionnaire(SwipeItem item) {
        Log.i(CLASS_NAME, "get selected operation type in the UI: " + item.value + ". CreationDate: " + Global.getSelectedQuestionnaireDate());
        Date creationDate = Global.getSelectedQuestionnaireDate();
        OperationType selectedOpType = (OperationType) item.value;
        MetaQuestionnaire meta = new MetaQuestionnaireManager().getMetaDataByCreationDate(creationDate);
        if (meta == null) {
            Log.i(CLASS_NAME, "Could not find any meta data for creation date, so create a new one");
            meta = new MetaQuestionnaire(creationDate);
            meta.setOperationType(selectedOpType);
            new MetaQuestionnaireManager().insert(meta);
        } else {
            meta.setOperationType(selectedOpType);
            meta.setCreationDate(creationDate);
        }

        return meta;
    }

    private void initSwipeSelector(View rootView, int resourceId) {
        Log.i(CLASS_NAME, "init meta data swipe selector");
        if (rootView != null) {
            swipeSelector = (SwipeSelector) rootView.findViewById(resourceId);
            swipeSelector.setItems(
                    getSwipeItemsForOperationType()
            );
        }
    }

    @NonNull
    private SwipeItem[] getSwipeItemsForOperationType() {
        return new SwipeItem[]{new SwipeItem(OperationType.PRE, Strings.getStringByRId(R.string.pre_operation), Strings.getStringByRId(R.string.pre_operation_description)),
                new SwipeItem(OperationType.POST, Strings.getStringByRId(R.string.post_operation), Strings.getStringByRId(R.string.post_operation_description))};
    }

}
