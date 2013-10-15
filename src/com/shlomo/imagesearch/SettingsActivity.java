package com.shlomo.imagesearch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    Spinner imageSizeSp;
    Spinner imageColorSp;
    Spinner imageTypeSp;
    EditText etSiteFilter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Show the Up button in the action bar.
        setupActionBar();
        
        initInputs();
    }

    private void initInputs() {
        imageSizeSp = (Spinner) findViewById(R.id.spImageSize);
        imageColorSp = (Spinner) findViewById(R.id.spColorFilter);
        imageTypeSp = (Spinner) findViewById(R.id.spImageType);
        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
        
        imageSizeSp.setSelection(getSpinnerItemPosition(imageSizeSp, getIntent().getStringExtra("imageSize")));
        imageColorSp.setSelection(getSpinnerItemPosition(imageColorSp, getIntent().getStringExtra("imageColor")));
        imageTypeSp.setSelection(getSpinnerItemPosition(imageTypeSp, getIntent().getStringExtra("imageType")));
        etSiteFilter.setText(getIntent().getStringExtra("siteFilter"));
    }
    
    private int getSpinnerItemPosition(Spinner spinner, String value) {
        for(int i=0; i<imageSizeSp.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        
        return 0;
    }
    
    public void onClickSaveSettings(View v) {
        Toast.makeText(getApplicationContext(), "Saving settings", Toast.LENGTH_SHORT)
            .show();
        
        goBackToMainScreen();
    }
    
    private void goBackToMainScreen() {
        Intent data = new Intent();
        data.putExtra("imageSize", imageSizeSp.getSelectedItem().toString());
        data.putExtra("imageColor", imageColorSp.getSelectedItem().toString());
        data.putExtra("imageType", imageTypeSp.getSelectedItem().toString());
        data.putExtra("siteFilter", etSiteFilter.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
    
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
