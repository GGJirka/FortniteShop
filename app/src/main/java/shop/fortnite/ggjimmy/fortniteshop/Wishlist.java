package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Wishlist extends AppCompatActivity {
    public SharedPreferences prefs;
    public ListView listView;
    public ArrayList<Set<String>> skinNames;
    public ArrayList<String> key;
    public BaseAdapter adapter;

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
        adapter = new WishlistAdapter(Wishlist.this, skinNames,key);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),skinNames.get(position).toString(),Toast.LENGTH_SHORT).show();
            }
        });
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
