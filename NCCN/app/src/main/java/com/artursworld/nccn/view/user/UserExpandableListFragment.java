package com.artursworld.nccn.view.user;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class UserExpandableListFragment extends Fragment {

    private static final String CLASS_NAME = UserExpandableListFragment.class.getSimpleName();
    private boolean isVisibleInSelectUserFragment = false;

    // UI
    private ExpandableRelativeLayout expandLayout;
    private View rootView = null;
    private RecyclerView recyclerView = null;
    private List<User> filteredUsers = new ArrayList<>();
    private List<User> allUsers = new ArrayList<>();
    private UserSearchRecyclerAdapter adapter = null;
    private EditText searchEditText = null;

    public UserExpandableListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(CLASS_NAME, " onCreateView()");

        View view = inflater.inflate(R.layout.fragment_user_expandable_search_list, container, false);

        initUI(view);

        checkWhereFragmentIsDisplayed();

        return view;
    }

    private void checkWhereFragmentIsDisplayed() {
        Bundle bundle = getActivity().getIntent().getExtras();
        isVisibleInSelectUserFragment = bundle.getBoolean(Strings.getStringByRId(R.string.c_is_selectuser_fragment), false);
        Log.i(CLASS_NAME, CLASS_NAME  + " is visible in 'Select User' = " + isVisibleInSelectUserFragment);
    }

    private void initUI(View view) {
        searchEditText = (EditText) view.findViewById(R.id.search_edit_text);
        addOnSearchTextChangeListener();
        expandLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout);
        expandLayout.expand();
        initUserList(view);
        this.rootView = view;
    }

    private void initUserList(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.i(CLASS_NAME, "first load of all users");
        loadAllUsers(true);
    }

    private void addOnSearchTextChangeListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasToFillUserList = true;
                //getAllUsersAsyncAndFillUserList(hasToFillUserList, false);
                expandLayout.expand();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fillUserList(View view) {
        searchEditText = (EditText) view.findViewById(R.id.search_edit_text);
        filteredUsers.clear();
        String searchedText = searchEditText.getText().toString();
        for (User user : allUsers) {
            if (user.getName() != null) {
                boolean containsSearchText = Pattern.compile(searchedText, Pattern.CASE_INSENSITIVE + Pattern.LITERAL).matcher(user.toString()).find();
                if (containsSearchText) {
                    filteredUsers.add(user);
                }
            }
        }
        if(adapter!= null)
         adapter.notifyDataSetChanged();
    }

    private void loadAllUsers(final boolean hasToFillUserList) {
        new AsyncTask<Void, Void, List<User>>() {

            @Override
            protected List<User> doInBackground(Void... voids) {
                if(allUsers.size() <= 0)
                    return new UserManager().getAllUsers();

                return allUsers;
            }

            @Override
            protected void onPostExecute(List<User> users) {
                Log.i(CLASS_NAME, "users has been loaded, now fill list");
                super.onPostExecute(users);
                allUsers = users;

                Log.i(CLASS_NAME, "insert users from all user list");
                for(int j = 0; j < 5; j++){
                    if(allUsers.size() > j)
                        filteredUsers.add(allUsers.get(j));
                }

                adapter = new UserSearchRecyclerAdapter(filteredUsers, getActivity());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(CLASS_NAME, "onResume()");

        if(searchEditText != null)
            if(Global.getSelectedUser() != null)
                searchEditText.setText(Global.getSelectedUser());

    }
}
