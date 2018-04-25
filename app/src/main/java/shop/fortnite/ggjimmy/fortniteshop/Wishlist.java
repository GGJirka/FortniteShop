package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Wishlist extends AppCompatActivity {
    public SharedPreferences prefs;
    public ListView listView;
    public ArrayList<Set<String>> skinNames;
    public ArrayList<String> key;
    public BaseAdapter adapter;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.wishlist_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        TextView title = (TextView) findViewById(R.id.wishlist_title);
        title.setText("Wishlist");
        title.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
        skinNames = new ArrayList<>();
        key = new ArrayList<>();
        listView = (ListView) findViewById(R.id.wishlist_listview);
        try {
            prefs = getSharedPreferences("fshop.wishlist", Context.MODE_PRIVATE);
            Map<String, ?> allEntries = prefs.getAll();

            for (Map.Entry<String, ?> entry : allEntries.entrySet()){
                String s = entry.getKey();
                skinNames.add(prefs.getStringSet(s,null));
                key.add(s);
            }
        }catch(Exception e){

        }
        MobileAds.initialize(this, "ca-app-pub-5090360471586053~1383172270");
        mAdView = (AdView) findViewById(R.id.wishlist_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if(skinNames.isEmpty()){
            TextView introductionWishlist = (TextView) findViewById(R.id.notification_introduction);
            TextView introductionWishlist2 = (TextView) findViewById(R.id.notification_introduction2);
            introductionWishlist.setVisibility(View.VISIBLE);
            introductionWishlist2.setVisibility(View.VISIBLE);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Set<String> set = skinNames.get(position);
                String rarity="";
                String name="";
                String outfitType = "";
                for(Iterator<String> it = set.iterator();it.hasNext();){
                    String data = it.next();
                    if(data.startsWith("5a")){
                        //url = data;
                    }else if(data.startsWith("1") || data.startsWith("2") || data.startsWith("8") ||
                            data.startsWith("5") || data.equals("???")){
                        //price = data;
                    }else if(data.equals("legendary") || data.equals("epic") || data.equals("rare")
                            || data.equals("uncommon")){
                        rarity = data;
                    }else if(data.equals("outfit") || data.equals("pickaxe") || data.equals("glider")
                            || data.equals("emote")) {
                        outfitType = data;
                    }else{
                        name = data;
                    }
                }
                Intent skinIntent = new Intent(Wishlist.this, SkinIntent.class);
                skinIntent.putExtra(FortniteShop.INTENT_ID, name);
                skinIntent.putExtra(FortniteShop.OUTFIT_TYPE,rarity);
                skinIntent.putExtra(FortniteShop.RARITY, outfitType);
                startActivity(skinIntent);
            }
        });
        adapter = new WishlistAdapter(Wishlist.this, skinNames,key);
        listView.setAdapter(adapter);


    }

    @Override
    public void onResume(){
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
         switch (item.getItemId()) {
                case android.R.id.home:
                    this.finish();
                    break;
         }
        return true;
    }
}
