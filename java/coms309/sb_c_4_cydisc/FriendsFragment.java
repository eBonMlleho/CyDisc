package coms309.sb_c_4_cydisc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//TODO: add API to this class
public class FriendsFragment extends Fragment {


    public FriendsFragment() {
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
        View view = inflater.inflate(R.layout.friends_activity, container, false);

        String[] myItems = {"Tom Brady", "Marshawn Lynch", "Tiger Woods", "Michael Jackson"};
        String[] myItems2 = {"Tom Brady", "Marshawn Lynch", "Tiger Woods", "Michael Jackson", "Bob Brown", "Colonel Sanders", "Michael Scott", "Leslie Knope"};
        ListView listView = (ListView) view.findViewById(R.id.list_recentFriends);
        ListView listView2 = (ListView) view.findViewById(R.id.list_allFriends);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, myItems);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, myItems2);
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter2);

        return view;
    }

}