package com.shlomo.imagesearch;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ImageResult implements Serializable {
    private static final long serialVersionUID = 6053720771235331187L;
    private String url;
    private String tbUrl;
    
    public ImageResult(JSONObject json) {
        try {
            this.url = json.getString("url");
            this.tbUrl = json.getString("tbUrl");
        }
        catch (JSONException e) {
            this.url = null;
            this.tbUrl = null;
        }
    }
    
    public String toString() {
        return this.tbUrl;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getTbUrl() {
        return tbUrl;
    }
    
    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    public static ArrayList<ImageResult> fromJSONArray(
            JSONArray array) {
        
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for(int i=0; i<array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
