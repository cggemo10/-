package com.rrja.carja.fragment.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.activity.CarManagerActivity;
import com.rrja.carja.activity.FeedbackActivity;
import com.rrja.carja.activity.MainActivity;
import com.rrja.carja.activity.OrderListActivity;
import com.rrja.carja.activity.UserCouponsActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.core.CoreManager;
import com.rrja.carja.model.UserInfo;
import com.rrja.carja.service.FileService;
import com.rrja.carja.utils.DialogHelper;
import com.rrja.carja.utils.ImageUtil;


public class UserCenterFragment extends Fragment implements View.OnClickListener, Handler.Callback {

    ImageView imgAvatar;
    TextView txtAccount;
    TextView txtNickName;
    Button btnModifyNick;
    Button btnLogin;

    RelativeLayout rlOrder;
    LinearLayout llOrderContent;

    LinearLayout llOrderPayed;
    LinearLayout llOrderUnpay;
    LinearLayout llOrderFinished;
    LinearLayout llOrderCancel;

    RelativeLayout rlMyCar;
    RelativeLayout rlCoupons;
    RelativeLayout rlFeedback;


    private UserReceiver userReceiver;
    private OnUserCenterInteractionListener mListener;

    private Handler mHandler;


    public static UserCenterFragment newInstance() {
        UserCenterFragment fragment = new UserCenterFragment();
        return fragment;
    }

