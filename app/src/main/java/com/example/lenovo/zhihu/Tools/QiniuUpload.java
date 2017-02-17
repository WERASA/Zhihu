package com.example.lenovo.zhihu.Tools;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by lenovo on 2017/2/14.
 */

public class QiniuUpload {


    public static  String getKey(String scope ) throws NoSuchAlgorithmException {
        JSONObject jsonObject=new JSONObject();
        int putFlags=1;
        try {
            jsonObject.put("scope",scope);
            jsonObject.put("deadine",1514736000);
            JSONObject returnBody=new JSONObject();
            returnBody.put("name","(fname)");
            returnBody.put("size","(fsize)");
            returnBody.put("w","(imageInfo.width)");
            returnBody.put("h","(imageInfo.width)");
            returnBody.put("hash","(etag)");
            jsonObject.put("returnbody",returnBody);
            byte[] putData=jsonObject.toString().getBytes();
            String encodedPutPolicy= Base64.encodeToString(putData, Base64.URL_SAFE);
            Log.d("js",jsonObject.toString());
            Log.d("d",encodedPutPolicy);
            String secretkey="MY_SECRET_KEY";
            String accesskey=" EkgHnK6hay4eCw73StmWB6Dz-r9-l8bHGG7df7b8";
            String type = "HMAC-SHA1";
            SecretKeySpec secretKeySpec=new SecretKeySpec(secretkey.getBytes(),type);
            Mac mac= Mac.getInstance(type);
            mac.init(secretKeySpec);
            byte[]dygest=mac.doFinal(encodedPutPolicy.getBytes());
            String  encodedSign =Base64.encodeToString(dygest,Base64.URL_SAFE);
            Log.d("encode",encodedSign);
            String uploadKey=accesskey+":"+encodedSign+":"+encodedPutPolicy;
            Log.d("key",uploadKey);
            return  uploadKey;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
