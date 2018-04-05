package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

public class FortniteShop extends AppCompatActivity {
    public static LruCache<String, Bitmap> mMemoryCache;

    public static final String INTENT_ID = "SKIN_NAME";
    public static final String OUTFIT_TYPE = "OUTFIT_TYPE";
    public static final String RARITY = "RARITY";
    public ArrayList<String> urls;
    public ArrayList<String> itemNames;
    public ArrayList<String> itemPrice;
    public ArrayList<String> rarity;
    public ArrayList<String> outfitType;
    public Document document;
    public String URL = "https://fnbr.co/shop";
    public ListView listOfItems;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortnite_shop);
        getSupportActionBar().hide();
        urls = new ArrayList<>();
        itemNames = new ArrayList<>();
        itemPrice = new ArrayList<>();
        rarity = new ArrayList<>();
        outfitType = new ArrayList<>();
        listOfItems = (ListView) findViewById(R.id.items);

        final int maxMemorySize = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemorySize /8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){

            @Override
            public int sizeOf(String key, Bitmap value){
                return value.getByteCount() / 1024;
            }
        };

        JsoupAsyncTask asyncTask = new JsoupAsyncTask();
        asyncTask.execute();

        TextView mainTitle = (TextView) findViewById(R.id.main_title);

        mainTitle.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
        MobileAds.initialize(this, "ca-app-pub-5090360471586053~9240769580");

        mAdView = (AdView) findViewById(R.id.example);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try{
                document = Jsoup.connect(URL).get();
                Elements elements = document.getAllElements();

                for(Element el : elements){
                    if(el.attr("class").equals("col-md-3")){
                        Elements item = el.getAllElements();
                        for(Element index : item){
                            String[] s = index.attr("src").split("/");

                            if(s[s.length-1].equals("icon.png")){
                                urls.add(index.attr("src"));

                                String[] typeSplit = index.attr("class").split(" ");
                                outfitType.add(typeSplit[typeSplit.length-1]);
                            }else if(index.attr("class").contains("card splash-card")){
                                StringBuilder itemNameBuilder = new StringBuilder();
                                String[] stringArray = index.text().split(" ");

                                for(String str : stringArray) {
                                    if (str != stringArray[stringArray.length - 1]) {
                                        itemNameBuilder.append(str);
                                        itemNameBuilder.append(" ");
                                    }
                                }
                                itemPrice.add(stringArray[stringArray.length-1]);
                                itemNames.add(itemNameBuilder.toString().toUpperCase());
                            }
                            if(index.attr("class").equals("card splash-card rarity-legendary")){
                                rarity.add("legendary");
                            }else if(index.attr("class").equals("card splash-card rarity-rare")){
                                rarity.add("rare");
                            }else if(index.attr("class").equals("card splash-card rarity-epic")){
                                rarity.add("epic");
                            }else if(index.attr("class").equals("card splash-card rarity-uncommon")){
                                 rarity.add("uncommon");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              rarity.add("uncommon");
                            }
                        }
                    }
                }

            } catch(IOException e){
                e.printStackTrace();
            }
            return urls;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result){
            super.onPostExecute(result);


            new DownloadItemImageTask(result,itemNames).execute();
            BaseAdapter adapter = new ItemList(FortniteShop.this,result,itemNames, itemPrice,
                    Typeface.createFromAsset(getAssets(),"burbank.otf"), rarity);
            listOfItems.setAdapter(adapter);

            listOfItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent skinIntent = new Intent(FortniteShop.this,SkinIntent.class);
                    skinIntent.putExtra(INTENT_ID, itemNames.get(position));
                    skinIntent.putExtra(OUTFIT_TYPE, rarity.get(position));
                    skinIntent.putExtra(RARITY, outfitType.get(position));
                    startActivity(skinIntent);
                }
            });
            adapter.notifyDataSetChanged();
            ProgressBar loading = (ProgressBar) findViewById(R.id.shop_loading);
            loading.setVisibility(View.GONE);

            listOfItems.setVisibility(View.VISIBLE);
        }
    }

    private class DownloadItemImageTask extends AsyncTask<String, Void, Bitmap>{

        ArrayList<String> urls;
        ArrayList<String> names;

        public DownloadItemImageTask(ArrayList<String> urls, ArrayList<String> names){
            this.urls = urls;
            this.names = names;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap imageBitmap = null;
                try {
                    for(int i = 0;i < urls.size();i++) {
                        InputStream inputStream = new URL(urls.get(i)).openStream();
                        imageBitmap = BitmapFactory.decodeStream(inputStream);
                        setBitmapToMemoryCache(names.get(i), imageBitmap);
                    }
                } catch (Exception e) {

                }

            return imageBitmap;
        }

        protected void onPostExecute(Bitmap result){

        }
    }
    public void onSkinsClick(View v){
        Intent intent = new Intent(FortniteShop.this, AllSkins.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override                                                                                                                                                                                                                       
    public void onStop() {
        super.onStop();
    }

    public static Bitmap getBitmapFromMemoryCache(String key){
        return mMemoryCache.get(key);
    }
    public static void setBitmapToMemoryCache(String key, Bitmap bitmap){
        mMemoryCache.put(key, bitmap);
    }
}
