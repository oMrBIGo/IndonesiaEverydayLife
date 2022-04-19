package com.nathit.indonesia.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nathit.indonesia.Activity.HomeActivity;
import com.nathit.indonesia.Activity.MainActivity;
import com.nathit.indonesia.Model.UsersModel;
import com.nathit.indonesia.R;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView tvName, tvEmail;
    Button EditProfile, Logout , EditHide;
    private EditText editName;
    private String uid, textName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();

        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        EditProfile = view.findViewById(R.id.EditProfile);
        Logout = view.findViewById(R.id.Logout);
        EditHide = view.findViewById(R.id.EditHide);
        CardView CardView_EditProfile = view.findViewById(R.id.CardView_EditProfile);
        editName = view.findViewById(R.id.etname);

        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CardView_EditProfile.setVisibility(View.VISIBLE);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });

        EditHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardView_EditProfile.setVisibility(View.GONE);
            }
        });


        showProfile();

        //EditProfile
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = "" + snapshot.child("name").getValue();
                editName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button buttonUpdateProfile = view.findViewById(R.id.EditConfirm);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile(firebaseUser);
            }
        });

        return view;
    }

    private void editProfile(FirebaseUser firebaseUser) {
        textName = editName.getText().toString().trim();
        if (TextUtils.isEmpty(textName)) {
            Toast.makeText(getContext(), "กรุณากรอกชื่อ", Toast.LENGTH_LONG).show();
            editName.setError("กรุณาระบุชื่อเต็ม");
            editName.requestFocus();
        } else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("name", textName);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(uid).updateChildren(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(textName).build();
                            firebaseUser.updateProfile(profileChangeRequest);
                            Toast.makeText(getContext(), "แก้ไขโปรไฟล์แล้ว", Toast.LENGTH_SHORT).show();
                            restartApp();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void restartApp() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ProfileFragment()).commit();
    }

    private void showProfile() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersModel model = snapshot.getValue(UsersModel.class);
                if (model != null) {
                    String name = "" + snapshot.child("name").getValue();
                    String email = "" + snapshot.child("email").getValue();

                    tvName.setText("ชื่อ: "+name);
                    tvEmail.setText("อีเมล: "+email);
                } else {
                    Toast.makeText(getContext(), "กรุณาเชื่อมต่ออินเทอร์เน็ต", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "กรุณาเชื่อมต่ออินเทอร์เน็ต", Toast.LENGTH_SHORT).show();
            }
        });
    }
}