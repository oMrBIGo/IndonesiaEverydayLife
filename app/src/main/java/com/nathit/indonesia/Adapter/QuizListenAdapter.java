package com.nathit.indonesia.Adapter;

import static com.nathit.indonesia.Fragment.QuizFragment.selected_cat_index;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nathit.indonesia.Activity.HomeActivity;
import com.nathit.indonesia.Activity.QuizListenActivity;
import com.nathit.indonesia.Model.QuizListenModel;
import com.nathit.indonesia.R;

import java.util.List;

public class QuizListenAdapter extends BaseAdapter {

    private List<QuizListenModel> quizListenModelList;

    public QuizListenAdapter(List<QuizListenModel> quizListenModelList) {
        this.quizListenModelList = quizListenModelList;
    }

    @Override
    public int getCount() {
        return quizListenModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_layout, parent, false);
        } else {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected_cat_index = position;
                Intent intent = new Intent(parent.getContext(), QuizListenActivity.class);
                parent.getContext().startActivity(intent);
                view.setEnabled(false);
            }
        });

        ((TextView) view.findViewById(R.id.setName)).setText(quizListenModelList.get(position).getName());

        return view;
    }
}
