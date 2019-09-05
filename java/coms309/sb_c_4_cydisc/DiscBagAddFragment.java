package coms309.sb_c_4_cydisc;
/**
 * @author Brian Newman
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
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

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass that allows the user to add a disc to their disc bag.
 */
public class DiscBagAddFragment extends Fragment {

    private static final String URL_FOR_ADD_DISC = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/addDisc.php";
    private String playerId;

    public DiscBagAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.disc_bag_add_activity, container, false);
        OnDataPass activity = (OnDataPass) getActivity();
        playerId = activity.getPlayerId();

        Button addDisc = (Button) view.findViewById(R.id.btnAddDisc);
        final EditText brand = (EditText) view.findViewById(R.id.etBrand);
        final EditText model = (EditText) view.findViewById(R.id.etModel);
        final EditText speed = (EditText) view.findViewById(R.id.etSpeed);
        final EditText glide = (EditText) view.findViewById(R.id.etGlide);
        final EditText turn = (EditText) view.findViewById(R.id.etTurn);
        final EditText fade = (EditText) view.findViewById(R.id.etFade);

        addDisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDisc(brand.getText().toString(), model.getText().toString(), speed.getText().toString(), glide.getText().toString(), turn.getText().toString(), fade.getText().toString());
            }
        });

        return view;
    }

    private void insertDisc( final String brand, final String model, final String speed, final String glide, final String turn, final String fade) {
        // Tag used to cancel the request
        String cancel_req_tag = "add disc";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_ADD_DISC, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Add Disc Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Add Disc Error: " + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                // hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("playerid", playerId);
                params.put("discbrand", brand);
                params.put("discmodel", model);
                params.put("discspeed", speed);
                params.put("discglide", glide);
                params.put("discturn", turn);
                params.put("discfade", fade);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq, cancel_req_tag);

        Fragment fragment = null;
        Class fragmentClass = DiscBagFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }
}
