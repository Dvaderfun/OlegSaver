package com.quarnuts.olegsaver.ui.login;

import android.app.Activity;

import alirezat775.lib.kesho.Kesho;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.quarnuts.olegsaver.MainActivity;
import com.quarnuts.olegsaver.R;
import com.quarnuts.olegsaver.api.model.LoginInformation;
import com.quarnuts.olegsaver.api.model.ProfileResponse;
import com.quarnuts.olegsaver.api.retrofit.UserClient;

import static com.quarnuts.olegsaver.api.Api.BASE_URL;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText loginEdit;
    TextInputEditText passEdit;
    MaterialButton materialButton;
    private UserClient client;
    Kesho kesho;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        kesho = new Kesho(this, Kesho.SHARED_PREFERENCES_MANAGER, Kesho.Encrypt.NONE_ENCRYPT,"_hello_world_secret_key_");


        loginEdit = findViewById(R.id.login);
        passEdit = findViewById(R.id.pass);
        materialButton = findViewById(R.id.loginButton);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(UserClient.class);

        materialButton.setOnClickListener(view -> {
            LoginInformation loginInformation = new LoginInformation(loginEdit.getText().toString(), passEdit.getText().toString());
            signInProfile(loginInformation);
        });
    }

    public void signInProfile(LoginInformation loginInformation) {

        Call<ProfileResponse> c = client.signIn(loginInformation);

        c.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                String token = response.body().getToken();
                kesho.push("token", token, Kesho.NONE_EXPIRE_TIME);

                String role = response.body().getRole();
                kesho.push("role", role, Kesho.NONE_EXPIRE_TIME);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failed to get response", Toast.LENGTH_LONG).show();
                Log.d("REST", call.toString());
                Log.d("REST", t.getMessage());
            }
        });
    }
}
