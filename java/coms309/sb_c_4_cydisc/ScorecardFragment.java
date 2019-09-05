package coms309.sb_c_4_cydisc;
/**
 * @author David Kirhenbaum
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.INotificationSideChannel;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

/**
 * This class will print the names and scores of up to 4 players in the same lobby as you in a table.
 */
public class ScorecardFragment extends Fragment  {
    TableLayout scoreTable;
    TextView test;
    SocketManager socket;

    /**
     * Create an empty ScorecardFragment object
     */
    public ScorecardFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new ScorecardFragment and recieve and instance of it back
     * @return Instance of the ScorecardFragment
     */
    public static ScorecardFragment newInstance() {
        return new ScorecardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.scorecard, container, false);
        scoreTable = rootView.findViewById(R.id.scoreTable);
        socket = (SocketManager) getArguments().getSerializable("socket");
        test = rootView.findViewById(R.id.test);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createTable(rootView);
            }
        }, 200);
        return rootView;
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
     * Puts all the names and score information in the table. Gets the information from SocketManager class
     * @param rootView The main View of the Scorecard.xml page.
     */
    private void createTable(View rootView)
    {
        TextView currentNode;
        TableRow currentRow;
        JSONArray scoreArray = socket.scores;
        int index = 0;
        for(int i = 0; i < 4; i++)
        {
            String textViewId = "name" + i;
            int resId = getResources().getIdentifier(textViewId, "id", getActivity().getPackageName());
            TextView tv = rootView.findViewById(resId);
            try{
                tv.setText(scoreArray.getString(i));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        index = 4;
        for(int i = 0; i < 4; i++)
        {
            for(int j = 1; j < 19; j++)
            {
                String textViewId = "t" + j + i ;
                int resId = getResources().getIdentifier(textViewId, "id", getActivity().getPackageName());
                TextView tv = rootView.findViewById(resId);
                try {
                    tv.setText(scoreArray.getString(index));
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
                index++;
            }
        }


        /*for(int i = 1; i < 19; i++) {
            currentRow = (TableRow) scoreTable.getChildAt(i);
            for(int j = 0; j < 4; j++)
            {
                currentNode = (TextView) currentRow.getChildAt(j);
                currentNode.setText("hi");
            }
        }*/

    }


}