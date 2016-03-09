package picoledelimao.github.io.instadog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import picoledelimao.github.io.instadog.utils.HttpListener;
import picoledelimao.github.io.instadog.utils.HttpUtils;

/**
 * This activity describes the actions taken on login screen
 * @author Abner M. C. Araujo
 * @version 1.0
 * @since 01.17.2016
 */
public class LoginActivity extends AppCompatActivity {

    private HttpUtils mHttp;
    private View mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mHttp = new HttpUtils(this);
        mLoading = findViewById(R.id.loading);
        Button btSignup = (Button) findViewById(R.id.btSignup);
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        Button btLogin = (Button) findViewById(R.id.btLogin);
        final EditText etLogin = (EditText) findViewById(R.id.etLogin);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                login(login, password);
            }
        });
    }

    /**
     * This method is called when the login button is pressed
     * @param login
     *      Login filled in the login field
     * @param password
     *      Password filled in the password field
     */
    private void login(String login, String password) {
        mLoading.setVisibility(View.VISIBLE);
        String url = "http://instadog-lucasmarlon.rhcloud.com/login";
        JSONObject json = new JSONObject();
        try {
            json.put("login", login);
            json.put("senha", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mHttp.post(url, json.toString(), new HttpListener() {
            @Override
            public void onSucess(JSONObject result) {
                try {
                    if (result.getInt("ok") == 0) {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Erro")
                                .setMessage(result.getString("msg"))
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mLoading.setVisibility(View.GONE);
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Sucesso")
                                .setMessage("Login realizado com sucesso")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();                                }
                                })
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTimeout() {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Erro")
                        .setMessage("Conexão não disponível.")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mLoading.setVisibility(View.GONE);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

}
