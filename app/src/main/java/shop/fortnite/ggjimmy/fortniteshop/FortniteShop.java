package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class FortniteShop extends AppCompatActivity {

    public ArrayList<String> urls;
    public ArrayList<String> itemNames;
    public ArrayList<String> itemPrice;
    public ArrayList<String> rarity;
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
        listOfItems = (ListView) findViewById(R.id.items);
        JsoupAsyncTask asyncTask = new JsoupAsyncTask();
        asyncTask.execute();

        TextView mainTitle = (TextView) findViewById(R.id.main_title);
        mainTitle.setTypeface(Typeface.createFromAsset(getAssets(),"burbank.otf"));
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
                                rarity.add("uncommon");
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
            listOfItems.setAdapter(new ItemList(FortniteShop.this,result,itemNames, itemPrice,
                    Typeface.createFromAsset(getAssets(),"burbank.otf"), rarity));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
