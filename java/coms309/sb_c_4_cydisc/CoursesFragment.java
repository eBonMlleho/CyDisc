package coms309.sb_c_4_cydisc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//TODO: add API to this class
public class CoursesFragment extends Fragment {


    public CoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_activity, container, false);

        String[] myItems = {"Carroll Marty Disc Golf Course", "Stable Run Disc Golf Course", "Legacy Disc Golf Course", "Neverland Disc Golf Course"};
        ListView listView = (ListView) view.findViewById(R.id.list_courses);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, myItems);
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }

}