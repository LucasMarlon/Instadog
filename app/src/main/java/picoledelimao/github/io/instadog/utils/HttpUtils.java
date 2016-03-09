package picoledelimao.github.io.instadog.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import picoledelimao.github.io.instadog.utils.HttpListener;
import picoledelimao.github.io.instadog.MainActivity;

/**
 * Created by Abner Matheus on 22/01/2016.
 */
public class HttpUtils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient mClient;

    private Activity mContext;

    public HttpUtils(Activity context) {
        mClient = new OkHttpClient();
        mContext = context;
    }

    public void post(final String url, final String requestBodyJson, final HttpListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody body = RequestBody.create(JSON, requestBodyJson);
                Request request = new Request.Builder().url(url).post(body).build();
                Response response = null;
                try {
                    response = mClient.newCall(request).execute();
                    final String result = response.body().string();
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(result);
                                listener.onSucess(json);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onTimeout();
                        }
                    });
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}