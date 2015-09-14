package com.rrja.carja.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrja.carja.R;

import java.util.Calendar;

public class OrderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView nameDel;
    private ImageView telDel;
    private ImageView serverAddrDel;
    private ImageView mailreceiverDel;
    private ImageView mailAddrDel;

    private EditText edName;
    private EditText edTel;
    private EditText edServerAddr;
    private TextView txtServerTime;
    private EditText edReceiver;
    private EditText edReceiverMail;

    private SwitchCompat switchInvoice;
    private View invoiceView;
    private AppCompatButton btnCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        if (llloc != null) {
            llloc.setVisibility(View.GONE);
        }


        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initView();
    }

    private void initView() {
        nameDel = (ImageView) findViewById(R.id.img_name_del);
        nameDel.setOnClickListener(this);

        telDel = (ImageView) findViewById(R.id.img_tel_del);
        telDel.setOnClickListener(this);

        serverAddrDel = (ImageView) findViewById(R.id.img_server_address_del);
        serverAddrDel.setOnClickListener(this);

        mailreceiverDel = (ImageView) findViewById(R.id.img_mail_receiver_del);
        mailreceiverDel.setOnClickListener(this);

        mailAddrDel = (ImageView) findViewById(R.id.img_mail_address_del);
        mailAddrDel.setOnClickListener(this);

        edName = (EditText) findViewById(R.id.ed_order_name);
        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    nameDel.setVisibility(View.VISIBLE);
                } else {
                    nameDel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edTel.setFocusable(true);
                }
                return false;
            }
        });

        edTel = (EditText) findViewById(R.id.ed_order_tel);
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

        edServerAddr = (EditText) findViewById(R.id.ed_order_server_address);
        edServerAddr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    serverAddrDel.setVisibility(View.VISIBLE);
                } else {
                    serverAddrDel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtServerTime = (TextView) findViewById(R.id.txt_order_server_time);
        txtServerTime.setOnClickListener(this);

        edReceiver = (EditText) findViewById(R.id.ed_order_mail_receiver);
        edReceiver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    mailreceiverDel.setVisibility(View.VISIBLE);
                } else {
                    mailreceiverDel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edReceiverMail = (EditText) findViewById(R.id.ed_order_mail_address);
        edReceiverMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    mailAddrDel.setVisibility(View.VISIBLE);
                } else {
                    mailAddrDel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        invoiceView = findViewById(R.id.view_invoice);
        invoiceView.setVisibility(View.GONE);

        switchInvoice = (SwitchCompat) findViewById(R.id.switch_need_invoice);
        switchInvoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    invoiceView.setVisibility(View.VISIBLE);
                } else {
                    invoiceView.setVisibility(View.GONE);
                }
            }
        });
        switchInvoice.setChecked(false);

        btnCommit = (AppCompatButton) findViewById(R.id.btn_confirm_order);
        btnCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.img_name_del:
                edName.setText("");
                break;
            case R.id.img_tel_del:
                edTel.setText("");
                break;
            case R.id.img_server_address_del:
                edServerAddr.setText("");
                break;
            case R.id.img_mail_receiver_del:
                edReceiver.setText("");
                break;
            case R.id.img_mail_address_del:
                edReceiverMail.setText("");
                break;
            case R.id.txt_order_server_time:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }

                DatePickerDialog dialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date = year + "年" + monthOfYear + "月" + dayOfMonth;
                                txtServerTime.setText(date);
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.btn_commit_order:
                boolean checkInput = verifyParam();
        }
    }

    private boolean verifyParam() {



        return false;
    }
}
