package com.artursworld.nccn.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.elasticsearch.ElasticQuestionnaire;
import com.artursworld.nccn.controller.elasticsearch.ElasticRestClient;
import com.artursworld.nccn.controller.elasticsearch.METHOD;
import com.artursworld.nccn.controller.util.Generator;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.view.questionnaire.OperationTypeSwiper;
import com.artursworld.nccn.view.questionnaire.QuestionnaireSelectListFragment;
import com.artursworld.nccn.view.user.SelectUserActivity;
import com.artursworld.nccn.view.user.UserStartConfiguration;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String CLASS_NAME = StartMenu.class.getSimpleName();
    private Activity activity = null;
    private UserStartConfiguration configurationDialog = null;
    private OperationTypeSwiper operationTypeSwiper = null;

    // UI
    @BindView(R.id.user_name_edit_text)
    MaterialEditText userNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        ButterKnife.bind(this);
        initNavigationAndToolBar();
        activity = this;

        // init swipe for operation type e.g. pre-operation
        View rootView = getWindow().getDecorView().getRootView();
        if(operationTypeSwiper == null)
            operationTypeSwiper = new OperationTypeSwiper(rootView, R.id.select_operation_type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createUserBAndOnResumeWithSelectedUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        operationTypeSwiper = null;
    }

    private void onResumeWithSelectedUser() {
        userNameEditText.setText(Global.getSelectedUser());
        Log.i(CLASS_NAME, "Display global user(" + Global.getSelectedUser()+") in MaterialEditText");

        // add change listener
        addOnUserNameTextChangeListener();

        // init swipe for operation type e.g. pre-operation
        View rootView = getWindow().getDecorView().getRootView();

        addQuestionnaireListFragment(rootView);
    }

    private void addQuestionnaireListFragment(View rootView) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (rootView.findViewById(R.id.fragment_container) != null) {

            // Create a new Fragment to be placed in the activity layout
            QuestionnaireSelectListFragment fragment = new QuestionnaireSelectListFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        }
    }

    private void createUserBAndOnResumeWithSelectedUser() {
        //TODO: async
        if (Global.getSelectedUser() == null || Global.hasToCreateNewUser()) {
            createUser();
            onResumeWithSelectedUser();
            /*
            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... params) {
                    createUser();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    onResumeWithSelectedUser();
                }

            }.execute();
            */
        }
        else{
            onResumeWithSelectedUser();
        }

    }

    private void createUser() {
        String defaultUserName = Strings.getStringByRId(R.string.user) + Generator.getRandomInRange(0, 100000);
        Log.i(CLASS_NAME, "Creating new User("+defaultUserName+"), because no global user has been set");
        boolean hasCreated = new UserManager().insertUser(new User(defaultUserName));
        if(hasCreated){
            Global.setSelectedUserName(defaultUserName);
            Global.setHasToCreateNewUser(false);
        }
    }

    /**
     * Rename user by name
     */
    private void addOnUserNameTextChangeListener() {

        userNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(CLASS_NAME, "on user name text changed --> update user");
                final String oldName = Global.getSelectedUser();
                final String newName = userNameEditText.getText().toString();
                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        long result = new UserManager().renameUser(oldName, newName);
                        if (result != 0)
                            Global.setSelectedUserName(newName);
                        return null;
                    }
                }.execute();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_user_start_configuration) {
            Log.i(CLASS_NAME, "nav_user_start_configuration selected");
            if (configurationDialog == null)
                configurationDialog = new UserStartConfiguration(activity);
            configurationDialog.showConfigurationDialog();
        } else if (id == R.id.nav_user_statistics) {
            Log.i(CLASS_NAME, "Navigation item 'user_statistics' has been selected");
            Intent in = new Intent(activity, SelectUserActivity.class);
            in.putExtra(Strings.getStringByRId(R.string.c_is_selectuser_fragment), true);
            startActivity(in);
        } else if (id == R.id.nav_elastic_synchronisation) {
            Log.i(CLASS_NAME, "Start Synchronisation...");
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    String response = ElasticQuestionnaire.syncAll(activity.getApplicationContext());
                    Log.i(CLASS_NAME, "response: " + response);
                    return null;
                }
            }.execute();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Initializes the Navigation and Toolbar
     */
    private void initNavigationAndToolBar() {
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Toggleable navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_user_start_configuration).setChecked(false);
        navigationView.getMenu().findItem(R.id.nav_user_statistics).setChecked(false);
        navigationView.setNavigationItemSelectedListener(this);

        initSelectedUserAndDateInNavigation();
    }

    private void initSelectedUserAndDateInNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        TextView selectedUserText = (TextView) hView.findViewById(R.id.menu_selected_user);
        TextView selectedCreationDate = (TextView) hView.findViewById(R.id.menu_creation_date);

        if(selectedUserText!=null)
            selectedUserText.setText(Global.getSelectedUser());

        if(selectedCreationDate!=null) {
            Date selectedQuestionnaireDate = Global.getSelectedQuestionnaireDate();
            if(selectedCreationDate!=null)
                selectedCreationDate.setText(EntityDbManager.dateFormat.format(selectedQuestionnaireDate));
        }
    }
}
