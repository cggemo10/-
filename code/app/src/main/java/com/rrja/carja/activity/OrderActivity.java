package com.rrja.carja.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.rrja.carja.R;
import com.rrja.carja.constant.Constant;
import com.rrja.carja.model.PayInfo;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.model.myorder.OrderRecord;
import com.rrja.carja.service.DataCenterService;
import com.rrja.carja.service.impl.OrderBinder;
import com.rrja.carja.utils.PayResult;
import com.rrja.carja.utils.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

public class OrderActivity extends BaseActivity implements View.OnClickListener,
        View.OnFocusChangeListener, TextView.OnEditorActionListener {

    private int year = -1;
    private int month = -1;
    private int day = -1;
    private int hour = -1;
    private int minute = -1;

    private ImageView nameDel;
    private ImageView telDel;
    private ImageView serverAddrDel;
    private ImageView invoiceTitleDel;
    private ImageView mailreceiverDel;
    private ImageView mailAddrDel;

    private EditText edName;
    private EditText edTel;
    private TextView txtPlateNum;
    private EditText edServerAddr;
    private TextView txtServerTime;
    private EditText edInvoiceTitle;
    private EditText edReceiver;
    private EditText edReceiverMail;

    private SwitchCompat switchInvoice;
    private View invoiceView;
    private AppCompatButton btnCommit;

    private OrderBinder orderService;

    private MaintenanceOrder order;
    private OrderReceiver mReceiver;
    private String orderSubject;


    private static final int SDK_ORDRE_FLAG = 10;

    private PayInfo payInfo;



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

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("order") && extras.containsKey("subject")) {
            try {
                order = extras.getParcelable("order");
                orderSubject = extras.getString("subject");
            } catch (Exception e) {
                // TODO
                e.printStackTrace();
                finish();
            }

        } else if (extras.containsKey("payInfo") && extras.get("payInfo") != null) {
            // from orderlist
            setResult(RESULT_CANCELED);
            payInfo = getIntent().getParcelableExtra("payInfo");
            if (payInfo == null) {
                finish();
            }
        } else {
            finish();
        }

        initView();

        Intent intent = new Intent(this, DataCenterService.class);
        intent.setAction(Constant.ACTION_ORDER_SERVICE);
        bindService(intent, conn, BIND_AUTO_CREATE);

        registReceiver();
    }

    @Override
    protected void onDestroy() {
        if (orderService != null) {
            unbindService(conn);
        }
        unRegistReceiver();
        super.onDestroy();
    }

    private void initView() {
        nameDel = (ImageView) findViewById(R.id.img_name_del);
        nameDel.setOnClickListener(this);

        telDel = (ImageView) findViewById(R.id.img_tel_del);
        telDel.setOnClickListener(this);

        serverAddrDel = (ImageView) findViewById(R.id.img_server_address_del);
        serverAddrDel.setOnClickListener(this);

        invoiceTitleDel = (ImageView) findViewById(R.id.img_invoice_title_del);
        invoiceTitleDel.setOnClickListener(this);

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
        edName.setOnFocusChangeListener(this);
        edName.setOnEditorActionListener(this);

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
        edTel.setOnFocusChangeListener(this);
        edTel.setOnEditorActionListener(this);

        txtPlateNum = (TextView) findViewById(R.id.txt_order_car_num);
        if (order != null) {
            txtPlateNum.setText(order.getmCarInfo().getPlatNum());
        }

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
        edServerAddr.setOnFocusChangeListener(this);

        txtServerTime = (TextView) findViewById(R.id.txt_order_server_time);
        txtServerTime.setOnClickListener(this);

        edInvoiceTitle = (EditText) findViewById(R.id.ed_order_invoice_title);
        edInvoiceTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    invoiceTitleDel.setVisibility(View.VISIBLE);
                } else {
                    invoiceTitleDel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edInvoiceTitle.setOnFocusChangeListener(this);
        edInvoiceTitle.setOnEditorActionListener(this);

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
        edReceiver.setOnFocusChangeListener(this);
        edReceiver.setOnEditorActionListener(this);

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
        edReceiverMail.setOnFocusChangeListener(this);

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

        if (payInfo != null) {

            edName.setText(payInfo.getUserName());
            edName.setEnabled(false);

            edTel.setText(payInfo.getTel());
            edTel.setEnabled(false);

            txtPlateNum.setText(payInfo.getCarPlat());
            txtPlateNum.setEnabled(false);

            edServerAddr.setText(payInfo.getServiceLoc());
            edServerAddr.setEnabled(false);

            txtServerTime.setText(payInfo.getServiceTime());
            txtServerTime.setEnabled(false);
            txtServerTime.setClickable(false);

            String invoiceTitle = payInfo.getInvoiceTitle();
            String invoiceMail = payInfo.getInvoiceMail();
            String invoiceReceiver = payInfo.getInvoiceReceiver();
            if (!TextUtils.isEmpty(invoiceTitle) && !TextUtils.isEmpty(invoiceMail) && !TextUtils.isEmpty(invoiceReceiver)) {
                switchInvoice.setChecked(true);
                edInvoiceTitle.setText(payInfo.getInvoiceTitle());
                edInvoiceTitle.setEnabled(false);

                edReceiver.setText(payInfo.getInvoiceReceiver());
                edReceiver.setEnabled(false);

                edReceiverMail.setText(payInfo.getInvoiceMail());
                edReceiverMail.setEnabled(false);
            } else {
                switchInvoice.setChecked(false);
            }
            switchInvoice.setEnabled(false);

            nameDel.setVisibility(View.GONE);
            telDel.setVisibility(View.GONE);
            serverAddrDel.setVisibility(View.GONE);
            invoiceTitleDel.setVisibility(View.GONE);
            mailreceiverDel.setVisibility(View.GONE);
            mailAddrDel.setVisibility(View.GONE);
        }
    }

    boolean firstStart = false;
    @Override
    protected void onResume() {
        super.onResume();
        if (payInfo != null && !firstStart) {
            pay(payInfo);
            firstStart = true;
        }
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
            case R.id.ed_order_invoice_title:
                edInvoiceTitle.setText("");
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

                                TimePickerDialog timeDialog = new TimePickerDialog(OrderActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                OrderActivity.this.hour = hourOfDay;
                                                OrderActivity.this.minute = minute;

                                                String date = OrderActivity.this.year + "年" +
                                                        OrderActivity.this.month + "月" +
                                                        OrderActivity.this.day + "日" +
                                                        OrderActivity.this.hour + "时" +
                                                        OrderActivity.this.minute + "分";
                                                txtServerTime.setText(date);
                                            }
                                        },
                                        Calendar.getInstance().get(Calendar.HOUR),
                                        Calendar.getInstance().get(Calendar.MINUTE),
                                        true);
                                timeDialog.show();

                                OrderActivity.this.year = year;
                                OrderActivity.this.month = monthOfYear;
                                OrderActivity.this.day = dayOfMonth;

                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();


                break;

            case R.id.btn_confirm_order:
                btnCommit.setEnabled(false);
                if (payInfo == null) {
                    boolean success = commitOrder();
                    if (!success) {
                        btnCommit.setEnabled(true);
                    }
                } else {
                    pay(payInfo);
                }
        }
    }

    private boolean commitOrder() {

        String name = edName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请填写您的姓名", Toast.LENGTH_LONG).show();
            edName.requestFocus();
            return false;
        }

        String tel = edTel.getText().toString();
        if (TextUtils.isEmpty(tel)) {
            Toast.makeText(this, "请填写您的联系电话", Toast.LENGTH_LONG).show();
            edTel.requestFocus();
            return false;
        }

        String serverAddr = edServerAddr.getText().toString();
        if (TextUtils.isEmpty(serverAddr)) {
            Toast.makeText(this, "请填写服务地点", Toast.LENGTH_LONG).show();
            edServerAddr.requestFocus();
            return false;
        }

        String serverTime = "";
        if (year == -1 || month == -1 || day == -1 || hour == -1 || minute == -1) {
            Toast.makeText(this, "请选择服务时间", Toast.LENGTH_LONG).show();
            return false;
        }
        serverTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + "00";

        String invoiceTitle = "";
        String receiver = "";
        String receiverMail = "";
        if (switchInvoice.isChecked()) {

            invoiceTitle = edInvoiceTitle.getText().toString();
            if (TextUtils.isEmpty(invoiceTitle)) {
                Toast.makeText(this, "请填写发票抬头", Toast.LENGTH_LONG).show();
                edInvoiceTitle.requestFocus();
                return false;
            }

            receiver = edReceiver.getText().toString();
            if (TextUtils.isEmpty(receiver)) {
                Toast.makeText(this, "请填写收件人", Toast.LENGTH_LONG).show();
                edReceiver.requestFocus();
                return false;
            }

            receiverMail = edReceiverMail.getText().toString();
            if (TextUtils.isEmpty(receiverMail)) {
                Toast.makeText(this, "请填写收件地址", Toast.LENGTH_LONG).show();
                edReceiverMail.requestFocus();
                return false;
            }
        }

        Bundle data = new Bundle();
        data.putString("contacts", name);
        data.putString("phone", tel);
        data.putString("plate_num", order.getmCarInfo().getPlatNum());
        data.putString("server_location", serverAddr);
        data.putString("server_date", serverTime);
        data.putBoolean("need_invoice", switchInvoice.isChecked());
        data.putString("invoice_title", invoiceTitle);
        data.putString("receiver", receiver);
        data.putString("receiver_addr", receiverMail);
        data.putString("order_details", order.getCommitContent());

        orderService.commitOrder(order, data);

        return true;
    }

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            orderService = (OrderBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            orderService = null;
        }
    };

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            int id = v.getId();
            switch (id) {
                case R.id.ed_order_name:
                    edTel.setFocusable(true);
                    break;
                case R.id.ed_order_tel:
                    edServerAddr.setFocusable(true);
                    break;
                case R.id.ed_order_invoice_title:
                    edReceiver.setFocusable(true);
                    break;
                case R.id.ed_order_mail_receiver:
                    edReceiverMail.setFocusable(true);
                    break;
            }
        }

        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        if (hasFocus) {
            switch (id) {
                case R.id.ed_order_name:
                    if (edName.getText().length() != 0) {
                        nameDel.setVisibility(View.VISIBLE);
                    } else {
                        nameDel.setVisibility(View.GONE);
                    }
                    break;
                case R.id.ed_order_tel:
                    if (edTel.getText().length() != 0) {
                        telDel.setVisibility(View.VISIBLE);
                    } else {
                        telDel.setVisibility(View.GONE);
                    }
                    break;
                case R.id.ed_order_server_address:
                    if (edServerAddr.getText().length() != 0) {
                        serverAddrDel.setVisibility(View.VISIBLE);
                    } else {
                        serverAddrDel.setVisibility(View.GONE);
                    }
                    break;
                case R.id.ed_order_invoice_title:
                    if (edInvoiceTitle.getText().length() != 0) {
                        invoiceTitleDel.setVisibility(View.VISIBLE);
                    } else {
                        invoiceTitleDel.setVisibility(View.GONE);
                    }
                    break;
                case R.id.ed_order_mail_receiver:
                    if (edReceiver.getText().length() != 0) {
                        mailreceiverDel.setVisibility(View.VISIBLE);
                    } else {
                        mailreceiverDel.setVisibility(View.GONE);
                    }
                    break;
                case R.id.ed_order_mail_address:
                    if (edReceiverMail.getText().length() != 0) {
                        mailreceiverDel.setVisibility(View.VISIBLE);
                    } else {
                        mailreceiverDel.setVisibility(View.GONE);
                    }
                    break;
            }
        } else {
            switch (id) {
                case R.id.ed_order_name:
                    nameDel.setVisibility(View.GONE);
                    break;
                case R.id.ed_order_tel:
                    telDel.setVisibility(View.GONE);
                    break;
                case R.id.ed_order_server_address:
                    serverAddrDel.setVisibility(View.GONE);
                    break;
                case R.id.ed_order_invoice_title:
                    invoiceTitleDel.setVisibility(View.GONE);
                    break;
                case R.id.ed_order_mail_receiver:
                    mailreceiverDel.setVisibility(View.GONE);
                    break;
                case R.id.ed_order_mail_address:
                    mailAddrDel.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private class OrderReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (Constant.ACTION_BROADCAST_ORDER_ERR.equals(action)) {

                if (intent.hasExtra("description")) {
                    String description = intent.getStringExtra("description");
                    Toast.makeText(context, description, Toast.LENGTH_SHORT).show();

                    btnCommit.setEnabled(true);
                }
            }

            if (Constant.ACTION_BROADCAST_ORDER_SUCC.equals(action)) {

                // TODO pay order
                if (intent.hasExtra("orderNum")) {

                    Message msg = mHandler.obtainMessage();
                    msg.what = SDK_ORDRE_FLAG;
                    msg.obj = intent.getStringExtra("orderNum");
                    mHandler.sendMessage(msg);
                } else {
                    // TODO
                }
            }
        }
    }

    private void registReceiver() {
        if (mReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_BROADCAST_ORDER_SUCC);
            filter.addAction(Constant.ACTION_BROADCAST_ORDER_ERR);

            mReceiver = new OrderReceiver();
            registerReceiver(mReceiver, filter);
        }
    }

    private void unRegistReceiver() {

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);

            mReceiver = null;
        }
    }


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case SDK_ORDRE_FLAG:
                    String orderNum = (String) msg.obj;
                    PayInfo info = new PayInfo();
                    info.setTradeNo(orderNum);
                    info.setSubject(orderSubject);
                    info.setFee(order.calculateTotalFee());
                    info.setBody(orderSubject);

                    pay(info);
                    break;
            }
        }
    };

    public MaintenanceOrder getMaintenanceOrder() {
        return order;
    }

    public PayInfo getpayInfo() {
        return payInfo;
    }


}
