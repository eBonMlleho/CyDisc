package coms309.sb_c_4_cydisc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhanghao Wen
 * Ask user information that required for create a new account
 */
public class RegisterFragment extends Fragment {
    private String TAG = "RegisterActivity";
    private static final String REGISTER_REQUEST_URL = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/newAccount.php"; //server link.com/Register.php

    /**
     *  Required empty public constructor
     */
    public RegisterFragment() { /* Required empty public constructor */ }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.register_activity, container, false);
        final EditText userName = (EditText) view.findViewById(R.id.etUsername);
        final EditText password = (EditText) view.findViewById(R.id.etPassword);
        final EditText email = (EditText) view.findViewById(R.id.etEmail);
        Button register = (Button) view.findViewById(R.id.bRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(userName.getText().toString(), email.getText().toString(), password.getText().toString() );
            }
        });

        return view;
    }

    private void registerUser(final String username,  final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";
        StringRequest strReq = new StringRequest(Request.Method.POST, REGISTER_REQUEST_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        String username = jObj.getString("username"); /** 'name' could be 'username'? **/

                        Bundle data = new Bundle();

                        data.putString("username", username); /** 'name' could be 'username'? **/

                        Fragment fragment = LoginFragment.newInstance();
                        Class fragmentClass = LoginFragment.class;
                        fragment.setArguments(data);

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    /**
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
    **/
}