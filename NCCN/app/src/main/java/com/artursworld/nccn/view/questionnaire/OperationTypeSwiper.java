package com.artursworld.nccn.view.questionnaire;


import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.OperationType;
import com.artursworld.nccn.controller.util.Strings;
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
        addOnSwipeItemChangeListener();
    }

    private void addOnSwipeItemChangeListener() {
        swipeSelector.setOnItemSelectedListener(new OnSwipeItemSelectedListener() {
            @Override
            public void onItemSelected(SwipeItem item) {
                Date selectedQuestionnaireDate = Global.getSelectedQuestionnaireDate();
                //TODO: update test type by creationDate
                Log.i(CLASS_NAME, "selected test type: " + item.value + " for date: " + selectedQuestionnaireDate);
                if (OperationType.PRE == item.value) {
                    //TODO: update test type by creationDate
                } else if (OperationType.POST == item.value) {
                    //TODO: update test type by creationDate
                }
            }
        });
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
