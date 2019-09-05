package coms309.sb_c_4_cydisc;
/**
 * @author David Kirhenbaum
 */
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Create a new game, either by joining an existing lobby or creating your own
 *
 */
public class NewGame extends Fragment  implements  View.OnClickListener{

    Button newGameButton;
    Button joinGameButton;
    TextView textViewLobbyId;
    EditText editTextLobbyId;
    SocketManager socket;
    String username;

    /**
     * Empty NewGame constructor
     */
    public NewGame() {
        // Required empty public constructor
    }


    /**
     * Constructor for instance of a new game
     * @return Instance of a NewGame
     */
    public static NewGame newInstance() {
        return new NewGame();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_game, container, false);
        newGameButton = rootView.findViewById(R.id.newGame);
        joinGameButton = rootView.findViewById(R.id.joinGame);
        editTextLobbyId = rootView.findViewById(R.id.etLobbyId);
        textViewLobbyId = rootView.findViewById(R.id.tvLobbyId);

        username = getArguments().getString("username");
        textViewLobbyId.setText(username);
        newGameButton.setOnClickListener(this);
        joinGameButton.setOnClickListener(this);

        socket = new SocketManager();


        return rootView;
    }

    /**
     * Onclick for the two buttons on this page
     * @param v Main view of the fragment
     */
    @Override
    public void onClick(View v)
    {
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        ScorekeepingFragment scorekeepingFragment = new ScorekeepingFragment();
        Bundle bundle = new Bundle();

        switch (v.getId())
        {

            case R.id.newGame:
                socket.createLobby(username);


                try {
                    fragment =  ScorekeepingFragment.newInstance();
                } catch(Exception e) {
                    e.printStackTrace();
                }

                bundle.putSerializable("socket", socket);
                bundle.putString("username" , username);
                fragment.setArguments(bundle);


                // insert the fragment, replacing any existing fragment(s)

                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                break;
            case R.id.joinGame:
                socket.joinLobby(editTextLobbyId.getText().toString(), username);
                try {
                    fragment =  ScorekeepingFragment.newInstance();
                } catch(Exception e) {
                    e.printStackTrace();
                }

                bundle.putSerializable("socket", socket);
                bundle.putString("username" , username);
                fragment.setArguments(bundle);


                // insert the fragment, replacing any existing fragment(s)
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
    }


}
