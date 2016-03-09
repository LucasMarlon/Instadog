package picoledelimao.github.io.instadog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import picoledelimao.github.io.instadog.utils.HttpListener;
import picoledelimao.github.io.instadog.utils.HttpUtils;

/**
 * This activity describes the actions taken on signup screen
 * @author Abner M. C. Araujo
 * @version 1.0
 * @since 01.17.2016
 */
public class SignupActivity extends AppCompatActivity {

    private HttpUtils mHttp;
    private View mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mHttp = new HttpUtils(this);
        mLoading = findViewById(R.id.loading);
        Button btSignup = (Button) findViewById(R.id.btSignup);
        final EditText etLogin = (EditText) findViewById(R.id.etLogin);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etRepeatPassword = (EditText) findViewById(R.id.etRepeatPassword);
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = etLogin.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String repeatPassword = etRepeatPassword.getText().toString();
                signup(login, email, password, repeatPassword);
            }
        });
    }

    /**
     * This method is called when the signup button is pressed
     * @param login
     *      Login filled in the login field
     * @param email
     *      E-mail filled in the e-mail field
     * @param password
     *      Password filled in the password field
     * @param repeatPassword
     *      Repeat password filled in the repeat password field
     */
    private void signup(final String login, final String email, final String password, final String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            new AlertDialog.Builder(SignupActivity.this)
                    .setTitle("Erro")
                    .setMessage("Senhas não coincidem")
                    .setNeutralButton("OK", null)
                    .create()
                    .show();
        } else {
            mLoading.setVisibility(View.VISIBLE);
            String url = "http://instadog-lucasmarlon.rhcloud.com/cadastrar";
            JSONObject json = new JSONObject();
            try {
                json.put("login", login);
                json.put("email", email);
                json.put("senha", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mHttp.post(url, json.toString(), new HttpListener() {
                @Override
                public void onSucess(JSONObject result) {
                    try {
                        if (result.getInt("ok") == 0) {
                            new AlertDialog.Builder(SignupActivity.this)
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
                            new AlertDialog.Builder(SignupActivity.this)
                                    .setTitle("Sucesso")
                                    .setMessage("Cadastro realizado com sucesso")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            finish();
                                        }
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
                    new AlertDialog.Builder(SignupActivity.this)
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
}