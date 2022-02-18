package com.sweettyy;


import com.sweettyy.myclasses.Hash;
import com.sweettyy.myclasses.Producer;
import com.sweettyy.myclasses.Consumer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class main {

    public static boolean find;
    public static void main(String[] args) throws JSONException, NoSuchAlgorithmException, InterruptedException {
        main.find = false;
        File f = new File("hash.json");
        String content = "";

        try (FileInputStream fin = new FileInputStream(f)) {
            int i = -1;
            while ((i = fin.read()) != -1) {
                content += (char) i;
            }
            System.out.println("\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        JSONArray mJsonArray = new JSONArray(content);
        JSONObject mJsonObject = new JSONObject();

        Hash hashs[] = new Hash[mJsonArray.length()];

        for (int i = 0; i < hashs.length; i++) {
            hashs[i] = new Hash();
        }

        for (int i = 0; i < mJsonArray.length(); i++) {
            mJsonObject = mJsonArray.getJSONObject(i);
            mJsonObject.getString("hash");
            hashs[i].setContent(mJsonObject.getString("hash"));
        }

        hashs[0].setContent("57acd9204d45c9c71dd7f1625a252b4f6ef962c882d8ad6bb6d872660d02200e");
        hashs[0].passwordSelection(5);

    }
}
