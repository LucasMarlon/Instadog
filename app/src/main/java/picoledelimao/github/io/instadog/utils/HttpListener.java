package picoledelimao.github.io.instadog.utils;

import org.json.JSONObject;

/**
 * Created by Lucas on 22/01/2016.
 */
public interface HttpListener {
    void onSucess(JSONObject response);
    void onTimeout();
}
