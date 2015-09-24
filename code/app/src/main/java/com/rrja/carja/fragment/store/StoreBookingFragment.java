package com.rrja.carja.fragment.store;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rrja.carja.R;
import com.rrja.carja.activity.StoreReservationDetalActivity;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.fragment.BaseElementFragment;

import java.util.Calendar;

public class StoreBookingFragment extends BaseElementFragment implements View.OnClickListener, View.OnFocusChangeListener, TextView.OnEditorActionListener {

    private EditText edName;
    private EditText edTel;
    private TextView txtTime;
    private EditText edLoc;

    private ImageView nameDel;
    private ImageView telDel;
    private ImageView locDel;

    private int year = -1, month = -1, day = -1, hour = -1, minute = -1;

    private AppCompatButton btnCommit;

    private OnBookActionListener mListener;
    private StoreBookReceiver mReceiver;

    public static StoreBookingFragment newInstance() {
        StoreBookingFragment fragment = new StoreBookingFragment();
        return fragment;
    }

    public StoreBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_booking, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {

        nameDel = (ImageView) view.findViewById(R.id.img_book_user_del);
        nameDel.setOnClickListener(this);

        telDel = (ImageView) view.findViewById(R.id.img_book_tel_del);
        telDel.setOnClickListener(this);

        locDel = (ImageView) view.findViewById(R.id.img_book_area_del);
        locDel.setOnClickListener(this);

        edName = (EditText) view.findViewById(R.id.ed_store_book_user);
        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    nameDel.setVisibility(View.GONE);
                } else {
                    nameDel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edName.setOnFocusChangeListener(this);
        edName.setOnFocusChangeListener(this);

        edTel = (EditText) view.findViewById(R.id.ed_store_book_tel);
        edTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    telDel.setVisibility(View.VISIBLE);
                } else {
                    telDel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edTel.setOnFocusChangeListener(this);
        edTel.setOnEditorActionListener(this);

        txtTime = (TextView) view.findViewById(R.id.txt_store_book_time);
        txtTime.setOnClickListener(this);

        edLoc = (EditText) view.findViewById(R.id.ed_store_book_area);
        edLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    locDel.setVisibility(View.VISIBLE);
                } else {
                    locDel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edLoc.setOnFocusChangeListener(this);
        edLoc.setOnEditorActionListener(this);

        btnCommit = (AppCompatButton) view.findViewById(R.id.btn_conform_booking);
        btnCommit.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = ((StoreReservationDetalActivity)activity).getBookActionListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        registReceiver();
    }

    @Override
    public void onStop() {
        unRegistReceiver();
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void registReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_BOOK_STORE);
            filter.addAction(Constant.ACTION_BROADCAST_BOOK_STORE_ERR);

            mReceiver = new StoreBookReceiver();
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    public void unRegistReceiver() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.img_book_user_del:
                edName.setText("");
                break;

            case R.id.img_book_tel_del:
                edTel.setText("");
                break;

            case R.id.img_book_area_del:
                edLoc.setText("");
                break;

            case R.id.txt_store_book_time:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                TimePickerDialog timeDialog = new TimePickerDialog(getActivity(),
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                StoreBookingFragment.this.hour = hourOfDay;
                                                StoreBookingFragment.this.minute = minute;

                                                String date = StoreBookingFragment.this.year + "年" +
                                                        StoreBookingFragment.this.month + "月" +
                                                        StoreBookingFragment.this.day + "日" +
                                                        StoreBookingFragment.this.hour + "时" +
                                                        StoreBookingFragment.this.minute + "分";
                                                txtTime.setText(date);
                                            }
                                        },
                                        Calendar.getInstance().get(Calendar.HOUR),
                                        Calendar.getInstance().get(Calendar.MINUTE),
                                        true);
                                timeDialog.show();

                                StoreBookingFragment.this.year = year;
                                StoreBookingFragment.this.month = monthOfYear;
                                StoreBookingFragment.this.day = dayOfMonth;

                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();

            case R.id.btn_conform_booking:

                String name = edName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getActivity(), "请输入联系人信息！", Toast.LENGTH_SHORT).show();
                    edName.setFocusable(true);
                    break;
                }

                String tel = edTel.getText().toString();
                if (TextUtils.isEmpty(tel)) {
                    Toast.makeText(getActivity(), "请输入联系电话！", Toast.LENGTH_SHORT).show();
                    edTel.setFocusable(true);
                    break;
                }

                if (year < 0 || month < 0 || day < 0 || hour < 0 || minute < 0) {
                    Toast.makeText(getActivity(), "请选择预约时间！", Toast.LENGTH_SHORT).show();
                    break;
                }
                String time = year + "年" + month + "月" + day + "日 " + hour + "时" + minute + "分";

                String location = edLoc.getText().toString();
                if (TextUtils.isEmpty(location)) {
                    Toast.makeText(getActivity(), "请输入您的位置！", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (mListener != null) {
                    mListener.onBookCommit(name, tel, time, location);
                }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            int id = v.getId();
            if (id == R.id.ed_store_book_user) {
                edTel.setFocusable(true);
            }

            if (id == R.id.ed_store_book_tel) {
                edLoc.setFocusable(true);
            }
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        int id = v.getId();
        if (hasFocus) {
            switch (id) {
                case R.id.ed_store_book_user:
                    if (edName.getText().length() != 0) {
                        nameDel.setVisibility(View.VISIBLE);
                    } else {
                        nameDel.setVisibility(View.GONE);
                    }
                    break;
                case R.id.ed_store_book_tel:
                    if (edTel.getText().length() != 0) {
                        telDel.setVisibility(View.VISIBLE);
                    } else {
                        telDel.setVisibility(View.GONE);
                    }
                    break;
                case R.id.ed_local_verify:
                    if (edLoc.getText().length() != 0) {
                        locDel.setVisibility(View.VISIBLE);
                    } else {
                        locDel.setVisibility(View.GONE);
                    }
            }
        } else {
            switch (id) {
                case R.id.ed_store_book_user:
                    nameDel.setVisibility(View.GONE);
                    break;
                case R.id.ed_store_book_tel:
                    telDel.setVisibility(View.GONE);
                    break;
                case R.id.ed_store_book_area:
                    locDel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (mListener != null) {
            mListener.onBackClicked();
        }
        return true;
    }

    public interface OnBookActionListener {

        public void onBookCommit(String name, String tel, String time, String loc);
        public void onBackClicked();
        public void onCommitResult(boolean result);
    }

    private class StoreBookReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_BOOK_STORE.equals(action)) {
                if (mListener != null) {
                    mListener.onCommitResult(true);
                }
            }

            if (Constant.ACTION_BROADCAST_BOOK_STORE_ERR.equals(action)) {
                if (mListener != null) {

                    String errMsg = context.getString(R.string.str_err_net);
                    if (intent.hasExtra("description")) {
                        errMsg = intent.getStringExtra("description");
                    }
                    Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();

                    mListener.onCommitResult(false);
                }
            }
        }
    }

}
