package coms309.sb_c_4_cydisc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Zhanghao Wen
 * This class allow user to log in if he/she already have a account, and also provide a register button if not registered yet.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/login_updated.php";
	
    OnDataPass dataPasser;
    Activity activity;
    HandlerPasser passer;

    private String username;
    TextClicked mCallback;

    /**
     * Empty constructor for a LoginFragment.
     */
    public LoginFragment() { /** Required empty public constructor **/ }

    /**
     * Constructor for an instance of a LoginFragment.
     * @return an instance of a LoginFragment.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    /**
     * Transfer data between the different fragments.
     */
    public void passData(Bundle data) {
        dataPasser.onDataPass(data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_activity, container, false);

        TextView register = (TextView) view.findViewById(R.id.tvRegister);
        final EditText username = (EditText) view.findViewById(R.id.etUsername);
        final EditText password = (EditText) view.findViewById(R.id.etPassword);
        Button login = (Button) view.findViewById(R.id.bLogin);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Fragment fragment = null;

                try {
                    fragment = (Fragment) RegisterFragment.class.newInstance();
                } catch(Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(username.getText().toString(), password.getText().toString());
            }
        });

        return view;
    }



    private void loginUser( final String username, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                // hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        // Launch User activity
                        String playerId = jObj.getString("playerid");
                        String email = jObj.getString("email");
                        String username = jObj.getString("username");

                        Bundle data = new Bundle();
                        data.putString("playerid", playerId);
                        data.putString("email", email);
                        data.putString("username", username);



                        Fragment fragment = HomeFragment.newInstance();
                        Class fragmentClass = HomeFragment.class;
                        fragment.setArguments(data);

                        passData(data);
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                            fragment.setArguments(data);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                // hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    /**
     * interface TextClicked
     */
    public interface TextClicked{
        public void sendText(String name);
    }

    @Override
    public void onAttach(Context context){

        super.onAttach(context);
        Activity a;
        if(context instanceof Activity){
            a = (Activity) context;
            dataPasser = (OnDataPass) a;
        }
    }

    /**
     * Helper method
     */
    public void onDetach(){
        mCallback = null;
        super.onDetach();
    }


    /*
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
     */
}