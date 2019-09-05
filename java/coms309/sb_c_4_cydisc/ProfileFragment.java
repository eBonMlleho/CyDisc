package coms309.sb_c_4_cydisc;
/**
 * @author Brian Newman
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass that allows the user to view their profile containing their
 * top scores, discs, and friends.
 */
public class ProfileFragment extends Fragment {
    private static final String URL_FOR_DISCS = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/getDiscs_updated.php";
    private static final String URL_FOR_SCORES = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/getProfileScores.php";
    private static final String URL_FOR_PROFILE_PICTURE = "http://proj-309-sb-c-4.cs.iastate.edu/davidFolder/displayProfilePicture.php";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    OnDataPass dataPasser;
    private String playerId;
    String mCurrentPhotoPath;
    ImageView proPic;

    /**
     * Empty constructor for a ProfileFragment.
     */
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Constructor for an instance of a ProfileFragment.
     * @return an instance of a ProfileFragment.
     */
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_activity, container, false);
        OnDataPass activity = (OnDataPass) getActivity();
        String username = activity.getUsername();
        playerId = activity.getPlayerId();

        TextView playerName = (TextView) view.findViewById(R.id.tvPlayerName);
        TextView viewAllDisc = (TextView) view.findViewById(R.id.tvViewAllDisc);
        TextView viewMoreScores = (TextView) view.findViewById(R.id.textView7);
        TextView changePicture = (TextView) view.findViewById(R.id.tvChange);
        proPic = (ImageView) view.findViewById(R.id.ivProfilePicture);

        playerName.setText(username);
        getPlayerDiscs(view);
        getPlayerTopScores(view);
        displayProfilePicture();

        viewAllDisc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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
        });

        viewMoreScores.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Fragment fragment = null;

                try {
                    fragment = (Fragment) LeaderboardFragment.class.newInstance();
                } catch(Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });

        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass = CameraFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        });

        return view;
    }

    private void displayProfilePicture() {
        // Tag used to cancel the request
        String cancel_req_tag = "profile picture";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_PROFILE_PICTURE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        String encodedImage = jObj.getString("photo");
                        byte[] decodedString = Base64.decode(encodedImage, Base64.URL_SAFE);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        proPic.setImageBitmap(decodedByte);
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
                params.put("playerid", playerId);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void getPlayerDiscs(final View view) {
        String cancel_req_tag = "load discs";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_DISCS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    ListView listView = (ListView) view.findViewById(R.id.list_mostUsedDiscs);
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
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("playerid", playerId);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void getPlayerTopScores(final View view) {
        String cancel_req_tag = "top scores";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_SCORES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                ListView listView = (ListView) view.findViewById(R.id.list_topScores);
                List<String> topScores = new ArrayList<String>();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String courseName = jObject.getString("coursename");
                        String courseCity = jObject.getString("coursecity");
                        String courseState = jObject.getString("coursestate");
                        String totalScore = jObject.getString("totalscore");
                        String sessionDate = jObject.getString("sessiondate");
                        String insert = courseName + " - " + courseCity + ", " + courseState + ": Total Score->" + totalScore + ", Date->" + sessionDate;
                        topScores.add(insert);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(topScores.isEmpty()){
                    String message = "There are no top scores to show";
                    topScores.add(message);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, topScores);
                listView.setAdapter(adapter);
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
                params.put("playerid", playerId);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    // To possibly be used in CameraFragment instead?
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // To possibly be used in CameraFragment instead?
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}