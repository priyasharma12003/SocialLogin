package com.example.google;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLogin extends AppCompatActivity {
  /*  private CircleImageView mCircleImageView;
    private LoginButton login;
    private TextView txtname,txtemail;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCircleImageView=findViewById(R.id.image);
        txtname=findViewById(R.id.name);
        txtemail=findViewById(R.id.email);
        login=findViewById(R.id.login_button);

        mCallbackManager=CallbackManager.Factory.create();
        login.setReadPermissions(Arrays.asList("email","profile_picture"));

        login.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);


        AccessTokenTracker accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {


                if (currentAccessToken==null){
                    txtname.setText("");
                    txtemail.setText("");
                    mCircleImageView.setImageResource(0);
                    Toast.makeText(MainActivity.this, "User Logged Out", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadUserProfile(currentAccessToken);
                }
            }
        };


    }
    private void loadUserProfile(AccessToken newaccesstoken){

        GraphRequest graphRequest= GraphRequest.newMeRequest(newaccesstoken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");
                    String email=object.getString("emailid");
                    String id=object.getString("id");
                    String imageurl="htttps://graph.facebook.com/"+id+"/picture?type=normal";


                    txtname.setText(first_name+""+last_name);
                    txtemail.setText(email);

                    RequestOptions requestOptions=new RequestOptions();
                    requestOptions.dontAnimate();


                    Glide.with(MainActivity.this).load(imageurl).into(mCircleImageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,emailid");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }*/






    CallbackManager callbackManager;
    LoginButton btnfacebook;
    Button mTwitterLoginButton;
    AccessTokenTracker accessTokenTracker;
    TextView useremail,firstname;
    ImageView imageView;
    private static final String EMAIL = "email";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        btnfacebook = findViewById(R.id.login_button);

        imageView = findViewById(R.id.image);
        useremail = findViewById(R.id.email);
        firstname=findViewById(R.id.name);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        mTwitterLoginButton = findViewById(R.id.twitter);


        mTwitterLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FacebookLogin.this,TwitterPage.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setSubtitle("User Login");
        btnfacebook.setPermissions(Arrays.asList(EMAIL));

        if (!btnfacebook.getText().toString().equals("Log out"))
        {
            firstname.setText("");
            useremail.setText("");
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(FacebookLogin.this,GoogleLogin.class);
                startActivity(i);
            }
        });


        // Callback registration
        btnfacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(FacebookLogin.this, "Login canceled by user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(FacebookLogin.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //getting current user token (if any available user is logged in)
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    firstname.setText("");
                    useremail.setText("");
                    Toast.makeText(FacebookLogin.this, "no user logged-in at this time", Toast.LENGTH_SHORT).show();
                } else

                getUserDetails(currentAccessToken);
            }
        };

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void getUserDetails(AccessToken accessToken) {
        //requesting server to fetching data from facebook server
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    try {
                        String first_name=object.getString("first_name");
                        String last_name=object.getString("last_name");
                        String email=object.getString("email");
                        String id=object.getString("id");
                        String imageview="https://graph.facebook.com/"+id+"/picture?type=normal";


                        firstname.setText(first_name+""+last_name);
                        useremail.setText(email);
                        RequestOptions options=new RequestOptions();
                        options.dontAnimate();
                        Glide.with(FacebookLogin.this).load(imageview).into(imageView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
        Bundle bundle = new Bundle();
        //putting keyword to retrieve the same data from facebook server
        bundle.putString("fields", "id,first_name,last_name,email");
        request.setParameters(bundle);
        request.executeAsync();

    }
}