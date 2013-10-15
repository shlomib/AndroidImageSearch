package com.shlomo.imagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ImageSearchActivity extends Activity {

    private static final String TAG = "ImageSearchActivity";
    
    static final int REQUEST_CODE = 1234;
    static final String IMAGE_COLOR = "&imgcolor=";
    static final String SITE_SEARCH = "&as_sitesearch=";
    static final String IMAGE_SIZE = "&imgsz=";
    static final String IMAGE_TYPE = "&imgtype=";
    
    Button searchButton;
    EditText searchInput;
    GridView gvImages;
    ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
    ImageResultArrayAdapter imageAdapter;
    String imageSize = "";
    String imageColor = "";
    String imageType = "";
    String siteFilter = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        searchButton = (Button) findViewById(R.id.btnSubmitSearch);
        searchInput = (EditText) findViewById(R.id.etSearchInput);
        gvImages = (GridView) findViewById(R.id.gvImages);
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        gvImages.setAdapter(imageAdapter);
        gvImages.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View parent, int position,
                    long rowId) {
                Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                ImageResult imageResult = imageResults.get(position);
                i.putExtra("result", imageResult);
                startActivity(i);
            }
            
        });
        
        searchButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            
            runImageSearch();
          }
        });
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
         imageSize = data.getExtras().getString("imageSize");
         imageColor = data.getExtras().getString("imageColor");
         imageType = data.getExtras().getString("imageType");
         siteFilter = data.getExtras().getString("siteFilter");
         
         Log.v(TAG, "imageSize from settings: " + imageSize);
         Log.v(TAG, "imageColor from settings: " + imageColor);
         Log.v(TAG, "imageType from settings: " + imageType);
         Log.v(TAG, "siteFilter from settings: " + siteFilter);
         
         runImageSearch();
      }
    } 
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_search, menu);
        return true;
    }
    
    public void onClickSettings(MenuItem mi) {
        Intent i = new Intent(this, SettingsActivity.class);
        i.putExtra("imageSize", imageSize);
        i.putExtra("imageColor", imageColor);
        i.putExtra("imageType", imageType);
        i.putExtra("siteFilter", siteFilter);
        startActivityForResult(i, REQUEST_CODE); 
    }
    
    public String getSearchUrl(String inputValue) {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" +
                "start=" + 0 + "&v=1.0&q=" + Uri.encode(inputValue);
        
        if (!imageSize.equals("")) {
            url += IMAGE_SIZE + imageSize;
        }
        if (!imageColor.equals("")) {
            url += IMAGE_COLOR + imageColor;
        }
        if (!imageType.equals("")) {
            url += IMAGE_TYPE + imageType;
        }
        if (!siteFilter.equals("")) {
            url += SITE_SEARCH + siteFilter;
        }
        
        Log.v(TAG, "url: " + url);
        
        return url;
    }
    
    public void runImageSearch() {
        searchInput = (EditText) findViewById(R.id.etSearchInput);
        String searchTextValue = searchInput.getText().toString();
        
        Log.v(TAG, "search value: " + searchTextValue);
        
        Toast.makeText(getApplicationContext(), "Searching for: " + searchTextValue, Toast.LENGTH_LONG)
            .show();
        
        
        
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getSearchUrl(searchTextValue),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        JSONArray imageJsonResults = null;
                        try {
                            imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
                            imageResults.clear();
                            imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
                            Log.v(TAG, imageResults.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    
                    @Override
                    public void onFailure(Throwable e, JSONObject errorResponse) {
                        e.printStackTrace();
                    }
                }
        );
    }

}