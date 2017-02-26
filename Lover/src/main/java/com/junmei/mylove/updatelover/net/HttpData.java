package com.junmei.mylove.updatelover.net;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpData extends AsyncTask<String, Void, String> {
    private String urlPath;
    private URL url;
    private String result;
    private HttpGetListener listener;
    private HttpURLConnection coon;
    private InputStream in;

    /**
     * @param urlPath：请求的网址
     * @param listener
     */
    public HttpData(String urlPath, HttpGetListener listener) {
        this.urlPath = urlPath;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            url = new URL(urlPath);
            coon = (HttpURLConnection) url.openConnection();
            coon.setConnectTimeout(3000);
            coon.setReadTimeout(3000);
            coon.connect();
            int resultCode = coon.getResponseCode();
            if (resultCode == 200) {
                in = coon.getInputStream();
                result = readFromInputStream(in);
            } else if (resultCode == 404) {
            } else if (resultCode == 500) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (coon != null) {
                coon.disconnect();
            }
        }
        Log.e("TAG", "url=" + url);
        Log.e("TAG", "result=" + result);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            s = parseJson(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (s == null) {
            s = "主人，人家出错了啦";
        }
        listener.getDataUri(s);
    }

    private String parseJson(String json) throws JSONException {
        String text = null;
        String result=null;
        if(json!=null){
            JSONObject jsonObj = new JSONObject(json);
            result = jsonObj.optString("result", null);
        }
        if (result != null) {
            JSONObject obj = new JSONObject(result);
            text = obj.optString("text", null);
        }
        return text;
    }

    private String readFromInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        return bos.toString();
    }


    public interface HttpGetListener {
        void getDataUri(String data);
    }

}

