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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * @author Brian Newman
 * A simple {@link Fragment} subclass that allows the user to view their uploaded disc and link to
 * upload a new one.
 */
public class DiscBagFragment extends Fragment {

    private static final String URL_FOR_DISCS = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/getDiscs_updated.php";
    private String playerId;

    public DiscBagFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.disc_bag_activity, container, false);
        OnDataPass activity = (OnDataPass) getActivity();
        playerId = activity.getPlayerId();

        ImageButton discImage = (ImageButton) view.findViewById(R.id.imageButton);

        getPlayerDiscs(view);

        discImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;

                try {
                    fragment = (Fragment) DiscBagAddFragment.class.newInstance();
                } catch(Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });

        return view;
    }

    private void getPlayerDiscs(final View view) {
        String cancel_req_tag = "load discs";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_DISCS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    ListView listView = (ListView) view.findViewById(R.id.list);
                    List<String> discs = new ArrayList<String>();

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObject = jsonArray.getJSONObject(i);

                        String discBrand = jObject.getString("discBrand");
                        String discName = jObject.getString("discName");
                        String discSpeed = jObject.getString("discSpeed");
                        String discGlide = jObject.getString("discGlide");
                        String discTurn = jObject.getString("discTurn");
                        String discFade = jObject.getString("discFade");

                        String insert = discBrand + " " + discName + "-<" + discSpeed + "," + discGlide + "," + discTurn + "," + discFade + ">";

                        discs.add(insert);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, discs);
                    listView.setAdapter(adapter);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("playerid", playerId);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq, cancel_req_tag);
    }
}