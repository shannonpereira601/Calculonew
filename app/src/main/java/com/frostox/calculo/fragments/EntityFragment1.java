package com.frostox.calculo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.frostox.calculo.Nodes.Notes;
import com.frostox.calculo.Nodes.Standards;
import com.frostox.calculo.Nodes.Subjects;
import com.frostox.calculo.Nodes.Topics;
import com.frostox.calculo.activities.Home;
import com.frostox.calculo.activities.McqActivity;
import com.frostox.calculo.activities.McqActivityold;
import com.frostox.calculo.adapters.Data;

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
public class EntityFragment1 extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "current";
    List<Data> data;

    private RecyclerView recyclerView;

    Firebase ref;

    private FirebaseRecyclerAdapter recyclerAdapter;

    private RecyclerView.LayoutManager layoutManager;

    // TODO: Rename and change types of parameters
    private String id;
    private String current = "Standard";

    private String[] courses = {"10th", "CET Foundation", "11th Science", "12th Sci"};
    private String[] key;
    private String name, numberofq, difficulty;
    boolean rvexists;

    private OnFragmentInteractionListener mListener;

    private static MyClickListener myClickListener;

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
        homeActivity = (Home) this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rvexists = true;
        View view = inflater.inflate(R.layout.fragment_standard, container, false);

        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
            Log.d("onnCreate", id);
            current = getArguments().getString(ARG_PARAM2);
        }

        ref = new Firebase("https://extraclass.firebaseio.com/");
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(homeActivity));

        if (current.equals("Standard")) {
            final Firebase standardref = ref.child("courses");
            getKey(standardref);
            recyclerAdapter = new FirebaseRecyclerAdapter<Standards, DataObjectHolder>(Standards.class, R.layout.recycler_view_item, DataObjectHolder.class, standardref) {
                @Override
                public void populateViewHolder(DataObjectHolder dataObjectHolder, final Standards standards, final int position) {
                    dataObjectHolder.label.setText(standards.getName());
                    dataObjectHolder.label.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name = standards.getName();
                            myClickListener.onItemClick(position, v);

                        }
                    });
                }
            };
        } else if (current.equals("Subject")) {

            Firebase subjectref = ref.child("subjects");
            Query query = subjectref.orderByChild("course").equalTo(id);
            getKey(query);

            recyclerAdapter = new FirebaseRecyclerAdapter<Subjects, DataObjectHolder>(Subjects.class, R.layout.recycler_view_item, DataObjectHolder.class, query) {
                @Override
                public void populateViewHolder(DataObjectHolder dataObjectHolder, final Subjects subject, final int position) {
                    //   if(subject.getCourse().equals(id)) {
                    dataObjectHolder.label.setText(subject.getName());
                    //  }
                    dataObjectHolder.label.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name = subject.getName();
                            myClickListener.onItemClick(position, v);
                        }
                    });
                }
            };
        } else if (current.equals("Topic")) {
            Firebase topicref = ref.child("topics");
            Query query = topicref.orderByChild("subject").equalTo(id);
            getKey(query);

            recyclerAdapter = new FirebaseRecyclerAdapter<Topics, DataObjectHolder>(Topics.class, R.layout.recycler_view_item, DataObjectHolder.class, query) {
                @Override
                public void populateViewHolder(DataObjectHolder dataObjectHolder, final Topics topic, final int position) {


                    //   if(subject.getCourse().equals(id)) {
                    dataObjectHolder.label.setText(topic.getName());
                    //  }
                    dataObjectHolder.label.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name = topic.getName();
                            myClickListener.onItemClick(position, v);
                        }
                    });

                }
            };
        } else if (current.equals("MCQ")) {
            rvexists = false;
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
            ll.setVisibility(View.VISIBLE);
            Button Proceed = (Button) view.findViewById(R.id.PROCEED);
            Spinner spinnernoq = (Spinner) view.findViewById(R.id.spinnernoq);
            Spinner spinnerdifficulty = (Spinner) view.findViewById(R.id.spinnerdifficulty);

            final List<String> noq = new ArrayList<String>();
            noq.add("10");
            noq.add("20");
            noq.add("30");
            noq.add("40");
            noq.add("50");

            final List<String> difficultyal = new ArrayList<String>();
            difficultyal.add("1");
            difficultyal.add("2");
            difficultyal.add("3");
            difficultyal.add("4");

            ArrayAdapter<String> noqAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, noq);
            noqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnernoq.setAdapter(noqAdapter);

            ArrayAdapter<String> difficltyAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, difficultyal);
            difficltyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerdifficulty.setAdapter(difficltyAdapter);

            spinnernoq.setOnItemSelectedListener(this);
            spinnerdifficulty.setOnItemSelectedListener(this);

            Toast.makeText(getContext(), "Please select No. of Questions and Difficulty", Toast.LENGTH_LONG).show();

            Proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), McqActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("difficulty", difficulty);
                    intent.putExtra("noq", numberofq);
                    startActivity(intent);
                }
            });

        } else if (current.equals("Note")) {
            Firebase noteref = ref.child("notes");
            Query query = noteref.orderByChild("topic").equalTo(id);
            getKey(query);

            recyclerAdapter = new FirebaseRecyclerAdapter<Notes, DataObjectHolder>(Notes.class, R.layout.recycler_view_item, DataObjectHolder.class, query) {
                @Override
                public void populateViewHolder(DataObjectHolder dataObjectHolder, final Notes note, final int position) {

                    //   if(subject.getCourse().equals(id)) {
                    dataObjectHolder.label.setText(note.getName());
                    //  }
                    dataObjectHolder.label.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name = note.getName();
                            myClickListener.onItemClick(position, v);
                        }
                    });

                }
            };
        }
        if (rvexists) {
            recyclerView.setAdapter(recyclerAdapter);
        }

        return view;

    }


    public void getKey(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            int count = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int length = (int) dataSnapshot.getChildrenCount();
                key = new String[length];

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    key[count] = postSnapshot.getKey();
                    count++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public void onResume() {

        super.onResume();
        setOnItemClickListener(new MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (key[position] != null) {
                    homeActivity.navNext(key[position], name);
                }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rvexists) {
            recyclerAdapter.cleanup();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinnernoq) {
            numberofq = parent.getItemAtPosition(position).toString();
        } else if (spinner.getId() == R.id.spinnerdifficulty) {
            difficulty = parent.getItemAtPosition(position).toString();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView label;


        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.recycler_view_item_text);
        }

    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
