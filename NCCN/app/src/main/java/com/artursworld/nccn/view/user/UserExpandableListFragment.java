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
    private RecyclerView recyclerView = null;
    private List<User> filteredUsers = new ArrayList<>();
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
        return view;
    }

    private void initUI(View view) {
        searchEditText = (EditText) view.findViewById(R.id.search_edit_text);
        addOnSearchTextChangeListener();
        expandLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout);
        expandLayout.expand();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //TODO: dummy name?
        filteredUsers.add(new User("need this dummy"));
        adapter = new UserSearchRecyclerAdapter(filteredUsers, getActivity());
        recyclerView.setAdapter(adapter);
        this.rootView = view;
    }

    private void addOnSearchTextChangeListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getAllUsersAsyncAndFillUserList();
                expandLayout.expand();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fillUserList(View view, List<User> allUsers) {
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
        //Log.i(CLASS_NAME, "need to show list with this content: " + this.filteredUsers.toString());
        adapter.notifyDataSetChanged();
    }

    private void getAllUsersAsyncAndFillUserList() {
        new AsyncTask<Void, Void, List<User>>(){

            @Override
            protected List<User> doInBackground(Void... voids) {
                return new UserManager().getAllUsers();
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                fillUserList(rootView,users);
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(CLASS_NAME, "onResume()");
    }
}
