package com.artursworld.nccn.model.persistence.manager;


import android.content.Context;

import com.artursworld.nccn.controller.config.App;

public class DistressThermometerQuestionnaireManager extends EntityDbManager {

    public DistressThermometerQuestionnaireManager() {
        super(App.getAppContext());
    }

    /**
     * This constructor is used for unit tests
     * @param context the database context to use
     */
    DistressThermometerQuestionnaireManager(Context context) {
        super(context);
    }

}
