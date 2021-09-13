package com.example.google;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class TwitterPage extends AppCompatActivity {

    FirebaseAuth fauth;
    Button btnsignout;
    TwitterLoginButton twittebtn;
    TextView firstname,email;
    CircleImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        firstname=findViewById(R.id.tname);
        email=findViewById(R.id.temail);
        img=findViewById(R.id.timage);
        btnsignout=findViewById(R.id.tsignout);
        getSupportActionBar().setSubtitle("twitter login");
        if (firstname.getText().toString().equals("first_name") || firstname.getText().toString().equals(""))
        {
            btnsignout.setVisibility(View.GONE);
        }
        //for log out user
        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname.setText("");
                email.setText("");
                img.setImageResource(R.drawable.ic_baseline_person_24);
                LoginManager.getInstance().logOut();
                fauth.signOut();
                FirebaseAuth.getInstance().signOut();
            }
        });


        fauth = FirebaseAuth.getInstance();
        twittebtn = findViewById(R.id.twitterbtn);


        TwitterAuthConfig twitterAuthConfig = new TwitterAuthConfig(getString(R.string.twitterapikey),getString(R.string.twittersecret));
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(twitterAuthConfig)
                .build();
        Twitter.initialize(twitterConfig);
        twittebtn.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                getDataofUser(result.data);

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(TwitterPage.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        twittebtn.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getDataofUser(TwitterSession session) {

        AuthCredential authCredential = TwitterAuthProvider.getCredential(session.getAuthToken().token, session.getAuthToken().secret);

        fauth.signInWithCredential(authCredential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(@NonNull AuthResult authResult) {
                        firstname.setText("username \n"+authResult.getUser().getDisplayName());
                        email.setText("Email\n "+authResult.getUser().getEmail());
                        btnsignout.setVisibility(View.VISIBLE);
                        Glide.with(TwitterPage.this).load(authResult.getUser().getPhotoUrl()).into(img);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TwitterPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}