package shop.fortnite.ggjimmy.fortniteshop;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AllSkins extends AppCompatActivity {
    public MaterialSearchView search;
    public ListView listView;
    public SkinHolder urls;
    public SkinHolder names;
    public SkinHolder prices;
    public SkinHolder rarity;
    public SkinHolder outfitType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_skins);
        Toolbar toolbar = (Toolbar) findViewById(R.id.skins_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        TextView title = (TextView) findViewById(R.id.all_skins_title);
        title.setText("All Skins");
        title.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
        listView = (ListView) findViewById(R.id.all_skins_list_view);
        search = (MaterialSearchView) findViewById(R.id.search_view);
        urls = new SkinHolder();
        names = new SkinHolder();
        prices = new SkinHolder();
        rarity = new SkinHolder();
        outfitType = new SkinHolder();
        new JsoupAsyncTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        search.setMenuItem(item);
        return true;
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
                        for(Element item : items){

                            if(item.attr("class").contains("card-img-top")){
                                String[] split = item.attr("class").split(" ");
                                s = split[split.length-1];
                            }
                            if(!s.equals("backpack")) {
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
            }catch(IOException e){

            }
            return urls.list;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result){
            listView.setAdapter(new AllSkinsList(AllSkins.this, urls, names, prices, rarity,outfitType));
        }
    }
}
