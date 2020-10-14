package io.drew.record.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.drew.record.model.AddressData;

/**
 * @Author: KKK
 * @CreateDate: 2020/9/10 10:12 AM
 */
public class FileUtils {

    public static List<AddressData> getAddressDatas(Context context) {
        List<AddressData> list = new Gson().fromJson(parseJson(context), new TypeToken<ArrayList<AddressData>>() {
        }.getType());
        return list;
    }

    public static String parseJson(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open("district.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
