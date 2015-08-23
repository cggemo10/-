package com.rrja.carja.fragment.homemaintenance;

import android.app.Activity;
 import android.app.Fragment;
 import android.net.Uri;
 import android.os.Bundle;
 import android.support.v4.view.ViewPager;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.LinearLayout;
 import android.widget.TextView;

 import com.rrja.carja.R;
 import com.rrja.carja.activity.HomeMaintenanceActivity;

 import java.util.ArrayList;


public class MaintenanceTagServiceFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

     private TextView tv1, tv2, tv3;
     private TextView tvLine;
     private ViewPager viewPager;

     private int lineWidth;
     private ArrayList<View> views;

     private MaintenanceTagActionListener mListener;

     public static MaintenanceTagServiceFragment newInstance(String param1, String param2) {
         MaintenanceTagServiceFragment fragment = new MaintenanceTagServiceFragment();
         return fragment;
     }

     public MaintenanceTagServiceFragment() {
         // Required empty public constructor
     }

     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);


     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_maintenance_tag, container, false);
         initView(view);
         return view;
     }

     private void initView(View view) {
         tv1 = (TextView) view.findViewById(R.id.txt_maintenance_tag1);
         tv1.setOnClickListener(this);
         tv2 = (TextView) view.findViewById(R.id.txt_maintenance_tag2);
         tv2.setOnClickListener(this);
         tv3 = (TextView) view.findViewById(R.id.txt_maintenance_tag3);
         tv3.setOnClickListener(this);
         tvLine = (TextView) view.findViewById(R.id.tv_tag_line);

         viewPager = (ViewPager) view.findViewById(R.id.vp_maintenance);
         HomeMaintenanceActivity.MaintenancePagerAdapter adapter = ((HomeMaintenanceActivity)(getActivity())).getPagerAdapter();
         viewPager.setAdapter(adapter);
         viewPager.addOnPageChangeListener(this);

         views = new ArrayList<>();



         LayoutInflater inflater = getActivity().getLayoutInflater();
         views.add(inflater.inflate(R.layout.item_pagerv_maintenance, null));
         views.add(inflater.inflate(R.layout.item_pagerv_repair, null));
         views.add(inflater.inflate(R.layout.item_pagerv_cosmetology, null));

     }

     @Override
     public void onAttach(Activity activity) {
         super.onAttach(activity);
         mListener = ((HomeMaintenanceActivity) getActivity()).getTagActionListener();
     }

     @Override
     public void onStart() {
         super.onStart();
         int width = tv1.getWidth();
         if (width > 0) {
             lineWidth = width;
             ViewGroup.LayoutParams layoutParams = tvLine.getLayoutParams();
             layoutParams.width = lineWidth;
             tvLine.setLayoutParams(layoutParams);
         }
     }

     @Override
     public void onStop() {
         super.onStop();
     }

     @Override
     public void onDetach() {
         super.onDetach();
         mListener = null;
     }


     @Override
     public void onClick(View v) {
         switch (v.getId()) {
             case R.id.txt_maintenance_tag1:
                 viewPager.setCurrentItem(0, false);
                 break;
             case R.id.txt_maintenance_tag2:
                 viewPager.setCurrentItem(1, false);
                 break;
             case R.id.txt_maintenance_tag3:
                 viewPager.setCurrentItem(2, false);
                 break;

             default:
                 break;
         }
     }


     @Override
     public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
         int s = position * lineWidth + (int) (positionOffset * lineWidth);
         Log.i("haha", s + "");
         LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.MATCH_PARENT);
         layoutParams.width = lineWidth;
         layoutParams.setMargins(s, 0, 0, 0);
         tvLine.setLayoutParams(layoutParams);
     }


     @Override
     public void onPageSelected(int position) {

     }


     @Override
     public void onPageScrollStateChanged(int state) {

     }


     public interface MaintenanceTagActionListener {

         public void onFragmentInteraction(Uri uri);
     }



 }
