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

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_ORDRE_FLAG = 10;

    private PayInfo payInfo;

    private static final String RSA_PRIVATE =
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANPLWVqPLOB3vNUn" +
                    "mvK2VGX1zC3YaVYCsh/LAgp+35IOeiK5+Tg0ZQqig0nBk6aMrmWFpEhDN8Ci4Ha5" +
                    "LExkqcE7/J/vmnr4SgiA8NoaOpd8NDPJwaUxXWEqWN4mYxksQ2hykXwOPYiTi94W" +
                    "3vkVye75LXf9MQ8QYH3fUAXBuxRFAgMBAAECgYEAntVozHaFhE2n6v9Jv+4nO1Pr" +
                    "7RHYZW2eIiPAAxkFF+cNh4+LLLB2elRIO38V+RiWDYL9IkhuvmQoDEFwzFXNy8Qs" +
                    "djpmi8XmmzS+T72tzwoCW6ye2c8xORmlV0T8RqIsH5dVlOkYe+uevIjafXMUXJMD" +
                    "7CWgPrGfZPOVP3hLgF0CQQD1qzxZxkgtBXGb6+9J6QyIDEJNcWFGETZbDsREb3YJ" +
                    "XLscd9nbbjSLSc6hFw/Slij7TFGrLyHZ4nxMH2w/Ryh/AkEA3LNvP4cmsSZXnOXE" +
                    "WRBHpK4sAUGkc2pAOSHVXceC9Heb6o0OV5i10vsEz36dC+spUZh2TTFUMZx6nnHX" +
                    "yrPBOwJAJjKfyZ2o70cfaQ0AQc+4oRa24G+2njxi5Tmgjrt98Oq4NS71csbp6JZr" +
                    "SGTbkvTCHoC5WhKUAtrSVMu1+wjHZQJBALuy20VjMxPscQzeGJxKjTPQcVpsMml6" +
                    "Dr7IOId2W7iAVRko9+6l0NVraQ7zNqthR5W+nbZO4rUCyQIh/g3FzOsCQQDKfs+3" +
                    "ZWd2AD9AjGRcaoflBXx8hcW64xerxzDkjJgRRdV9WKNezcoPMOYiiZUH9FEPYm7a" +
                    "4AOuOPCO7fesa76F";

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
        txtPlateNum.setText(order.getmCarInfo().getPlatNum());

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

    @Override
    protected void onResume() {
        super.onResume();
        if (payInfo != null) {
            pay(payInfo);
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

    // ----------------------------------------------------------------------------------------

    private void pay(final PayInfo info) {

        // 订单
        String orderInfo = getOrderInfo(info);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(OrderActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                Bundle bundle = new Bundle();
                bundle.putString("payResult", result);
                bundle.putString("orderNum", info.getTradeNo());
                msg.obj = bundle;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(PayInfo payInfo
//                               String tradeNo,String subject, String body, String price
    ) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + "2088911832390945" + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + "2088911832390945" + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + payInfo.getTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + payInfo.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + payInfo.getBody() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + payInfo.getFee() + "\"";

        // 服务器异步通知页面路径
//        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
//                + "\"";
        orderInfo += "&notify_url=" + "\""
                + "http://120.25.201.50/api/order/syncAlipay?nattel="
                + order.getUserInfo().getTel() + "%26orderNum="
                + payInfo.getTradeNo() + "%26status=22"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//        orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SDK_PAY_FLAG:
                    Bundle data = (Bundle) msg.obj;

                    PayResult payResult = new PayResult(data.getString("payResult"));

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OrderActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();

                        // TODO order sync page
//                        payResult.
                        orderService.syncOrder(data.getString("orderNum"), "22");
                        OrderActivity.this.finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                            orderService.syncOrder(data.getString("orderNum"), "22");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();
                            orderService.syncOrder(data.getString("orderNum"), "11");
                            btnCommit.setEnabled(true);
                        }
                    }
                    break;
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

}
