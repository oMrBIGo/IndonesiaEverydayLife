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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nathit.indonesia.Adapter.CategoryInAdapter;
import com.nathit.indonesia.Model.CategoryInModel;
import com.nathit.indonesia.R;

import java.util.ArrayList;
import java.util.List;

public class NumberActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CategoryInAdapter categoryInAdapter;
    ProgressDialog progressDialog;
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
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CATEGORYS").child("NUMBER");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryInModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CategoryInModel model = ds.getValue(CategoryInModel.class);

                    categoryInModelList.add(model);

                    categoryInAdapter = new CategoryInAdapter(getApplicationContext(), categoryInModelList);
                    recyclerView.setAdapter(categoryInAdapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
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