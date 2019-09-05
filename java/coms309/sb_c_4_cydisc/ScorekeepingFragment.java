package coms309.sb_c_4_cydisc;
/**
 * @author David Kirhenbaum
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.koushikdutta.async.parser.JSONArrayParser;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class will manage the fragment that takes scores from the user and allows them to navigate other features of the scorecard,
 */
public class ScorekeepingFragment extends Fragment implements View.OnClickListener {

    Button nextHole;
    Button sendScore;
    TextView tvHole;
    Button openScorecard;
    TextView holeOutput;
    EditText etScore;
    Button prevHole;
    Button submitGame;
    TextView tvLobbyId;
    int holeNumber;
    SocketManager socket;
    String username;


    /**
     * Empty ScorekeepingFragment constructor
     */
    public ScorekeepingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){

        super.onAttach(context);
        Activity a;
        if(context instanceof Activity){
            a = (Activity) context;
        }
    }

    /**
     * Constructor for an instance of ScorekeepingFragment
     * @return Instance of ScorekeepingFramgent
     */
    public static ScorekeepingFragment newInstance() {
        return new ScorekeepingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scorekeeping, container, false);
        openScorecard = rootView.findViewById(R.id.openScorecard);
        nextHole = rootView.findViewById(R.id.bNext);
        prevHole = rootView.findViewById(R.id.bPrevious);
        etScore = rootView.findViewById(R.id.etScore);
        sendScore = rootView.findViewById(R.id.newGame);
        tvHole = rootView.findViewById(R.id.tvHole);
        holeOutput = rootView.findViewById(R.id.tvHole);
        tvLobbyId = rootView.findViewById(R.id.tvLobbyId);
        submitGame = rootView.findViewById(R.id.submitGame);
        holeNumber = 1;
        username = getArguments().getString("username");
        socket = (SocketManager) getArguments().getSerializable("socket");

        tvLobbyId.setText(socket.socketLobbyId);
        openScorecard.setOnClickListener(this);
        nextHole.setOnClickListener(this);
        prevHole.setOnClickListener(this);
        submitGame.setOnClickListener(this);
        sendScore.setOnClickListener(this);

        return rootView;
    }

    /**
     * Onclick of all the buttons on the frargment
     * @param v Main View of the framgent
     */
    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        ScorecardFragment scorecardFragment;
        FragmentManager fragmentManager;
        switch (v.getId()) {

            case R.id.openScorecard:


                scorecardFragment = new ScorecardFragment();
                try {
                    fragment = scorecardFragment.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bundle.putSerializable("socket", socket);
                fragment.setArguments(bundle);
                socket.getScores(socket.socketLobbyId);

                // insert the fragment, replacing any existing fragment(s)
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                break;
            case R.id.bNext:
                holeNumber++;
                if (holeNumber == 19) {
                    holeNumber = 1;
                }
                holeOutput.setText("Hole " + holeNumber);
                break;
            case R.id.bPrevious:
                holeNumber--;
                if (holeNumber == 0) {
                    holeNumber = 18;
                }
                holeOutput.setText("Hole " + holeNumber);
                break;

            case R.id.submitGame:
                scorecardFragment = new ScorecardFragment();
                try {
                    fragment = scorecardFragment.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bundle.putSerializable("socket", socket);
                fragment.setArguments(bundle);
                socket.getScores(socket.socketLobbyId);

                // insert the fragment, replacing any existing fragment(s)
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
                break;

            case R.id.newGame:
                holeNumber++;
                if(holeNumber == 19)
                {
                    holeNumber = 1;
                }
                holeOutput.setText("Hole " + holeNumber);
                String score = etScore.getText().toString();
                socket.sendScore(holeNumber - 1, Integer.parseInt(score), username);

        }
    }
 };





