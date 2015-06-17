package com.jayangche.android.fragment.home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jayangche.android.R;
import com.jayangche.android.activity.MainActivity;
import com.jayangche.android.core.CoreManager;
import com.jayangche.android.model.UserInfo;


public class UserCenterFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ImageView imgAvatar;
    TextView txtAccount;
    TextView txtNickName;
    Button btnModifyNick;
    Button btnLogin;
    ListView listSetting;

    private OnUserCenterInteractionListener mListener;


    public static UserCenterFragment newInstance(String param1, String param2) {
        UserCenterFragment fragment = new UserCenterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserCenterFragment() {

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
        View view = inflater.inflate(R.layout.fragment_user_center,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        txtAccount = (TextView) view.findViewById(R.id.txt_account);
        txtNickName = (TextView) view.findViewById(R.id.txt_nick_name);
        btnModifyNick = (Button) view.findViewById(R.id.btn_modify_nick_name);
        btnModifyNick.setOnClickListener(this);

        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((MainActivity)mListener).getUserCenterInteraction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onLogout() {

        imgAvatar.setVisibility(View.GONE);
        txtAccount.setVisibility(View.GONE);
        txtNickName.setVisibility(View.GONE);
        btnModifyNick.setVisibility(View.GONE);

        btnLogin.setVisibility(View.VISIBLE);
    }

    public void onLogin() {
        UserInfo userInfo = CoreManager.getManager().getCurrUser();
        if (userInfo == null) {
            onLogout();
        }

        // TODO show avatar from path
        txtAccount.setText(userInfo.getName());
        txtNickName.setText(userInfo.getNikeName());
        imgAvatar.setVisibility(View.VISIBLE);
        txtAccount.setVisibility(View.VISIBLE);
        txtNickName.setVisibility(View.VISIBLE);
        btnModifyNick.setVisibility(View.VISIBLE);

        btnLogin.setVisibility(View.GONE);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_modify_nick_name:
                break;
        }
    }


    public interface OnUserCenterInteractionListener {
        
        public void onFragmentInteraction(Uri uri);
    }

}
