package com.rrja.carja.fragment.homemaintenance;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrja.carja.R;
import com.rrja.carja.activity.HomeMaintenanceActivity;
import com.rrja.carja.adapter.MaintenanceAdapter;
import com.rrja.carja.model.TagableElement;

import java.util.ArrayList;
import java.util.List;


public class MaintenanceMainFragment extends BaseElementFragment implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private LinearLayout llloc;
    private TextView txtLoc;
    private RecyclerView recyclerMaintenance;

    private MaintenanceAdapter maintanceAdapter;

    private OnFragmentInteractionListener mListener;


    public static MaintenanceMainFragment newInstance() {
        MaintenanceMainFragment fragment = new MaintenanceMainFragment();
        return fragment;
    }

    public MaintenanceMainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        View t = view.findViewById(R.id.titlebar);

        toolbar = (Toolbar) t;
        llloc = (LinearLayout) toolbar.findViewById(R.id.ll_cur_loc);
        txtLoc = (TextView) llloc.findViewById(R.id.txt_location);
        if (txtLoc != null) {
            txtLoc.setVisibility(View.GONE);
        }
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        ImageView imgAdd = (ImageView) llloc.findViewById(R.id.img_loc);
        imgAdd.setImageResource(R.drawable.icon_add);
        imgAdd.setOnClickListener(this);

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        toolbar.setTitle(R.string.title_activity_maintenance);


        recyclerMaintenance = (RecyclerView) view.findViewById(R.id.recycler_main_maintenance);
        recyclerMaintenance.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerMaintenance.setAdapter(maintanceAdapter);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.img_loc) {
            // TODO switch service choise
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().finish();
            return true;
        }
        return false;
    }


    public interface OnFragmentInteractionListener {

    }

    public List<TagableElement> getOrderContent() {
        if (getActivity() != null) {
            return ((HomeMaintenanceActivity) getActivity()).getOrderContent();
        }
        return new ArrayList<>();
    }

}
