/*
package coms309.sb_c_4_cydisc;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Scorecard extends AppCompatActivity {
    private static final String TAG = "SendScoreActivity";
    private static final String URL_FOR_SCORECARD = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/addScore.php";
    private static final String URL_FOR_NEW_SCORECARD = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/newScoreboard.php";
    private static final String URL_FOR_SAVE_SCORES = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/storeScores.php";
    ProgressDialog progressDialog;
    private EditText etScore;
    private Button bSave;
    private Button bNext;
    private Button bPrev;
    private Button scoreTable;
    private int holeNumber;
    private String username;
    private TextView textView;/////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);
        holeNumber = 1;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        etScore = (EditText)findViewById(R.id.EnterScore);
        scoreTable = (Button)findViewById(R.id.scoreTable);
        bSave = (Button)findViewById(R.id.save);
        textView = (TextView)findViewById(R.id.textView);////////////////////////////
        bNext = (Button)findViewById(R.id.next);
        bPrev = (Button)findViewById(R.id.previous);



        // get//////////////////
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");

        newScoreboard();

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendScore(etScore.getText().toString(), username);

            }
        });

        scoreTable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(Scorecard.this, scoreTable.class);
                startActivity(i);
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holeNumber == 18)
                {
                    saveScores();
                }
                else {
                    holeNumber++;
                    textView.setText("HOLE: # " + holeNumber);
                    etScore.setText("");
                    etScore.setHint("Enter Score Here~!");
                }

            }
        });


        bPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holeNumber --;
                textView.setText("HOLE: # " + holeNumber);
                etScore.setText("");
                etScore.setHint("Enter Score Here~!");
            }
        });

    }

    private void newScoreboard() {
        String cancel_req_tag = "sending";
        progressDialog.setMessage("Sending your score......");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_NEW_SCORECARD, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d(TAG, "New Scorecard Response: " + response.toString());
                hideDialog();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if (success) {

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void saveScores() {
        String cancel_req_tag = "sending";
        progressDialog.setMessage("Sending your score......");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_SAVE_SCORES, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d(TAG, "New Scorecard Response: " + response.toString());
                hideDialog();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if (success) {

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    private void sendScore(final String score, final String username){
        String cancel_req_tag = "sending";
        progressDialog.setMessage("Sending your score......");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_SCORECARD, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        holeNumber ++;
                        textView.setText("HOLE: # " + holeNumber);
                        etScore.setText("");
                        etScore.setHint("Enter Score Here~!");
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("score", score);
                params.put("username", username);
                params.put("hole", Integer.toString(holeNumber));
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }





    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
*/