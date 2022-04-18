package com.nathit.indonesia.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nathit.indonesia.Model.NumberModel;
import com.nathit.indonesia.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ViewHolder> {

    TextToSpeech toSpeech;
    Context context;
    List<NumberModel> categoryInAdapterArrayList;
    FirebaseFirestore db;
    List<NumberModel> categoryInModelList = new ArrayList<>();


    public NumberAdapter(Context context, List<NumberModel> categoryInAdapterArrayList) {
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
        holder.setData(des, image, title);

    }

    @Override
    public int getItemCount() {
        return categoryInAdapterArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cat_title, cat_des;
        ImageView cat_image, cat_sound, cat_sound_off, cat_fav;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_title = itemView.findViewById(R.id.cat_title);
            cat_des = itemView.findViewById(R.id.cat_des);
            cat_image = itemView.findViewById(R.id.cat_image);
            cat_sound = itemView.findViewById(R.id.cat_sound);
            cat_sound_off = itemView.findViewById(R.id.cat_sound_off);
            cat_fav = itemView.findViewById(R.id.cat_fav);
        }

        private void setData(String des, String image, String title) {
            db = FirebaseFirestore.getInstance();
            cat_des.setText(des);
            Glide.with(context).load(itemView.getContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(cat_image);
            cat_title.setText(title);
            toSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        toSpeech.setLanguage(new Locale("id","ID"));
                    }
                }
            });

            cat_sound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String toSpeak = title;
                    Toast.makeText(context, toSpeak, Toast.LENGTH_SHORT).show();
                    toSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                    if (!isNetworkAvailable() == true) {
                        new AlertDialog.Builder(context)
                                .setIcon(R.drawable.ic_dialog_alert)
                                .setTitle("แจ้งเตือน!")
                                .setMessage("กรุณาเชื่อมต่ออินเทอร์เน็ตเพื่อฟังเสียงคำศัพท์")
                                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                }

                public boolean isNetworkAvailable() {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                    if (connectivityManager != null) {
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                            if (capabilities != null) {
                                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                    return true;
                                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                    return true;
                                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }
            });

            cat_sound_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toSpeech.stop();
                    Toast.makeText(context, "ปิดเสียงคำศัพท์", Toast.LENGTH_SHORT).show();
                }
            });

            cat_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }
    }

}
