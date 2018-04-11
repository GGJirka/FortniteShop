package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Wishlist extends AppCompatActivity {
    public SharedPreferences prefs;
    public ListView listView;
    public ArrayList<Set<String>> skinNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.wishlist_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        skinNames = new ArrayList<>();
        listView = (ListView) findViewById(R.id.wishlist_listview);
        try {
            prefs = getSharedPreferences("fshop.wishlist", Context.MODE_PRIVATE);
            Map<String, ?> allEntries = prefs.getAll();

            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                skinNames.add(prefs.getStringSet(entry.getKey().toString(),null));
            }
        }catch(Exception e){

        }
        listView.setAdapter(new WishlistAdapter(Wishlist.this, skinNames));

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
