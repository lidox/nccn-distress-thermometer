package com.artursworld.nccn.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.ElasticRestClient;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.view.user.SelectUserActivity;
import com.artursworld.nccn.view.user.UserStartConfiguration;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String CLASS_NAME = StartMenu.class.getSimpleName();
    private Activity activity = null;
    private UserStartConfiguration configurationDialog = null;
    private User selectedUser = null;

    // UI
    @BindView(R.id.user_name_edit_text) MaterialEditText userNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        ButterKnife.bind(this);
        createUserIfNull();
        initNavigationAndToolBar();
        activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userName = setUserNameTextByGlobalValue();
        Log.i(CLASS_NAME, "Display selected user: " + userName);
        addOnUserNameTextChangeListener();

        ElasticRestClient restapi = new ElasticRestClient();
        //restapi.getHttpRequest("twitter/tweet/1");
        restapi.create("4");
    }


    /**
     * Rename user by name
     */
    private void addOnUserNameTextChangeListener() {
        userNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    Log.i(CLASS_NAME, "on focus out --> update user");
                    updateUserByEditText();
                }
            }
        });
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(CLASS_NAME, "on user name text changed --> update user");
                updateUserByEditText();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateUserByEditText() {
        createUserIfNull();

        if (selectedUser == null)
            selectedUser = new UserManager().getUserByName(Global.getSelectedUser());

        if(selectedUser != null){
            final String newName = userNameEditText.getText().toString();
            Log.i(CLASS_NAME, "rename user from '" + selectedUser.getName() + "' to '" + newName+"'");
            selectedUser.setName(newName);
            Log.i(CLASS_NAME, "user: " + selectedUser);
            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    long result = new UserManager().update(selectedUser);
                    if(result != 0)
                        Global.setSelectedUserName(newName);
                    return null;
                }
            }.execute();
        }
    }

    private void createUserIfNull() {
        if(Global.getSelectedUser() == null){
            String defaultUserName = Strings.getStringByRId(R.string.user_name);
            new UserManager().insertUser(new User(defaultUserName));
            selectedUser = new UserManager().getUserByName(defaultUserName);
            if(selectedUser != null){
                Global.setSelectedUserName(defaultUserName);
                Global.setHasToCreateNewUser(false);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_user_start_configuration) {
            Log.i(CLASS_NAME, "nav_user_start_configuration selected");
            if(configurationDialog == null)
                configurationDialog = new UserStartConfiguration(activity);
            configurationDialog.showConfigurationDialog();
        } else if (id == R.id.nav_user_statistics) {
            Log.i(CLASS_NAME, "Navigation item 'user_statistics' has been selected");
            Intent in = new Intent(activity, SelectUserActivity.class);
            in.putExtra(Strings.getStringByRId(R.string.c_is_selectuser_fragment), true);
            startActivity(in);
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
    }

    @Nullable
    private String setUserNameTextByGlobalValue() {
        final String userName = Global.getSelectedUser();
        if(userName != null){
            if(Global.hasToCreateNewUser()){
                userNameEditText.setText("");
            }
            else{
                userNameEditText.setText(userName);
            }
            selectedUser = new UserManager().getUserByName(userName);
        }
        else if(userName == null) {
            Log.i(CLASS_NAME, "user is null, so first time opened this app");
            String defaultUserName = Strings.getStringByRId(R.string.user_name);
            selectedUser = new UserManager().getUserByName(defaultUserName);
            if(selectedUser != null){
                Global.setSelectedUserName(defaultUserName);
                Global.setHasToCreateNewUser(false);
            }
        }

        return userName;
    }
}
