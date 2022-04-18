package com.nathit.indonesia.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nathit.indonesia.Model.NumberModel;
import com.nathit.indonesia.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ViewHolder> {

    TextToSpeech toSpeech;
    Context context;
    List<NumberModel> categoryInAdapterArrayList;
    FirebaseFirestore db;
    List<NumberModel> categoryInModelList = new ArrayList<>();
    NumberAdapter adapter;

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
        Integer index = categoryInAdapterArrayList.get(position).getIndex();
        holder.setData(des, image, title, index);

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

        private void setData(String des, String image, String title, int index) {
            db = FirebaseFirestore.getInstance();
            cat_des.setText(des);
            Glide.with(context).load(itemView.getContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(cat_image);
            cat_title.setText(title);
            toSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        toSpeech.setLanguage(new Locale("id", "ID"));
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
                    addNewCategory(des, image, title, index);
                }
            });
        }

        private void addNewCategory(String des, String image, String title, int index) {

            final Map<String, Object> catData = new ArrayMap<>();
            catData.put("cat_des", des);
            catData.put("cat_image", image);
            catData.put("cat_title", title);
            catData.put("index", index);

            final String cat_id = db.collection("FAVORITE").document().getId();

            db.collection("FAVORITE").document(cat_id)
                    .set(catData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Map<String, Object> catDoc = new ArrayMap<>();
                            catDoc.put("cat_des", des);
                            catDoc.put("cat_image", image);
                            catDoc.put("cat_title", title);
                            catDoc.put("index", index);

                            db.collection("FAVORITE").document()
                                    .update(catDoc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "เพิ่มคำศัพท์ที่ชื่นชอบแล้ว", Toast.LENGTH_SHORT).show();
                                            categoryInModelList.add(new NumberModel(des,image,title,index));
                                            adapter.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "เพิ่มคำศัพท์ที่ชื่นชอบแล้ว", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(context, "ไม่สามารถเพิ่มคำศัพท์ที่ชื่นชอบได้ โปรดตรวจสอบอินเทอร์เน็ตของท่าน แล้วลองใหม่อีกครั้ง!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}
