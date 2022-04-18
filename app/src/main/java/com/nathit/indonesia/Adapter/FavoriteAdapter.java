package com.nathit.indonesia.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nathit.indonesia.Activity.HomeActivity;
import com.nathit.indonesia.Model.CategoryInModel;
import com.nathit.indonesia.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FavoriteAdapter extends FirebaseRecyclerAdapter<CategoryInModel, FavoriteAdapter.viewHolder> {
    FirebaseAuth firebaseAuth;
    Context context;

    public FavoriteAdapter(@NonNull FirebaseRecyclerOptions<CategoryInModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FavoriteAdapter.viewHolder holder, int position, @NonNull CategoryInModel model) {
        String title = model.getCat_title();
        String des = model.getCat_des();
        Integer index = model.getIndex();
        Glide.with(holder.cat_image.getContext()).load(model.getCat_image()).into(holder.cat_image);
        holder.setData(des, title, index, position);
    }

    @NonNull
    @Override
    public FavoriteAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_layout, parent, false);
        return new viewHolder(view);
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView cat_title, cat_des;
        ImageView cat_image, cat_sound, cat_sound_off, cat_del;
        TextToSpeech toSpeech;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            cat_title = itemView.findViewById(R.id.cat_title);
            cat_des = itemView.findViewById(R.id.cat_des);
            cat_image = itemView.findViewById(R.id.cat_image);
            cat_sound = itemView.findViewById(R.id.cat_sound);
            cat_sound_off = itemView.findViewById(R.id.cat_sound_off);
            cat_del = itemView.findViewById(R.id.cat_fav);
        }

        private void setData(String des, String title, Integer index, int position) {
            cat_des.setText(des);
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

            cat_del.setImageResource(R.drawable.ic_delete);
            cat_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(cat_image.getContext());
                    builder.setTitle("ยกเลิกคำศัพท์ที่ชื่นชอบ");
                    builder.setMessage("คุณต้องการยกเลิกคำศัพท์ที่ชื่นชอบใช่หรือไม่?");


                    builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteCategory(position);
                        }
                    });

                    builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    builder.show();

                }
            });
        }

        private void deleteCategory(int position) {
            firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String uid = user.getUid();
            FirebaseDatabase.getInstance().getReference("Users").child(uid).child("favorite").child(getRef(position).getKey()).removeValue();

        }
    }
}


