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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nathit.indonesia.Adapter.CategoryInAdapter;
import com.nathit.indonesia.Adapter.FavoriteAdapter;
import com.nathit.indonesia.Model.CategoryInModel;
import com.nathit.indonesia.R;

import java.util.ArrayList;
import java.util.List;

public class NumberActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    CategoryInAdapter categoryInAdapter;
    List<CategoryInModel> categoryInModelList = new ArrayList<>();
    DatabaseReference databaseReference,fvrRef,fvr_listRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("CATEGORYS").child("NUMBER");


        ImageView Btn_Back = findViewById(R.id.btn_back);
        Btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryInModelList.clear();
                startActivity(new Intent(NumberActivity.this, HomeActivity.class));
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        FirebaseRecyclerOptions<CategoryInModel> options =
                new FirebaseRecyclerOptions.Builder<CategoryInModel>()
                        .setQuery(databaseReference, CategoryInModel.class)
                        .build();

        categoryInAdapter = new CategoryInAdapter(options,this);
        recyclerView.setAdapter(categoryInAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        categoryInAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        categoryInAdapter.stopListening();
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