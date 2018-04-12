package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

public class AllSkins extends AppCompatActivity {

    public ArrayList<Drawable> drawables;
    public ArrayList<Drawable> drawables2;
    public MaterialSearchView search;
    public ListView listView;
    public SkinHolder urls;
    public SkinHolder names;
    public SkinHolder prices;
    public SkinHolder rarity;
    public SkinHolder outfitType;
    public BaseAdapter adapter;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            public void uncaughtException(Thread paramThread, Throwable paramThrowable){
                Log.e("error"+Thread.currentThread().getStackTrace()[2],paramThrowable.getLocalizedMessage());
            }
        });
        setContentView(R.layout.activity_all_skins);
        Toolbar toolbar = (Toolbar) findViewById(R.id.skins_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        TextView title = (TextView) findViewById(R.id.all_skins_title);
        title.setText("All Items");
        title.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.set_type);
        toolbar.setOverflowIcon(drawable);
        listView = (ListView) findViewById(R.id.all_skins_list_view);
        search = (MaterialSearchView) findViewById(R.id.search_view);
        urls = new SkinHolder();
        names = new SkinHolder();
        prices = new SkinHolder();
        rarity = new SkinHolder();
        outfitType = new SkinHolder();
        drawables = new ArrayList<>();

        MobileAds.initialize(this, "ca-app-pub-5090360471586053~9240769580");
        adView = (AdView) findViewById(R.id.all_skins_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        try {
            new JsoupAsyncTask().execute();
            setType("outfit");
        }catch(Exception e){}
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //getMenuInflater().inflate(R.menu.menu_item,menu);
        getMenuInflater().inflate(R.menu.sort_skins,menu);
        //MenuItem item = menu.findItem(R.id.action_search);
        //search.setMenuItem(item);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    this.finish();
                    break;
                case R.id.alllskins_favorites:
                    startActivity(new Intent(AllSkins.this, Wishlist.class));
                case R.id.sort_outfit:
                    setType("outfit");
                    item.setChecked(true);
                    break;
                case R.id.sort_emote:
                    setType("emote");
                    item.setChecked(true);
                    break;
                case R.id.sort_glinder:
                    setType("glider");
                    item.setChecked(true);
                    break;
                case R.id.sort_pickaxe:
                    setType("pickaxe");
                    item.setChecked(true);
                    break;

            }
        }catch(Exception e){}
        return true;
    }

    public void setType(String type) throws Exception{
        SkinHolder urls = new SkinHolder();
        SkinHolder names = new SkinHolder();
        SkinHolder prices = new SkinHolder();
        SkinHolder rarity = new SkinHolder() ;
        SkinHolder outfitType = new SkinHolder() ;

        for(int i=0;i<this.outfitType.list.size();i++){
            if(this.outfitType.list.get(i).equals(type) && !this.urls.list.get(i).equals("")
                    && !this.prices.list.get(i).equals("")){
                if(!this.names.equals("")) {
                    urls.list.add(this.urls.list.get(i));
                    names.list.add(this.names.list.get(i));
                    prices.list.add(this.prices.list.get(i));
                    rarity.list.add(this.rarity.list.get(i));
                    outfitType.list.add(this.outfitType.list.get(i));
                }
            }
        }

        for(int i=0;i<this.outfitType.list2.size();i++){
            if(this.outfitType.list2.get(i).equals(type) && !this.urls.list2.get(i).equals("")
                    && !this.prices.list2.get(i).equals("")){
                if(!this.names.list.get(i).equals("")) {
                    urls.list2.add(this.urls.list2.get(i));
                    names.list2.add(this.names.list2.get(i));
                    prices.list2.add(this.prices.list2.get(i));
                    rarity.list2.add(this.rarity.list2.get(i));
                    outfitType.list2.add(this.outfitType.list2.get(i));
                }
            }
        }
        if(type.equals("emote") || type.equals("pickaxe")) {
            for (int i = 5; i > 0; i--) {
                urls.list.remove(urls.list.size() - i);
            }
        }
        adapter = new AllSkinsList(AllSkins.this, urls, names, prices, rarity,outfitType, drawables, drawables2);
        listView.setAdapter(adapter);
    }


    private class JsoupAsyncTask extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try{
                Document d = Jsoup.connect("https://fnbr.co").get();
                Elements elements = d.getElementsByAttribute("class");
                int urlsCount = 0;
                int namesCount = 0;
                int pricesCount = 0;
                int rarityCount = 0;
                int outfitCount = 0;
                for(Element div : elements){
                    if(div.attr("class").equals("row items-display")){
                        Elements items = div.getAllElements();
                        String s="";
                        String common = "";
                        for(Element item : items){
                            if (item.attr("class").contains("card splash-card")) {
                                String[] split = item.attr("class").split("-");
                                common = split[split.length-1];
                            }

                            if(item.attr("class").contains("card-img-top")){
                                String[] split = item.attr("class").split(" ");
                                s = split[split.length-1];
                            }

                            if(!s.equals("backpack") && !s.equals("loading") && !s.equals("emoji") && !s.equals("skydive")) {
                                if(!common.equals("common")) {
                                    if (item.attr("class").equals("card-text itemprice")) {
                                        if (pricesCount % 2 == 0) {
                                            prices.list.add(item.text());
                                        } else {
                                            prices.list2.add(item.text());
                                        }
                                        pricesCount++;
                                    }

                                    if (item.attr("data-src") != "") {
                                        if (urlsCount % 2 == 0) {
                                            urls.list.add(item.attr("data-src"));
                                        } else {
                                            urls.list2.add(item.attr("data-src"));
                                        }
                                        urlsCount++;
                                    }
                                    if (item.attr("class").equals("card-title itemname")) {
                                        if (namesCount % 2 == 0) {
                                            names.list.add(item.text());
                                        } else {
                                            names.list2.add(item.text());
                                        }
                                        namesCount++;
                                    }

                                    if (item.attr("class").contains("card splash-card")) {
                                        String[] split = item.attr("class").split("-");
                                        if (rarityCount % 2 == 0) {
                                            rarity.list.add(split[split.length - 1]);
                                        } else {
                                            rarity.list2.add(split[split.length - 1]);
                                        }
                                        rarityCount++;
                                    }

                                    if (item.attr("class").contains("card-img-top")) {
                                        String[] split = item.attr("class").split(" ");
                                        if (outfitCount % 2 == 0) {
                                            outfitType.list.add(split[split.length - 1]);
                                        } else {
                                            outfitType.list2.add(split[split.length - 1]);
                                        }
                                        outfitCount++;
                                    }
                                }
                            }
                        }
                    }
                }
            }catch(Exception e){

            }
            return urls.list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result){
            try {
                setType("outfit");
            } catch (Exception e) {

            }
        }
    }

}