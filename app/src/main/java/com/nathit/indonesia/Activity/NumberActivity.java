package com.nathit.indonesia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nathit.indonesia.Adapter.CategoryInAdapter;
import com.nathit.indonesia.Model.CategoryInModel;
import com.nathit.indonesia.R;

import java.util.ArrayList;
import java.util.List;

public class NumberActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CategoryInAdapter categoryInAdapter;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    List<CategoryInModel> categoryInModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        ImageView Btn_Back = findViewById(R.id.btn_back);
        Btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryInModelList.clear();
                startActivity(new Intent(NumberActivity.this, HomeActivity.class));
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("กำลังโหลดข้อมูลคำศัพท์...");

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        categoryInAdapter = new CategoryInAdapter(NumberActivity.this, categoryInModelList);
        recyclerView.setAdapter(categoryInAdapter);

            db.collection("NUMBER").orderBy("index")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                    CategoryInModel categoryInModel = documentSnapshot.toObject(CategoryInModel.class);
                                    categoryInModelList.add(categoryInModel);
                                    categoryInAdapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    });
    }

    int backPressed = 0;

    @Override
    public void onBackPressed() {
        backPressed++;
        if (backPressed == 1) {
            categoryInModelList.clear();
            Intent intent = new Intent(NumberActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }
}