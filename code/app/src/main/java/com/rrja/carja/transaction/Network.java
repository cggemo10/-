package com.rrja.carja.transaction;

import android.text.TextUtils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtilsHC4;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chongge on 15/7/4.
 */
public class Network {

    public static JSONObject doGet(String urlStr) throws IOException {

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            conn.setUseCaches(false);// 忽略缓存

            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");;

            conn.setRequestProperty("Charset", "UTF-8");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                StringBuffer str = new StringBuffer();
                int len = -1;
                byte[] buf = new byte[1024];
                while ((len = inputStream.read(buf)) != -1) {
                    str.append(new String(buf,0,len));
                }

                JSONObject json = new JSONObject(str.toString());
                return json;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createErrorNetJsonObject();
    }

    public static JSONObject doPost(String urlStr, List<TextKeyValuePair> params, List<FileKeyValuePair> fileParams) {

        CloseableHttpClient httpClient = HttpClients.custom().useSystemProperties()
                .build();

        HttpPostHC4 httpPost = new HttpPostHC4(urlStr);
        httpPost.setHeader("Accept", "application/json; charset=UTF-8");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        if (params != null && params.size() != 0) {
            for (int i = 0; i < params.size(); i++) {
                builder.addTextBody(params.get(i).getName(),params.get(i).getValue(), ContentType.create("text/plain", Consts.UTF_8));
            }
        }

        if (fileParams != null && fileParams.size() != 0) {
            for (FileKeyValuePair filePair : fileParams) {
                File file = new File(filePair.getFilePath());
                if (file.exists()) {
                    String contentType = null;
                    if (filePair.getFileName().endsWith(".png")) {
                        contentType = "image/png";
                    } else if(filePair.getFileName().endsWith(".jpg")){
                        contentType = "image/jpeg";
                    }
                    if (!TextUtils.isEmpty(contentType)) {
                        builder.addPart(filePair.getName(), new FileBody(file, ContentType.create(contentType, Consts.UTF_8), filePair.getFileName()));
                    }
                }
            }
        }

        httpPost.setEntity(builder.build());

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String resultStr = EntityUtilsHC4.toString(response.getEntity(), "UTF-8");
            JSONObject result = new JSONObject(resultStr);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return createErrorNetJsonObject();
        }

//        try {
//            URL url = new URL(urlStr);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setConnectTimeout(10000);
//            conn.setReadTimeout(10000);
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setRequestMethod("POST");
//
//            conn.setUseCaches(false);// 忽略缓存
//
//            conn.setRequestProperty("Accept", "*/*");
//            conn.setRequestProperty("Connection", "Keep-Alive");
//            conn.setRequestProperty("User-Agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//
//            conn.setRequestProperty("Content-length", "" + content.length());
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");;
//
//            conn.setRequestProperty("Charset", "UTF-8");
//
//            JSONObject json = new JSONObject();
//
//            return json;
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;


    }

    private static JSONObject createErrorNetJsonObject() {
        JSONObject errJson = new JSONObject();
        // TODO add net err msg
        return errJson;
    }
}
