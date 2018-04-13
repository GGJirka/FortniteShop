package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
            new JsoupAsyncTask().execute();
        }
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
                    layout.setBackgroundColor(Color.parseColor("#ac592f"));
                    break;
                case "epic":
                    layout.setBackgroundColor(Color.parseColor("#833ca4"));
                    break;
                case "rare":
                    layout.setBackgroundColor(Color.parseColor("#2474b1"));
                    break;
                case "uncommon":
                    layout.setBackgroundColor(Color.parseColor("#1c8b2f"));
                    break;

            }
            skin.setImageBitmap(result);
            /*VideoView v = (VideoView) findViewById(R.id.video);
            v.setVideoPath("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
            v.start();*/
            layout.addView(skin);
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
}
