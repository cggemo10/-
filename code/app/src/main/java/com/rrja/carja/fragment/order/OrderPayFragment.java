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
        return inflater.inflate(R.layout.fragment_order_pay, container, false);
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
        // ����
        String orderInfo = getOrderInfo(info);

        // �Զ�����RSA ǩ��
        String sign = sign(orderInfo);
        try {
            // �����sign ��URL����
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // �����ķ���֧���������淶�Ķ�����Ϣ
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // ����PayTask ����
                PayTask alipay = new PayTask(getActivity());
                // ����֧���ӿڣ���ȡ֧�����
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

        // �����첽����
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    /**
     * create the order info. ����������Ϣ
     */
    public String getOrderInfo(PayInfo payInfo
//                               String tradeNo,String subject, String body, String price
    ) {
        // ǩԼ���������ID
        String orderInfo = "partner=" + "\"" + "2088911832390945" + "\"";

        // ǩԼ����֧�����˺�
        orderInfo += "&seller_id=" + "\"" + "2088911832390945" + "\"";

        // �̻���վΨһ������
        orderInfo += "&out_trade_no=" + "\"" + payInfo.getTradeNo() + "\"";

        // ��Ʒ����
        orderInfo += "&subject=" + "\"" + payInfo.getSubject() + "\"";

        // ��Ʒ����
        orderInfo += "&body=" + "\"" + payInfo.getBody() + "\"";

        // ��Ʒ���
        orderInfo += "&total_fee=" + "\"" + payInfo.getFee() + "\"";

        // �������첽֪ͨҳ��·��
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

        // ����ӿ����ƣ� �̶�ֵ
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // ֧�����ͣ� �̶�ֵ
        orderInfo += "&payment_type=\"1\"";

        // �������룬 �̶�ֵ
        orderInfo += "&_input_charset=\"utf-8\"";

        // ����δ����׵ĳ�ʱʱ��
        // Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
        // ȡֵ��Χ��1m��15d��
        // m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
        // �ò�����ֵ������С���㣬��1.5h����ת��Ϊ90m��
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_tokenΪ���������Ȩ��ȡ����alipay_open_id,���ϴ˲����û���ʹ����Ȩ���˻�����֧��
//        orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
//        orderInfo += "&return_url=\"m.alipay.com\"";

        // �������п�֧���������ô˲���������ǩ���� �̶�ֵ ����ҪǩԼ���������п����֧��������ʹ�ã�
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. �Զ�����Ϣ����ǩ��
     *
     * @param content ��ǩ��������Ϣ
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. ��ȡǩ����ʽ
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

                    // ֧�������ش˴�֧���������ǩ�������֧����ǩ����Ϣ��ǩԼʱ֧�����ṩ�Ĺ�Կ����ǩ
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(getActivity(), "֧���ɹ�",
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
                        // �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
                        // ��8000������֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getActivity(), "֧�����ȷ����",
                                    Toast.LENGTH_SHORT).show();
                            if (mListener != null) {
                                mListener.syncPayment("22", data.getString("orderNum"));
                            }
                            getActivity().setResult(Activity.RESULT_OK);
                        } else {
                            // ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
                            Toast.makeText(getActivity(), "֧��ʧ��",
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
