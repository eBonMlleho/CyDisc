package coms309.sb_c_4_cydisc;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhanghao on 2017/10/8.
 */
/**
 * @author Zhanghao Wen
 * This class set up a connection between login and network by using RequestQueue
 */
public class LoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = ""; //server link.com/Login.php
    private Map<String, String> params;

    /**
     * put all variables that needed for login request to Hashmap
     * @param username
     * @param password
     * @param listener
     */
    public LoginRequest(String username, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
