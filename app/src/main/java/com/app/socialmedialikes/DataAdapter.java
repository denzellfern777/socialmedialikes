package com.app.socialmedialikes;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataAdapter implements ListAdapter {
    private final ArrayList<Data> arrayList;
    private final Context context;

    public DataAdapter(Context context, ArrayList<Data> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data data = arrayList.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_item_reward_history, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            TextView transaction = convertView.findViewById(R.id.points_added_deducted_tv);
            TextView approval = convertView.findViewById(R.id.approval_tv);
            TextView timestamp = convertView.findViewById(R.id.timestamp_tv);
            TextView points = convertView.findViewById(R.id.points_tv);


            timestamp.setText(formatDate(data.timestamp));

            String transactionStr = data.getTransaction();
            String approvalStr = data.getApproval();

            if (approvalStr.equals("approved")) {
                approval.setText(context.getString(R.string.approved));
            } else if (approvalStr.equals("pending")) {
                approval.setText(context.getString(R.string.pending));
            }


            switch (transactionStr) {
                case "ad_view":
                    points.setText(String.format("+%s", data.points));
                    points.setTextColor(convertView.getResources().getColor(R.color.green));
                    transaction.setText(MainActivity.getInstance().getString(R.string.video_ad_reward));
                    break;
                case "redeemed":
                    points.setText(String.format("-%s", data.points));
                    points.setTextColor(convertView.getResources().getColor(R.color.colorPrimary));
                    transaction.setText(MainActivity.getInstance().getString(R.string.redeemed));
                    break;
                case "task":
                    if (data.getApproval().equals("pending")) {
                        points.setText(String.format("+%s", data.points));
                        points.setTextColor(convertView.getResources().getColor(R.color.colorPrimary));

                    } else if (data.getApproval().equals("approved")) {
                        points.setText(String.format("+%s", data.points));
                        points.setTextColor(convertView.getResources().getColor(R.color.green));
                    }
                    transaction.setText(MainActivity.getInstance().getString(R.string.task));
                    break;
            }


        }
        return convertView;
    }

    private String formatDate(String dateParam) {

        if (TextUtils.isEmpty(dateParam)) {
            return "(Blank)";
        } else {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            Date date = null;
            try {
                date = inputFormat.parse(dateParam);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, 330);

            return outputFormat.format(calendar.getTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}