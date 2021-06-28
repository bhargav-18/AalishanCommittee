package com.example.aalishancommittee;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RulesAdapter extends ArrayAdapter<Rules> {

    private final Context context;
    private final List<Rules> rules;

    public RulesAdapter(Context context, List<Rules> list)
    {
        super(context, R.layout.rules_row_layout, list);
        this.context = context;
        this.rules = list;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView =inflater.inflate(R.layout.rules_row_layout, parent, false);

        TextView tvSrNo = convertView.findViewById(R.id.tvSrNo);
        TextView tvTitleRule = convertView.findViewById(R.id.tvTitleRule);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);

        tvSrNo.setText(rules.get(position).getSrNo().trim() + ".");
        tvTitleRule.setText(rules.get(position).getTitle().trim());
        tvDescription.setText(rules.get(position).getDescription().trim());

        return convertView;
    }
}
