package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    public DrawView skin;
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
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Document d = null;
            try{
                d = Jsoup.connect("https://fnbr.co/gallery").get();
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
            skin = new DrawView(SkinIntent.this,outfitType, price);

            skin.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            skin.setScaleType(ImageView.ScaleType.CENTER_CROP);

            DisplayMetrics displayMetrics = new DisplayMetrics();

            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) skin.getLayoutParams();
            params.width = (int) (width*1.02);
            skin.setLayoutParams(params);
            skin.setImageBitmap(result);
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
