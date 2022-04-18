package com.nathit.indonesia.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();


        FirebaseRecyclerOptions<CategoryInModel> options =
                new FirebaseRecyclerOptions.Builder<CategoryInModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("favorite"), CategoryInModel.class)
                        .build();


        favoriteAdapter =new FavoriteAdapter(options,getContext());
        recyclerView.setAdapter(favoriteAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        favoriteAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        favoriteAdapter.stopListening();
    }
}