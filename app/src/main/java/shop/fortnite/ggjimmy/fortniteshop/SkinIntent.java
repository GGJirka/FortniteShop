package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import uk.co.senab.photoview.PhotoViewAttacher;

public class SkinIntent extends AppCompatActivity {
    public static final String INTENT_ID = "SKIN_NAME";
    public static final String OUTFIT_TYPE = "OUTFIT_TYPE";
    public static final String RARITY = "RARITY";
    public static final String PRICE = "PRICE";
    public LinearLayout layout;
    public String skinName;
    public ImageView skin;
    public String outfitType;
    public String rarity;
    public String price;
    public String url;
    public AdView mAdView;
    private boolean isTouched = false;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_intent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.title);
        setSupportActionBar(toolbar);

        layout = (LinearLayout) findViewById(R.id.intent_layout);
        skinName = getIntent().getExtras().getString(INTENT_ID);
        outfitType = getIntent().getExtras().getString(OUTFIT_TYPE);
        rarity = getIntent().getExtras().getString(RARITY);
        price = getIntent().getExtras().getString(PRICE);
        url = getIntent().getExtras().getString("IMAGE_URL_INTENT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        title.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
        title.setText("SKIN DETAILS");

        if( price != null && price.equals("???")) {
            TextView noSkinDisplay = (TextView) findViewById(R.id.coming_soon);
            noSkinDisplay.setText("Coming soon");
            noSkinDisplay.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
            hideLoading();
        }else{
            if(rarity.equals("emote")) {
                initVideo();
            }else{
                new JsoupAsyncTask().execute();
            }
        }
        MobileAds.initialize(this, "ca-app-pub-5090360471586053~1383172270");

        mAdView = (AdView) findViewById(R.id.skin_intent_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, String>{



        @Override
        protected String doInBackground(Void... params) {
            Document d = null;
            try{
                d = Jsoup.connect("https://fnbr.co/png").get();
                Elements elements = d.getAllElements();

                for(Element image : elements){
                    Elements data = image.getAllElements();
                    String[] split = image.attr("class").split(" ");
                    String type = split[split.length-1];

                    for(Element information : data){
                        if(skinName.toUpperCase().trim().equals(information.attr("title").toUpperCase().trim())){
                            if(rarity.equals("emote") || rarity.equals(type)) {
                                return information.attr("data-src");
                            }
                        }
                    }
                }
            }catch(IOException e){

            }
            return "https://image.fnbr.co/outfit/5ab155f4e9847b3170da0321/gallery.jpg";
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            new DownloadItemImageTask(layout,outfitType).execute(result);
        }
    }
    private class DownloadItemImageTask extends AsyncTask<String, Void, Bitmap> {

        private LinearLayout img;
        private String outfitType;

        public DownloadItemImageTask(LinearLayout img, String outfitType){
            this.img = img;
            this.outfitType = outfitType;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String urls = params[0];
            Bitmap imageBitmap = null;

            try{
                InputStream inputStream = new URL(urls).openStream();
                imageBitmap = BitmapFactory.decodeStream(inputStream);
            }catch(Exception e){
                e.printStackTrace();
            }
            return imageBitmap;
        }

        protected void onPostExecute(Bitmap result){
            hideLoading();
            skin = new ImageView(SkinIntent.this);

            skin.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            if(rarity.toLowerCase().equals("outfit")) {
                skin.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            switch(outfitType) {
                case "legendary":
                    layout.setBackgroundResource(R.drawable.legendary_background);
                    break;
                case "epic":
                    layout.setBackgroundResource(R.drawable.epic_background);
                    break;
                case "rare":
                    layout.setBackgroundResource(R.drawable.rare_background);
                    break;
                case "uncommon":
                    layout.setBackgroundResource(R.drawable.common_background);
                    break;
            }

            skin.setImageBitmap(result);
            /*VideoView v = (VideoView) findViewById(R.id.video);
            v.setVideoPath("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
            v.start();*/
            layout.addView(skin);
            /*PhotoViewAttacher attacher  = new PhotoViewAttacher(skin);
            attacher.update();*/
        }
    }
    public void hideLoading(){
        ProgressBar loading = (ProgressBar) findViewById(R.id.skin_loading);
        loading.setVisibility(View.GONE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    public void initVideo(){
        final VideoView video = new VideoView(SkinIntent.this);

        video.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));


        video.setVideoURI(Uri.parse(getUrl()));
        video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!isTouched) {
                    isTouched = true;
                    if (video.isPlaying()) {
                        video.pause();
                    } else {
                        video.start();
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isTouched = false;
                        }
                    }, 100);
                }
                return true;
            }
        });
        layout.addView(video);

    }
    public String getUrl(){
        switch(skinName.toLowerCase()){
            case "flapper":
                return "android.resource://" + getPackageName() + "/" + R.raw.flapper;
            case "fresh":
                return "android.resource://" + getPackageName() + "/" + R.raw.shit_dance;
            case "electro shuffle":
                return "android.resource://" + getPackageName() + "/" + R.raw.electro_shuffle;
            case "disco fever":
                return "android.resource://" + getPackageName() + "/" + R.raw.disco;
            case "reanimated":
                return "android.resource://" + getPackageName() + "/" + R.raw.reanimated;
            case "rocket rodeo":
                return "android.resource://" + getPackageName() + "/" + R.raw.rocket_ride;
            case "best mates":
                return "android.resource://" + getPackageName() + "/" + R.raw.best_mates;
            case "floss":
                return "android.resource://" + getPackageName() + "/" + R.raw.floss;
            case "dab":
                return "android.resource://" + getPackageName() + "/" + R.raw.dab;
            case "pure salt":
                return "android.resource://" + getPackageName() + "/" + R.raw.salt;
            case "take the l":
                return "android.resource://" + getPackageName() + "/" + R.raw.take_the_l;
            case "the worm":
                return "android.resource://" + getPackageName() + "/" + R.raw.worm;
            case "jubilation":
                return "android.resource://" + getPackageName() + "/" + R.raw.help;
            case "finger guns":
                return "android.resource://" + getPackageName() + "/" + R.raw.shot;
            case "slow clap":
                return "android.resource://" + getPackageName() + "/" + R.raw.slow_clap;
            case "breakin'":
                return "android.resource://" + getPackageName() + "/" + R.raw.dance;
            case "rock out":
                return "android.resource://" + getPackageName() + "/" + R.raw.rock_out;
            case "the robot":
                return "android.resource://" + getPackageName() + "/" + R.raw.robot;
            case "hootenanny":
                return "android.resource://" + getPackageName() + "/" + R.raw.hotenanny;
            case "flippin' sexy":
                return "android.resource://" + getPackageName() + "/" + R.raw.backflip;
            case "ride the pony":
                return "android.resource://" + getPackageName() + "/" + R.raw.ride;
            case "make it rain":
                return "android.resource://" + getPackageName() + "/" + R.raw.money_throw;
            case "step it up":
                return "android.resource://" + getPackageName() + "/" + R.raw.step_it_up;
            case "click!":
                return "android.resource://" + getPackageName() + "/" + R.raw.click;
            case "kiss kiss":
                return "android.resource://" + getPackageName() + "/" + R.raw.kiss;
            case "confused":
                return "android.resource://" + getPackageName() + "/" + R.raw.confused;
            case "breaking point":
                return "android.resource://" + getPackageName() + "/" + R.raw.breaking_point;
            case "wiggle":
                return "android.resource://" + getPackageName() + "/" + R.raw.cool_wave;
            case "brush your shoulders":
                return "android.resource://" + getPackageName() + "/" + R.raw.brush_shoulder;
            case "face palm":
                return "android.resource://" + getPackageName() + "/" + R.raw.facepalm;
            case "true love":
                return "android.resource://" + getPackageName() + "/" + R.raw.true_love;
            case "salute":
                return "android.resource://" + getPackageName() + "/" + R.raw.salute;
            case "rock paper scissors":
                return "android.resource://" + getPackageName() + "/" + R.raw.rock_paper;
            case "gun show":
                return "android.resource://" + getPackageName() + "/" + R.raw.gun_show;
            default:
                return "android.resource://" + getPackageName() + "/" + R.raw.confused;
        }
    }
}