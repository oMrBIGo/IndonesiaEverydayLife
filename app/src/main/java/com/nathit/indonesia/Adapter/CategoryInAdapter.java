package com.nathit.indonesia.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nathit.indonesia.Activity.MainActivity;
import com.nathit.indonesia.Model.CategoryInModel;
import com.nathit.indonesia.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CategoryInAdapter extends FirebaseRecyclerAdapter<CategoryInModel, CategoryInAdapter.viewHolder> {

    Context context;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    public CategoryInAdapter(@NonNull FirebaseRecyclerOptions<CategoryInModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull CategoryInModel model) {
        String title = model.getCat_title();
        String des = model.getCat_des();
        String image = model.getCat_image();
        Glide.with(holder.cat_image.getContext()).load(model.getCat_image()).into(holder.cat_image);
        Integer index = model.getIndex();
        holder.setData(des, image, title, index, position);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_layout, parent, false);
        return new viewHolder(view);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView cat_title, cat_des;
        ImageView cat_image, cat_sound, cat_sound_off, cat_fav;
        TextToSpeech toSpeech;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            cat_title = itemView.findViewById(R.id.cat_title);
            cat_des = itemView.findViewById(R.id.cat_des);
            cat_image = itemView.findViewById(R.id.cat_image);
            cat_sound = itemView.findViewById(R.id.cat_sound);
            cat_sound_off = itemView.findViewById(R.id.cat_sound_off);
            cat_fav = itemView.findViewById(R.id.cat_fav);
        }

        private void setData(String des, String image, String title, Integer index, int position) {
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

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser == null) {
                cat_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(cat_image.getContext());
                        builder.setTitle("กรุณาเข้าสู่ระบบ!");
                        builder.setMessage("หากคุณต้องการที่ต้องบันทึกคำศัพท์ที่คุณชื่นชอบ กรุณาเข้าสู่ระบบ");


                        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                context.startActivity(new Intent(context, MainActivity.class));
                            }
                        });

                        builder.setNegativeButton("ภายหลัง", new DialogInterface.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                    }
                });
            } else {
                cat_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(cat_image.getContext());
                        builder.setTitle("เพิ่มคำศัพท์ที่ชื่นชอบ");
                        builder.setMessage("คุณต้องการเพิ่มคำศัพท์ที่ชื่นชอบใช่หรือไม่?");


                        builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addFavorite(des, image, title, index);
                                ColorStateList csl = AppCompatResources.getColorStateList(context, R.color.red);
                                ImageViewCompat.setImageTintList(cat_fav, csl);
                                cat_fav.setEnabled(false);
                            }
                        });

                        builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        builder.show();
                    }
                });
            }

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
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"คำศัพท์: "+title +" | ความหมาย: "+des+ image);
                    context.startActivity(Intent.createChooser(shareIntent, "แชร์ข้อมูล..."));

                    // toSpeech.stop();
                   // Toast.makeText(context, "ปิดเสียงคำศัพท์", Toast.LENGTH_SHORT).show();
                }
            });

        }


        private void addFavorite(String des, String image, String title, int index) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String uid = user.getUid();
            String id = databaseReference.push().getKey();

            final Map<String, Object> catData = new ArrayMap<>();
            catData.put("cat_des", des);
            catData.put("cat_image", image);
            catData.put("cat_title", title);
            catData.put("index", index);
            databaseReference.child(uid).child("favorite").child(id).setValue(catData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "เพิ่มคำศัพท์ที่ชื่นชอบแล้ว", Toast.LENGTH_SHORT).show();
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
