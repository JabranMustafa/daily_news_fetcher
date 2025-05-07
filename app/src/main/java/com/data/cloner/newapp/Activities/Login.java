package com.data.cloner.newapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.Insets;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.data.cloner.newapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Login extends AppCompatActivity  {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    AppCompatButton googleBtn;
    Button changeLanguageButton;
    private Spinner languageSpinner;
    private final String[] languageNames = {"Arabic", "English","French", "German"};
    private final String[] languageCodes = {"ar","en", "fr", "de"};
    boolean firstLaunch = true;

    private FirebaseAuth mAuth;
    private AppCompatEditText emailField, passwordField;
    private AppCompatButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            String currentLang = Locale.getDefault().getLanguage();
            applyLegacyLocale(currentLang); // ensures UI is correct before view loads
        }
//        LocaleHelper.setLocale(this, getSharedPreferences("settings", MODE_PRIVATE).getString("language", "en"));
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setupLanguageSpinner();

/*        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
*//*
                if (LocaleHelper.getCurrentLocale(Login.this).equals("ar")){
                    LocaleHelper.setAppLocale(Login.this,"en");
                    recreate();
                } else if (LocaleHelper.getCurrentLocale(Login.this).equals("en")) {
                    LocaleHelper.setAppLocale(Login.this,"ar");
                    recreate();
                }*//*
//                changeLanguage("ar");
                setAppLanguage("ar");
            }
        });*/

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        googleBtn = findViewById(R.id.btnGoogle);

        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.etEmailLogin);
        passwordField = findViewById(R.id.etPasswordLogin);
        loginButton = findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            } else  {
                Intent signInIntent = gsc.getSignInIntent();
                startActivityForResult(signInIntent,1000);
            }


        });






        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            navigateToSecondActivity();
        }


        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }
    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(Login.this, NewsActivity.class);
        startActivity(intent);
    }
//    private void changeLanguage(String langCode) {
//        getSharedPreferences("settings", MODE_PRIVATE).edit().putString("language", langCode).apply();
//        LocaleHelper.setLocale(this, langCode);
//        recreate(); // Restart activity to apply language
//    }

    private void setupLanguageSpinner() {
        languageSpinner = findViewById(R.id.spinnerLanguages);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, languageNames);
        languageSpinner.setAdapter(adapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean first = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (firstLaunch) {
                    firstLaunch = false;
                    return; // skip initial load
                }
                String langCode = languageCodes[position];
                changeAppLanguage(langCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void changeAppLanguage(@NonNull String languageCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            LocaleListCompat locales = LocaleListCompat.forLanguageTags(languageCode);
            AppCompatDelegate.setApplicationLocales(locales);
        } else {
            applyLegacyLocale(languageCode);
            recreate();
        }
    }

    private void applyLegacyLocale(@NonNull String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
