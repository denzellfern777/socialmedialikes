package com.app.socialmedialikes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> implements View.OnClickListener {

    public TaskAdapter(ArrayList<Task> data, Context context) {
        super(context, R.layout.list_item_task, data);
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Task dataModel = (Task) object;

        if (v.getId() == R.id.task_name_tv) {
            Snackbar.make(v, "Task Name " + dataModel.getTaskName(), Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_task, parent, false);
            viewHolder.taskName = convertView.findViewById(R.id.task_name_tv);
            viewHolder.taskDesc = convertView.findViewById(R.id.task_desc_tv);
            viewHolder.taskReward = convertView.findViewById(R.id.task_reward_tv);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.taskName.setText(dataModel.getTaskName());
        viewHolder.taskDesc.setText(dataModel.getTaskDesc());
        viewHolder.taskReward.setText("+" + dataModel.getTaskReward());
        // Return the completed view to render on screen
        return convertView;
    }

    private static class ViewHolder {
        TextView taskName;
        TextView taskDesc;
        TextView taskReward;
    }

}