package com.nathit.indonesia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nathit.indonesia.Model.CategoryInModel;
import com.nathit.indonesia.R;

import java.util.List;

public class CategoryInAdapter extends RecyclerView.Adapter<CategoryInAdapter.ViewHolder> implements View.OnClickListener{

    Context context;
    List<CategoryInModel> categoryInAdapterArrayList;


    public CategoryInAdapter(Context context, List<CategoryInModel> categoryInAdapterArrayList) {
        this.context = context;
        this.categoryInAdapterArrayList = categoryInAdapterArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = categoryInAdapterArrayList.get(position).getCat_title();
        String des = categoryInAdapterArrayList.get(position).getCat_des();
        String image = categoryInAdapterArrayList.get(position).getCat_image();
        holder.setData(des,image,title);

    }

    @Override
    public int getItemCount() {
        return categoryInAdapterArrayList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cat_title,cat_des;
        ImageView cat_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_title = itemView.findViewById(R.id.cat_title);
            cat_des = itemView.findViewById(R.id.cat_des);
            cat_image = itemView.findViewById(R.id.cat_image);
        }

        private void setData(String des,String image, String title) {
            cat_des.setText(des);
            Glide.with(context).load(itemView.getContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(cat_image);
            cat_title.setText(title);

        }
    }
}
