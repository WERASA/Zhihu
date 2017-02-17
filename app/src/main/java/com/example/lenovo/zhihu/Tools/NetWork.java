package com.example.lenovo.zhihu.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by lenovo on 2017/1/16.
 */


public class NetWork {
    String returnThing;

    public Image getImage(String url, String content) {
        HttpURLConnection connection = null;
        URL mUrl = null;

        try {
            mUrl = new URL(url);
            connection = (HttpURLConnection) mUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {

                InputStream getMessage = connection.getInputStream();



            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }


    public static String post(String url, String content) {
        HttpURLConnection connection = null;

        try {
            URL mUrl = new URL(url);
            connection = (HttpURLConnection) mUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            String output = content;
            out.write(output.getBytes());
            out.flush();
            out.close();
            int respond = connection.getResponseCode();
            if (respond == 200) {
                String mRespond = paraseMessage(connection.getInputStream());
                return mRespond;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String paraseMessage(InputStream Message) throws IOException {
        ByteArrayOutputStream mMessage = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = Message.read(buffer)) != -1) {
            mMessage.write(buffer, 0, len);
        }
        Message.close();
        String mRespond = mMessage.toString();
        mMessage.close();
        return mRespond;
    }



    public static boolean isSuccess(JSONObject jsonObject) {
        try {
            if (jsonObject.getInt("status") == 200) {
                return true;

            } else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
