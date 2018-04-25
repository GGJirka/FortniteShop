package shop.fortnite.ggjimmy.fortniteshop;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SkinIntentFragment extends AppCompatActivity {
    public AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_intent_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_fragment);
        TextView text = (TextView) findViewById(R.id.title_fragment);
        setSupportActionBar(toolbar);
        text.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
        text.setText("SKIN DETAILS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int position = getIntent().getExtras().getInt("START_POSITION");

        ViewPager viewPager = (ViewPager) findViewById(R.id.skin_intent_pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(SkinIntentFragment.this);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(position);
        mAdView = (AdView) findViewById(R.id.skin_intent_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
