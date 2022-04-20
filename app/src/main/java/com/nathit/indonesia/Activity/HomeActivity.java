package com.nathit.indonesia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nathit.indonesia.Fragment.FavoriteFragment;
import com.nathit.indonesia.Fragment.HomeFragment;
import com.nathit.indonesia.Fragment.ProfileFragment;
import com.nathit.indonesia.Fragment.QuizFragment;
import com.nathit.indonesia.Model.UsersModel;
import com.nathit.indonesia.R;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String uid;
    TextView tvName, tvEmail;
    ImageView show_profile, hide_profile;

    private long backPressedTime;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();




        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();

        tvName = (TextView) findViewById(R.id.tvName_home);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        show_profile = (ImageView) findViewById(R.id.show_profile);
        hide_profile = (ImageView) findViewById(R.id.hide_profile);

        showProfile();

        LinearLayout profile_show = (LinearLayout) findViewById(R.id.profile_show);
        profile_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvName.setVisibility(View.GONE);
                tvEmail.setVisibility(View.GONE);
                show_profile.setVisibility(View.VISIBLE);
                hide_profile.setVisibility(View.GONE);
            }
        });

        show_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvName.setVisibility(View.VISIBLE);
                tvEmail.setVisibility(View.VISIBLE);
                show_profile.setVisibility(View.GONE);
                hide_profile.setVisibility(View.VISIBLE);
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment =null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.nav_fav:
                        fragment = new FavoriteFragment();
                        break;
                    case R.id.nav_quiz:
                        fragment = new QuizFragment();
                        break;
                    case R.id.nav_profile:
                        fragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                return true;
            }
        });
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
                    Toast.makeText(getApplicationContext(), "กรุณาเชื่อมต่ออินเทอร์เน็ต", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "กรุณาเชื่อมต่ออินเทอร์เน็ต", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            moveTaskToBack(true);
            finish();
        } else {
            Toast.makeText(this, "ทำอีกครั้งเพื่อออกจากแอปพลิเคชัน", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}