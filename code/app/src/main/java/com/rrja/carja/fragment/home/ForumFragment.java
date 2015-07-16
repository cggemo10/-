package com.rrja.carja.fragment.home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rrja.carja.R;
import com.rrja.carja.activity.MainActivity;
import com.rrja.carja.adapter.ForumAdapter;


public class ForumFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnForumInteractionListener mListener;

    RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;

    private static ForumFragment fragment = new ForumFragment();


    public static ForumFragment getFragment() {
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ForumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forum, container, false);

         swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_forum);

        RecyclerView list = (RecyclerView) root.findViewById(R.id.list_forum);



        mLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(mLayoutManager);

        list.setHasFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(new ForumAdapter((getActivity())));



        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((MainActivity)activity).getForumInteraction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public interface OnForumInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
