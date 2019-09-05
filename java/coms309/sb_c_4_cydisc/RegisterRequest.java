package coms309.sb_c_4_cydisc;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhanghao on 2017/10/8.
 */

/**
 * @author Zhanghao Wen
 * This class set up a connection between register and network by using RequestQueue
 */
public class RegisterRequest extends StringRequest{
    private static final String REGISTER_REQUEST_URL = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/newAccount.php"; //server link.com/Register.php
    private Map<String, String> params;

    /**
     *  put all variables that needed for register request to Hashmap
     * @param username
     * @param email
     * @param password
     * @param listener
     */
    public RegisterRequest(String username, String email, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
