package com.artursworld.nccn.view.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;
import com.artursworld.nccn.view.StartMenu;
import com.artursworld.nccn.view.statistics.StatisticsTabsActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserSearchRecyclerAdapter extends RecyclerView.Adapter<UserSearchRecyclerAdapter.ViewHolder> {

    private String CLASS_NAME = UserSearchRecyclerAdapter.class.getSimpleName();
    private final List<User> data;
    private Activity activity;
    private boolean hasToOpenStatistics = false;
    private int selectedUserPosition = 0;

    public void setHasToOpenStatistics(boolean hasTo) {
        hasToOpenStatistics = hasTo;
    }

    public UserSearchRecyclerAdapter(final List<User> data, Activity activity) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list_row, parent, false), activity);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(data.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                User selectedUser = data.get(position);
                Log.i(CLASS_NAME, "clicked on: " + selectedUser);

                if (hasToOpenStatistics) {
                    Global.setSelectedStatisticsUser(selectedUser.getName());
                    Log.i(CLASS_NAME, "open statistics");
                    Intent intent = new Intent(activity.getApplicationContext(), StatisticsTabsActivity.class);
                    activity.finish();
                    activity.startActivity(intent);
                } else {
                    Global.setSelectedUserName(selectedUser.getName());
                    // get date list
                    final List<String> dateList = new UserManager().getQuestionnaireDatesByUserName(selectedUser.getName());
                    dateList.add(0, Strings.getStringByRId(R.string.create_new_questionnaire_date));
                    showQuestionnaireSelectDateDialog(dateList);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                Log.i(CLASS_NAME, "selected user position:" + position);
                return false;
            }
        });
    }

    /**
     * Insert a new item to the RecyclerView on a predefined position
     *
     * @param position
     * @param user
     */
    public void insert(int position, User user) {
        this.data.add(position, user);
        notifyItemInserted(position);
    }

    /**
     * Remove a RecyclerView item containing a specified Data object
     *
     * @param user
     */
    public void remove(User user) {
        //TODO: async
        int deletedUserCount = new UserManager().delete(user);
        if(deletedUserCount>0){
            int position = this.data.indexOf(user);
            data.remove(position);
            notifyItemRemoved(position);
        }
    }


    private void showQuestionnaireSelectDateDialog(final List<String> dateList) {
        new MaterialDialog.Builder(activity)
                .title(R.string.select_qustionnaire_date)
                .items(dateList)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Log.i(CLASS_NAME, "questionnaire date selected: " + text);
                        if (text.equals(Strings.getStringByRId(R.string.create_new_questionnaire_date))) {
                            Global.setSelectedQuestionnaireDate(new Date());
                            Global.setHasToCreateNewQuestionnaire(true);
                        } else {
                            Global.setHasToCreateNewQuestionnaire(false);
                            try {
                                Global.setSelectedQuestionnaireDate(EntityDbManager.dateFormat.parse(dateList.get(which)));
                            } catch (ParseException e) {
                                Log.e(CLASS_NAME, "Could not set Selected Questionnaire Date: " + e.getLocalizedMessage());
                            }
                        }
                        return true;
                    }
                })
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(activity, StartMenu.class);
                        activity.finish();
                        activity.startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getPosition() {
        return selectedUserPosition;
    }

    public void setPosition(int position) {
        this.selectedUserPosition = position;
    }

    /*

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.user_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.delete_user) {
            Log.i(CLASS_NAME, "Option 'delete' selected for user: " + filteredUsers.get(info.position));
           // openMarkUserAsDeletedAttentionDialog(info.position);
        }
        return super.onContextItemSelected(item);
    }
    * */

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnCreateContextMenuListener{
        public TextView textView;
        private Activity activity;

        public ViewHolder(View v, Activity activity) {
            super(v);
            this.activity = activity;
            textView = (TextView) v.findViewById(R.id.textView);
            v.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if(activity !=null) {
                activity.getMenuInflater().inflate(R.menu.user_menu, menu);
            }
        }
    }

}
