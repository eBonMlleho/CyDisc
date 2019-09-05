package coms309.sb_c_4_cydisc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//TODO: add API to this class
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_activity, container, false);
        OnDataPass activity = (OnDataPass) getActivity();
        String username = activity.getUsername();

        /*
        TextView test = (TextView) view.findViewById(R.id.textView3);
        test.setText(username);
        */

        FragmentManager fragmentManager = getChildFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.topFrame, new LeaderboardFragment())
                .add(R.id.middleFrame, new ProfileFragment())
                .add(R.id.bottomFrame, new MapFragment())
                .commit();

        return view;
    }

    public void onStop(Bundle savedInstanceState) {
        super.onDestroy();

        FragmentManager fragmentManager = getChildFragmentManager();

        fragmentManager.beginTransaction()
                .remove(getChildFragmentManager().findFragmentById(R.id.topFrame))
                .remove(getChildFragmentManager().findFragmentById(R.id.middleFrame))
                .remove(getChildFragmentManager().findFragmentById(R.id.bottomFrame))
                .commit();
    }
}