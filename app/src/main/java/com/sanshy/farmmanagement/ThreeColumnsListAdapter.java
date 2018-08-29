package com.sanshy.farmmanagement;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ThreeColumnsListAdapter extends ArrayAdapter<ThreeColumnsList> {
    ArrayList<ThreeColumnsList> MyList = new ArrayList<>();
    Activity context;

    public ThreeColumnsListAdapter(@NonNull Activity context, ArrayList<ThreeColumnsList> MyList) {
        super(context, R.layout.three_columns_list, MyList);

        this.context = context;
        this.MyList = MyList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.three_columns_list,null,true);

        ThreeColumnsList list = MyList.get(position);

        TextView First = rowView.findViewById(R.id.first_column);
        TextView Second = rowView.findViewById(R.id.second_column);
        TextView Third = rowView.findViewById(R.id.third_column);

        First.setText(list.getFirst());
        Second.setText(list.getSecond());
        Third.setText(list.getThired());

        return rowView;
    }

}
