package com.artursworld.nccn.view.user;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Global;
import com.artursworld.nccn.controller.util.Strings;
import com.artursworld.nccn.model.entity.User;
import com.artursworld.nccn.model.persistence.manager.EntityDbManager;
import com.artursworld.nccn.model.persistence.manager.UserManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserSearchRecyclerAdapter extends RecyclerView.Adapter<UserSearchRecyclerAdapter.ViewHolder> {

    private String CLASS_NAME = UserSearchRecyclerAdapter.class.getSimpleName();
    private final List<User> data;
    private Activity activity;

    public UserSearchRecyclerAdapter(final List<User> data, Activity activity) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(data.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                User selectedUser = data.get(position);
                Log.i(CLASS_NAME, "clicked on: " + selectedUser);
                Global.setSelectedUserName(selectedUser.getName());

                // get date list
                final List<String> dateList = new UserManager().getQuestionnaireDatesByUserName(selectedUser.getName());
                dateList.add(0, Strings.getStringByRId(R.string.create_new_questionnaire_date));

                showQuestionnaireSelectDateDialog(dateList);
            }
        });
    }

    /**
     * Insert a new item to the RecyclerView on a predefined position
     * @param position
     * @param user
     */
    public void insert(int position, User user) {
        this.data.add(position, user);
        notifyItemInserted(position);
    }

    /**
     * Remove a RecyclerView item containing a specified Data object
     * @param user
     */
    public void remove(User user) {
        int position = this.data.indexOf(user);
        data.remove(position);
        notifyItemRemoved(position);
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
                .show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
        }
    }

}
