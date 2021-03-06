package com.example.quizz.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quizz.LoginActivity;
import com.example.quizz.R;
import com.example.quizz.Uploadphoto;
import com.example.quizz.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    ImageView userAvatar;
    TextView phoneNumber;
    TextView studentId;
    TextView name;
    TextView email;
    Button signout;
    private String userUid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userAvatar = (ImageView)view.findViewById(R.id.img_userAvatar);
        phoneNumber = (TextView)view.findViewById(R.id.txt_phoneNumber);
        studentId = (TextView)view.findViewById(R.id.txt_studentId);
        name = (TextView)view.findViewById(R.id.txt_username);
        email = (TextView)view.findViewById(R.id.txt_email);
        signout = (Button)view.findViewById(R.id.btn_signout);
        getUserProfile();
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Uploadphoto.class);
                startActivity(intent);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent signoutIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(signoutIntent);
            }
        });
        return view;
    }
    private void getUserProfile(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userUid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                //ImageView
                Glide.with(getContext()).load(user.getAvatar()).into(userAvatar);
                studentId.setText(user.getStudentId());
                phoneNumber.setText(user.getPhone());
                email.setText(user.getEmail());
                name.setText(user.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
