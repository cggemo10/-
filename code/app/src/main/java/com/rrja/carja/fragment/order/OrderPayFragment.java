package com.rrja.carja.fragment.order;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.rrja.carja.R;
import com.rrja.carja.activity.OrderActivity;
import com.rrja.carja.model.PayInfo;
import com.rrja.carja.model.maintenance.MaintenanceOrder;
import com.rrja.carja.utils.PayResult;
import com.rrja.carja.utils.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class OrderPayFragment extends Fragment {

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
    private static final int SDK_PAY_FLAG = 1;

    private OnOrderPayInteractionListener mListener;


    public static OrderPayFragment newInstance() {
        OrderPayFragment fragment = new OrderPayFragment();
        return fragment;
    }

    public OrderPayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_pay, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = ((OrderActivity) activity).getOrderPayListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                PayTask alipay = new PayTask(getActivity());
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
        String tel = null;
        MaintenanceOrder order = ((OrderActivity) getActivity()).getMaintenanceOrder();
        if (order != null) {
            tel = order.getUserInfo().getTel();
        } else if (payInfo != null) {
            tel = payInfo.getTel();
        }
        orderInfo += "&notify_url=" + "\""
                + "http://120.25.201.50/api/order/syncAlipay?nattel="
                + tel + "%26orderNum="
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
                        Toast.makeText(getActivity(), "支付成功",
                                Toast.LENGTH_SHORT).show();

                        // TODO order sync page
//                        payResult.
                        if (mListener != null) {
                            mListener.syncPayment("22", data.getString("orderNum"));
                        }
                        PayInfo payInfo = ((OrderActivity) getActivity()).getpayInfo();
                        if (payInfo != null) {
                            getActivity().setResult(Activity.RESULT_OK);
                        }
                        getActivity().finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getActivity(), "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                            if (mListener != null) {
                                mListener.syncPayment("22", data.getString("orderNum"));
                            }
                            getActivity().setResult(Activity.RESULT_OK);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getActivity(), "支付失败",
                                    Toast.LENGTH_SHORT).show();
                            if (mListener != null) {
                                mListener.syncPayment("11", data.getString("orderNum"));
                            }
                            getActivity().setResult(Activity.RESULT_CANCELED);
                            btnCommit.setEnabled(true);
                        }
                    }
                    break;

            }
        }
    };

    public interface OnOrderPayInteractionListener {

        public void syncPayment(String payStatus, String orderNum);
    }
}
