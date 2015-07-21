package com.rrja.carja.fragment.car;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrja.carja.R;
import com.rrja.carja.adapter.CarBrandAdapter;
import com.rrja.cja.view.recycler.stickyside.LayoutManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CarSeriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CarSeriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarBrandFragment extends Fragment {

    private int mHeaderDisplay;

    private OnFragmentInteractionListener mListener;

    private RecyclerView brandRecycleList;
    private CarBrandAdapter mAdapter;


    public static CarSeriesFragment newInstance(String param1, String param2) {
        return new CarSeriesFragment();
    }

    public CarBrandFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_brand, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeaderDisplay = getResources().getInteger(R.integer.default_header_display);

        brandRecycleList = (RecyclerView) view.findViewById(R.id.recycler_car_brand);
        brandRecycleList.setLayoutManager(new LayoutManager(getActivity()));
        mAdapter = new CarBrandAdapter(getActivity());
        mAdapter.setHeaderDisplay(mHeaderDisplay);
        brandRecycleList.setAdapter(mAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void smoothScrollToPosition(int position) {
        brandRecycleList.smoothScrollToPosition(position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
