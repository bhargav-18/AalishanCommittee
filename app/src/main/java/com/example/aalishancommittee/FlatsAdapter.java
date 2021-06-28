package com.example.aalishancommittee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FlatsAdapter extends ArrayAdapter<Flats>
{
    private Context context;
    private List<Flats> flats;

    public FlatsAdapter(Context context, List<Flats> list)
    {
        super(context, R.layout.row_layout, list);
        this.context = context;
        this.flats = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        convertView =inflater.inflate(R.layout.row_layout, parent, false);

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvFlatNo = convertView.findViewById(R.id.tvFlatNo);

        tvName.setText(flats.get(position).getOwnerName().trim());
        tvFlatNo.setText(flats.get(position).getFlatNo().trim());

        return convertView;
    }
}
