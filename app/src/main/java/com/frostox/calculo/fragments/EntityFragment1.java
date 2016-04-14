package com.frostox.calculo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.frostox.calculo.Nodes.Courses;
import com.frostox.calculo.Nodes.Subjects;
import com.frostox.calculo.activities.Home;
import com.frostox.calculo.adapters.Data;
import com.frostox.calculo.adapters.RVTask;
import com.frostox.calculo.adapters.RecyclerViewAdapter;
import com.frostox.calculo.pulled_sourses.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import calculo.frostox.com.calculo.R;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntityFragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "columnName";
    List<Data> data;

    private RecyclerView recyclerView;

    Firebase ref;

    private RecyclerViewAdapter recyclerViewAdapter;

    private RecyclerView.LayoutManager layoutManager;

    // TODO: Rename and change types of parameters
    private Long id;
    private String columnName;

    private String[] courses = {"10th","CET Foundation","11th Science","12th Sci"};
    private String[] subjects;

    private OnFragmentInteractionListener mListener;

    Home homeActivity;

    public EntityFragment1() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntityFragment.
     */
    // TODO: Rename and change types and number of parameters
 /*   public static EntityFragment newInstance(Long param1, String param2) {
        EntityFragment fragment = new EntityFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
       //     id = getArguments().getLong(ARG_PARAM1);
            columnName = getArguments().getString(ARG_PARAM2);
        }


        homeActivity = (Home) this.getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_standard, container, false);
        ref = new Firebase("https://extraclass.firebaseio.com/");

        // Query query = ref.orderByChild("name").equalTo("10th");

        Firebase cref = ref.child("courses");
        Firebase sref = ref.child("subjects");

        cref.addValueEventListener(new ValueEventListener() {
            int count = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int lenghth = (int) dataSnapshot.getChildrenCount();
                courses = new String[lenghth];


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    Courses course = postSnapshot.getValue(Courses.class);
                    courses[count] = course.getName();
                    System.out.println("onncourses " + " " + course.getName() + " " + key);
                    count++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



        sref.addValueEventListener(new ValueEventListener() {
            int count = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int lenghth = (int) dataSnapshot.getChildrenCount();
                subjects = new String[lenghth];

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Subjects subject = postSnapshot.getValue(Subjects.class);
                    subjects[count] = subject.getName();
                    System.out.println("onnsubject " + subject.getName() + " " + subject.getCourse());
                    count++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(homeActivity);
        recyclerView.setLayoutManager(layoutManager);

        data = getdata();

        recyclerViewAdapter = new RecyclerViewAdapter(data);

        recyclerView.setAdapter(recyclerViewAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL);

        recyclerView.addItemDecoration(itemDecoration);
        Log.d("onnTest", "1.5");
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Data datatext = data.get(position);
                String nme = datatext.text;
                homeActivity.navNext(nme);
                Log.d("Check", "onnResume clld" + nme);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public List<Data> getdata() {
        List<Data> names = new ArrayList<>();
        Log.d("onnTest","first");

        for (int i = 0; i < courses.length; i++) {
            Data current = new Data(courses[i]);
            current.text = courses[i];
            names.add(current);
        }
        return names;
    }
}
