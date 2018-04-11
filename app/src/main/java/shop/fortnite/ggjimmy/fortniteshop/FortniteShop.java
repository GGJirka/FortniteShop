package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import java.util.ArrayList;

public class FortniteShop extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
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
    public DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        toggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        urls = new ArrayList<>();
        itemNames = new ArrayList<>();
        itemPrice = new ArrayList<>();
        rarity = new ArrayList<>();
        outfitType = new ArrayList<>();
        listOfItems = (ListView) findViewById(R.id.items);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            public void uncaughtException(Thread paramThread, Throwable paramThrowable){
                Log.e("error"+Thread.currentThread().getStackTrace()[2],paramThrowable.getLocalizedMessage());
            }
        });
        try {
            JsoupAsyncTask asyncTask = new JsoupAsyncTask();
            asyncTask.execute();
        }catch(Exception e){

        }
        TextView mainTitle = (TextView) findViewById(R.id.main_title);

        mainTitle.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
        MobileAds.initialize(this, "ca-app-pub-5090360471586053~9240769580");

        mAdView = (AdView) findViewById(R.id.example);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void  manageDrawView(View v){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

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
                                String[] raritys = index.attr("class").split("-");
                                rarity.add(raritys[raritys.length-1]);
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
            try {
                BaseAdapter adapter = new ItemList(FortniteShop.this, result, itemNames, itemPrice,
                        Typeface.createFromAsset(getAssets(), "burbank.otf"), rarity,false);
                listOfItems.setAdapter(adapter);

                listOfItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent skinIntent = new Intent(FortniteShop.this, SkinIntent.class);
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
            }catch(Exception e){

            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    public void onImageRight(View v){
        startActivity(new Intent(FortniteShop.this,AllSkins.class));
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_all_items) {
            startActivity(new Intent(FortniteShop.this, AllSkins.class));
        }else if(id == R.id.drawer_wishlist){
            startActivity(new Intent(FortniteShop.this, Wishlist.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