    public UserCenterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_center, null);
        initView(view);
        view.setOnClickListener(this);
        return view;
    }

    private void initView(View view) {
        imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);
        txtAccount = (TextView) view.findViewById(R.id.txt_account);
        txtNickName = (TextView) view.findViewById(R.id.txt_nick_name);
        btnModifyNick = (Button) view.findViewById(R.id.btn_modify_nick_name);
        btnModifyNick.setOnClickListener(this);

        btnLogin = (Button) view.findViewById(R.id.btn_show_login);
        btnLogin.setOnClickListener(this);

        rlOrder = (RelativeLayout) view.findViewById(R.id.rl_setting_order);
        rlOrder.setOnClickListener(this);
        llOrderContent = (LinearLayout) view.findViewById(R.id.ll_setting_order_content);

        llOrderPayed = (LinearLayout) view.findViewById(R.id.ll_setting_order_payed);
        llOrderPayed.setOnClickListener(this);
        llOrderUnpay = (LinearLayout) view.findViewById(R.id.ll_setting_order_unpay);
        llOrderUnpay.setOnClickListener(this);
        llOrderFinished = (LinearLayout) view.findViewById(R.id.ll_setting_order_finished);
        llOrderFinished.setOnClickListener(this);
        llOrderCancel = (LinearLayout) view.findViewById(R.id.ll_setting_order_cancel);
        llOrderCancel.setOnClickListener(this);

        rlMyCar = (RelativeLayout) view.findViewById(R.id.rl_setting_mycar);
        rlMyCar.setOnClickListener(this);
        rlCoupons = (RelativeLayout) view.findViewById(R.id.rl_setting_coupons);
        rlCoupons.setOnClickListener(this);
        rlFeedback = (RelativeLayout) view.findViewById(R.id.rl_setting_feedback);
        rlFeedback.setOnClickListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((MainActivity) activity).getUserCenterInteraction();
        registUserReceiver();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        unregistUserReceiver();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLogin();
    }

    private void checkLogin() {
        UserInfo userInfo = CoreManager.getManager().getCurrUser();
        if (userInfo == null) {
            onLogout();
        } else {
            onLogin(userInfo);
        }
    }

    public void onLogout() {

        imgAvatar.setVisibility(View.GONE);
        txtAccount.setVisibility(View.GONE);
        txtNickName.setVisibility(View.GONE);
        btnModifyNick.setVisibility(View.GONE);

        btnLogin.setVisibility(View.VISIBLE);
    }

    public void onLogin(UserInfo userInfo) {

        txtAccount.setText(userInfo.getName());
        txtNickName.setText(userInfo.getNikeName());
        imgAvatar.setVisibility(View.VISIBLE);
        txtAccount.setVisibility(View.VISIBLE);
        txtNickName.setVisibility(View.VISIBLE);
        btnModifyNick.setVisibility(View.VISIBLE);

        btnLogin.setVisibility(View.GONE);

        String avatarPath = userInfo.getAvatarPath();
        try {
            if (avatarPath.endsWith(".png") || avatarPath.endsWith(".jpg")) {
                if (avatarPath.startsWith("http")) {
                    Intent intent = new Intent(getActivity(), FileService.class);
                    intent.setAction(FileService.ACTION_IMG_USER_AVATAR);
                    intent.putExtra("avatar", avatarPath);
                    getActivity().startService(intent);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(avatarPath);
                    Bitmap roundedAvatar = ImageUtil.getRoundedCornerBitmap(bitmap, bitmap.getHeight() / 2);
                    imgAvatar.setImageBitmap(roundedAvatar);
                    return;
                }
            }

            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getAssets().open("user.jpg"));
            Bitmap roundedAvatar = ImageUtil.getRoundedCornerBitmap(bitmap, bitmap.getHeight() / 2);
            imgAvatar.setImageBitmap(roundedAvatar);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        UserInfo currUser = CoreManager.getManager().getCurrUser();
        switch (v.getId()) {
            case R.id.btn_modify_nick_name:
                mListener.modifyNickname();
                break;
            case R.id.img_avatar:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String[] items = new String[]{"相册", "照片"};
                builder.setTitle("更新头像");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            getActivity().startActivityForResult(intent, MainActivity.TAKE_PICTURE);
                        }
                        if (which == 1) {
                            Intent intent = new Intent();
                            intent.setAction("android.media.action.IMAGE_CAPTURE");
                            getActivity().startActivityForResult(intent, MainActivity.MAKE_PICTURE);
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            case R.id.btn_show_login:
                mListener.loginInteraction();
                break;
            case R.id.rl_setting_order: {
                if (currUser != null) {
                    if (llOrderContent.getVisibility() == View.VISIBLE) {
                        llOrderContent.setVisibility(View.GONE);
                    } else {
                        llOrderContent.setVisibility(View.VISIBLE);
                    }
                } else {
                    mListener.loginInteraction();
                }
            }
            break;
            case R.id.ll_setting_order_payed:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {
                    Intent intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra("order_type", "22");
                    startActivity(intent);
                }

                break;
            case R.id.ll_setting_order_unpay:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {
                    Intent intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra("order_type", "11");
                    startActivity(intent);
                }
                break;
            case R.id.ll_setting_order_finished:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {
                    Intent intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra("order_type", "33");
                    startActivity(intent);
                }
                break;
            case R.id.ll_setting_order_cancel:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {
                    Intent intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra("order_type", "44");
                    startActivity(intent);
                }
                break;
            case R.id.rl_setting_mycar:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {
                    Intent intent = new Intent(getActivity(), CarManagerActivity.class);
                    intent.putExtra("order_type", "44");
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.rl_setting_coupons:
                if (currUser == null) {
                    mListener.loginInteraction();
                } else {
                    Intent intent = new Intent(getActivity(), UserCouponsActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.rl_setting_feedback:
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        checkLogin();
        return false;
    }


    public interface OnUserCenterInteractionListener {

        public void loginInteraction();

        public void modifyNickname();
    }

    private void registUserReceiver() {
        if (userReceiver == null) {
            userReceiver = new UserReceiver();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_LOGIN_BY_AUTH);
        filter.addAction(Constant.ACTION_LOGIN_BY_AUTH_ERROR);
        filter.addAction(Constant.ACTION_MODIFY_NICK_NAME);
        filter.addAction(Constant.ACTION_MODIFY_NICK_NAME_ERROR);
        filter.addAction(Constant.ACTION_MODIFY_AVATAR);
        filter.addAction(Constant.ACTION_MODIFY_AVATAR_ERR);
        filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_AVATAR);
        filter.addAction(Constant.ACTION_BROADCAST_DOWNLOAD_IMG_AVATAR_ERR);

        filter.addAction(Constant.ACTION_BORADCAST_APK_DOWNLOAD_PROGRESS);
        filter.addAction(Constant.ACTION_BORADCAST_APK_DOWNLOAD_FAILED);

        getActivity().registerReceiver(userReceiver, filter);
    }

    private void unregistUserReceiver() {
        if (userReceiver == null) {
            return;
        }
        getActivity().unregisterReceiver(userReceiver);
        userReceiver = null;
    }

    private class UserReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            DialogHelper.getHelper().dismissWatting();

            String action = intent.getAction();
            if (Constant.ACTION_LOGIN_BY_AUTH.equals(action)) {

                SharedPreferences sp = context.getSharedPreferences("authsp", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("auth", intent.getStringExtra("auth"));
                edit.putString("tel", intent.getStringExtra("tel"));
                edit.commit();

                mHandler.sendEmptyMessage(0);
            }

            if (Constant.ACTION_MODIFY_NICK_NAME.equals(action)) {
                mHandler.sendEmptyMessage(0);
            }

            if (Constant.ACTION_MODIFY_NICK_NAME_ERROR.equals(action)) {
                Toast.makeText(context, "更新昵称失败，请稍后再试！", Toast.LENGTH_LONG).show();
            }

            if (Constant.ACTION_LOGIN_BY_AUTH_ERROR.equals(action)) {
                // TODO
            }

            if (Constant.ACTION_MODIFY_AVATAR.equals(action)) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(CoreManager.getManager().getCurrUser().getAvatarPath());
                    Bitmap roundedAvatar = ImageUtil.getRoundedCornerBitmap(bitmap, bitmap.getHeight() / 2);
                    imgAvatar.setImageBitmap(roundedAvatar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (Constant.ACTION_MODIFY_AVATAR_ERR.equals(action)) {
                Toast.makeText(context, "更新头像失败，请稍后再试！", Toast.LENGTH_LONG).show();
            }

            if (Constant.ACTION_BROADCAST_DOWNLOAD_IMG_AVATAR.equals(action)) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(CoreManager.getManager().getCurrUser().getAvatarPath());
                    Bitmap roundedAvatar = ImageUtil.getRoundedCornerBitmap(bitmap, bitmap.getHeight() / 2);
                    imgAvatar.setImageBitmap(roundedAvatar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (Constant.ACTION_BROADCAST_DOWNLOAD_IMG_AVATAR_ERR.equals(action)) {
                Toast.makeText(context, "获取头像失败，请稍后再试！", Toast.LENGTH_LONG).show();
            }

            if (Constant.ACTION_BORADCAST_APK_DOWNLOAD_FAILED.equals(action)) {
                // TODO
            }

            if (Constant.ACTION_BORADCAST_APK_DOWNLOAD_PROGRESS.equals(action)) {
                // TODO
            }
        }
    }

}
