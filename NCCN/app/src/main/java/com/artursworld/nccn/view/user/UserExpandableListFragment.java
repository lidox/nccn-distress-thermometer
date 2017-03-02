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
import android.widget.EditText;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class UserExpandableListFragment extends Fragment {

    private static final String CLASS_NAME = UserExpandableListFragment.class.getSimpleName();

    // UI
    private ExpandableRelativeLayout expandLayout;
    private View rootView = null;
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

        rootView = initUI(view);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(CLASS_NAME, "onResume()");

        initRecyclerView();

        addOnSearchTextChangeListener();
        loadUsersIntoViewAsync();
    }

    private void checkWhereFragmentIsDisplayed() {
        try {
            Bundle bundle = getActivity().getIntent().getExtras();
            boolean isVisibleInSelectUserFragment = bundle.getBoolean(Strings.getStringByRId(R.string.c_is_selectuser_fragment), false);
            Log.i(CLASS_NAME, CLASS_NAME + " is visible in 'Select User' = " + isVisibleInSelectUserFragment);
            if (adapter != null)
                adapter.setHasToOpenStatistics(isVisibleInSelectUserFragment);
        } catch (Exception e) {
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }
    }

    private View initUI(View view) {
        Log.i(CLASS_NAME, "init UI");
        searchEditText = (EditText) view.findViewById(R.id.search_edit_text);
        expandLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout);
        expandLayout.expand();
        return view;
    }

    private void initRecyclerView() {
        Log.i(CLASS_NAME, "init recycler view");
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setUserDisplayCount(10);
        adapter = new UserSearchRecyclerAdapter(filteredUsers, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        checkWhereFragmentIsDisplayed();
    }

    private void addOnSearchTextChangeListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fillUserList();
                expandLayout.expand();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void fillUserList() {
        searchEditText = (EditText) rootView.findViewById(R.id.search_edit_text);
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
        adapter.notifyDataSetChanged();
    }

    private void loadUsersIntoViewAsync() {
        new AsyncTask<Void, Void, List<User>>() {

            @Override
            protected List<User> doInBackground(Void... voids) {
                if (allUsers.size() <= 0)
                    return new UserManager().getAllUsers();
                else
                    return allUsers;
            }

            @Override
            protected void onPostExecute(List<User> users) {
                Log.i(CLASS_NAME, "users has been loaded, now fill list. user count = " + users.size());
                super.onPostExecute(users);
                allUsers = users;

                Log.i(CLASS_NAME, "insert users from all user list");

                for (int j = 0; j < allUsers.size(); j++) {
                    if (allUsers.size() > j)
                        filteredUsers.add(allUsers.get(j));
                }
                expandLayout.expand();
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public void setUserDisplayCount(int userDisplayCount) {
        for (int i = 0; i < userDisplayCount; i++)
            filteredUsers.add(new User(" "));
    }
}
