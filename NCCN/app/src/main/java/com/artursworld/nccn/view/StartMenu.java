package com.artursworld.nccn.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.config.App;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.view.user.UserStartConfiguration;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String CLASS_NAME = StartMenu.class.getSimpleName();
    private Activity activity = null;
    private UserStartConfiguration configurationDialog = null;

    // UI
    @BindView(R.id.user_name_edit_text) MaterialEditText userNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        ButterKnife.bind(this);
        initNavigationAndToolBar();
        activity = this;
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
            Log.i(CLASS_NAME, "nav_user_statistics selected");
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
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userName = Global.getSelectedUser();
        if(userName != null)
            userNameEditText.setText(userName);

        Log.i(CLASS_NAME, "Display selected user: " + userName);
    }
}
