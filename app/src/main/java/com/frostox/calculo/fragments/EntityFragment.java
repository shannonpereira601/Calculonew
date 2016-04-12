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
import android.webkit.WebView;

import com.firebase.client.Firebase;
import com.frostox.calculo.activities.Home;
import com.frostox.calculo.adapters.RecyclerViewAdapter;
import com.frostox.calculo.interfaces.EntityGetter;
import com.frostox.calculo.pulled_sourses.DividerItemDecoration;

import java.util.List;

import calculo.frostox.com.calculo.R;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EntityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EntityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntityFragment<K extends EntityGetter, L extends AbstractDao<K, Long>> extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "columnName";

    private RecyclerView recyclerView;

    Firebase ref;

    private RecyclerViewAdapter recyclerViewAdapter;

    private RecyclerView.LayoutManager layoutManager;

    L dao;

    // TODO: Rename and change types of parameters
    private Long id;
    private String columnName;

    private OnFragmentInteractionListener mListener;

    Home homeActivity;

    public EntityFragment() {
        // Required empty public constructor
    }


    public void setDao(L dao) {
        this.dao = dao;
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
            id = getArguments().getLong(ARG_PARAM1);
            columnName = getArguments().getString(ARG_PARAM2);
        }

        homeActivity = (Home) this.getActivity();


    }

    List<K> entities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_standard, container, false);
        Firebase.setAndroidContext(getContext());
        ref = new Firebase("https://extraclass.firebaseio.com/courses");
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(homeActivity);
        recyclerView.setLayoutManager(layoutManager);

        QueryBuilder queryBuilder = dao.queryBuilder();
        if (id != null) {
            Property[] properties = dao.getProperties();
            for (int i = 0; i < properties.length; i++) {
                if (properties[i].name.equals(columnName)) {//checks which columnNme from bundle is pssed
                    queryBuilder.where(properties[i].eq(id));//gets ll vlues under the columns
                    Log.d("onnCreteview", "" + properties[i].name);
                    break;
                }
            }
        }

        Query query = queryBuilder.build();

        entities = query.list();
        recyclerViewAdapter = new RecyclerViewAdapter(entities);

        recyclerView.setAdapter(recyclerViewAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL);

        recyclerView.addItemDecoration(itemDecoration);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((RecyclerViewAdapter) recyclerViewAdapter).setOnItemClickListener(new RecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                String nme = entities.get(position).getName();
                homeActivity.navNext(entities.get(position).getId(), nme);
                Log.d("Check", "onnResume clld" + nme);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
