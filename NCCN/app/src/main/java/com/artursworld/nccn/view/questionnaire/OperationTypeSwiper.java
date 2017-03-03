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

    public OperationTypeSwiper(View rootView, int resourceId) {
        initSwipeSelector(rootView, resourceId);
        loadSwipeSelectionIfPossible();
        addOnSwipeItemChangeListener();

    }

    private void loadSwipeSelectionIfPossible() {
        new AsyncTask<Void, Void, MetaQuestionnaire>(){
            @Override
            protected MetaQuestionnaire doInBackground(Void... params) {
                Date creationDate = Global.getSelectedQuestionnaireDate();
                return new MetaQuestionnaireManager().getMetaDataByCreationDate(creationDate);
            }

            @Override
            protected void onPostExecute(MetaQuestionnaire meta) {
                super.onPostExecute(meta);
                if(meta != null)
                    swipeSelector.selectItemWithValue(meta.getOperationType());
            }
        }.execute();
    }

    private void addOnSwipeItemChangeListener() {
        swipeSelector.setOnItemSelectedListener(new OnSwipeItemSelectedListener() {
            @Override
            public void onItemSelected(final SwipeItem selectedOperationTypeItem) {
                new AsyncTask<Void, Void, Void>(){
                    @Override
                    protected Void doInBackground(Void... params) {
                        MetaQuestionnaire meta = getSelectedMetaQuestionnaire(selectedOperationTypeItem);
                        MetaQuestionnaireManager manager = new MetaQuestionnaireManager();
                        manager.update(meta);
                        return null;
                    }
                }.execute();
            }
        });
    }

    @NonNull
    private MetaQuestionnaire getSelectedMetaQuestionnaire(SwipeItem item) {
        Date selectedQuestionnaireDate = Global.getSelectedQuestionnaireDate();
        OperationType selectedOpType = (OperationType) item.value;
        MetaQuestionnaire meta = new MetaQuestionnaire();
        meta.setOperationType(selectedOpType);
        meta.setCreationDate(selectedQuestionnaireDate);
        Log.i(CLASS_NAME, "selected operation type: " + selectedOpType + " for date: " + selectedQuestionnaireDate);
        return meta;
    }

    private void initSwipeSelector(View rootView, int resourceId) {
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
