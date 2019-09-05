/*package coms309.sb_c_4_cydisc;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class scoreTable extends AppCompatActivity {

    private static final String URL_GET_SCORE = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/getScores.php";
    ProgressDialog progressDialog;
    private static final String TAG = "scoreTable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

    }

    private void getScores() {
        String cancel_req_tag = "sending";
        progressDialog.setMessage("Sending your score......");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET, URL_GET_SCORE, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d(TAG, "New Scorecard Response: " + response.toString());
                hideDialog();
                try {

                    JSONObject jObj = new JSONObject(response);
                     boolean success = jObj.getBoolean("success");

                    if (success) {
                        TableLayout table = (TableLayout) findViewById(R.id.table);
                        for (int i = 0; i <2; i++) {
                            //TableRow r1 =  new TableRow(this);

                        }
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