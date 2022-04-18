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
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nathit.indonesia.Activity.NumberActivity;
import com.nathit.indonesia.Adapter.NumberAdapter;
import com.nathit.indonesia.Model.NumberModel;
import com.nathit.indonesia.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    NumberAdapter categoryInAdapter;
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    List<NumberModel> categoryInModelList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("กำลังโหลดข้อมูลคำศัพท์ที่ชื่นชอบ...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        categoryInAdapter = new NumberAdapter(getContext(), categoryInModelList);
        recyclerView.setAdapter(categoryInAdapter);

        db.collection("FAVORITE").orderBy("index")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                NumberModel categoryInModel = documentSnapshot.toObject(NumberModel.class);
                                categoryInModelList.add(categoryInModel);
                                categoryInAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        }
                    }
                });

        return view;
    }
}