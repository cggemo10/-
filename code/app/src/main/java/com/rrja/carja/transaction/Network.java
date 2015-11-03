package com.rrja.carja.transaction;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.EntityUtilsHC4;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chongge on 15/7/4.
 */
public class Network {

    public static JSONObject doGet(String urlStr)  {

        CloseableHttpClient httpClient = HttpClients.custom().useSystemProperties()
                .build();
        try {

            HttpGetHC4 request = new HttpGetHC4(urlStr);
            CloseableHttpResponse response = httpClient.execute(request);
            String resultStr = EntityUtilsHC4.toString(response.getEntity(), "UTF-8");
            JSONObject result = new JSONObject(resultStr);
            return result;

        } catch (Exception e) {

            return createErrorNetJsonObject();

        }


    }

    public static JSONObject doPost(String urlStr, List<TextKeyValuePair> params, List<FileKeyValuePair> fileParams) {

        CloseableHttpClient httpClient = HttpClients.custom().useSystemProperties()
                .build();

        HttpPostHC4 httpPost = new HttpPostHC4(urlStr);
        httpPost.setHeader("Accept", "application/json; charset=UTF-8");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        if (params != null && params.size() != 0) {
            for (int i = 0; i < params.size(); i++) {
                builder.addTextBody(params.get(i).getName(),params.get(i).getValue(), ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8));
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

        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);

        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String resultStr = EntityUtilsHC4.toString(response.getEntity(), "UTF-8");
            JSONObject result = new JSONObject(resultStr);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return createErrorNetJsonObject();
        }
    }

    public static boolean doDownload(String url, String path) {

        String fileName = url.substring(url.lastIndexOf("/") + 1);
        File file = new File(path, fileName);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        CloseableHttpClient httpClient = HttpClients.custom().useSystemProperties()
                .build();

        try {
            HttpGetHC4 get = new HttpGetHC4(url);
            CloseableHttpResponse response = httpClient.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                FileOutputStream fos = new FileOutputStream(file);
                response.getEntity().writeTo(fos);
                fos.flush();
                fos.close();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    private static JSONObject createErrorNetJsonObject() {

        try {
            JSONObject errJson = new JSONObject();
            errJson.put("code", -1);
            errJson.put("description", "网络异常，请稍后再试！");
            return errJson;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
