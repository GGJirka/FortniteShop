package shop.fortnite.ggjimmy.fortniteshop;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FortniteShop extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String INTENT_ID = "SKIN_NAME";
    public static final String OUTFIT_TYPE = "OUTFIT_TYPE";
    public static final String RARITY = "RARITY";
    public static ArrayList<String> urls2;
    public static ArrayList<String> outfitType2;
    public static ArrayList<String> rarity2;
    public static ArrayList<String> itemNames2;
    public ArrayList<String> urls;
    public ArrayList<String> itemNames;
    public ArrayList<String> itemPrice;
    public ArrayList<String> rarity;
    public ArrayList<String> outfitType;
    public ArrayList<Integer> id;
    public int featuredId = 2;
    public RequestQueue requestQueue;
    public ListView listOfItems;
    private AdView mAdView;
    private JsoupAsyncTask asyncTask;
    private BaseAdapter adapter;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(this);
        urls = new ArrayList<>();
        itemNames = new ArrayList<>();
        itemPrice = new ArrayList<>();
        rarity = new ArrayList<>();
        itemNames2 = new ArrayList<>();
        outfitType = new ArrayList<>();
        urls2 = new ArrayList<>();
        outfitType2 = new ArrayList<>();
        rarity2 = new ArrayList<>();
        listOfItems = (ListView) findViewById(R.id.items);
        id = new ArrayList<>();
        connect();


        InitializeWishlist wishlist = new InitializeWishlist(FortniteShop.this, itemNames);
        wishlist.init();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            public void uncaughtException(Thread paramThread, Throwable paramThrowable){
                Log.e("error"+Thread.currentThread().getStackTrace()[2],paramThrowable.getLocalizedMessage());
            }
        });


        TextView mainTitle = (TextView) findViewById(R.id.main_title);

        mainTitle.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
        MobileAds.initialize(this, "ca-app-pub-5090360471586053~1383172270");

        mAdView = (AdView) findViewById(R.id.example);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
    public boolean connect(){
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }

        if(connected) {
            try {
                asyncTask = new JsoupAsyncTask();
                asyncTask.execute();
                listOfItems.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listOfItems.setVisibility(View.VISIBLE);
                return true;
            } catch (Exception e) {

            }
        }else{
            LinearLayout layout = (LinearLayout) findViewById(R.id.no_internet_layout);
            layout.setVisibility(View.VISIBLE);

            ProgressBar loading = (ProgressBar) findViewById(R.id.shop_loading);
            loading.setVisibility(View.GONE);

            TextView text = (TextView) findViewById(R.id.no_internet_text);
            text.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
            Button button = (Button) findViewById(R.id.no_internet_button);
            button.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"burbank.otf"));
        }
        return false;
    }

    public void onClick(View v) throws InterruptedException {
        ProgressBar loading = (ProgressBar) findViewById(R.id.shop_loading);
        loading.setVisibility(View.VISIBLE);
        LinearLayout layout = (LinearLayout) findViewById(R.id.no_internet_layout);
        layout.setVisibility(View.INVISIBLE);

        new CountDownTimer(1000, 1) {


            @Override
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                connect();
            }
        }.start();



    }
    @Override
    public void onStart(){
        super.onStart();

    }
    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            JsonObjectRequest jsonObjectRequest = new
                    JsonObjectRequest(Request.Method.GET,
                            "https://fnbr.co/api/shop",
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONObject data = response.getJSONObject("data");
                                        JSONArray array = data.getJSONArray("featured");
                                        JSONArray daily = data.getJSONArray("daily");
                                        id.add(array.length());
                                        for(int i=0;i<array.length();i++){
                                            JSONObject object = array.getJSONObject(i);
                                            itemNames.add(object.getString("name"));
                                            itemPrice.add(object.getString("price"));
                                            urls.add(object.getString("id"));
                                            outfitType.add(object.getString("type"));
                                            rarity.add(object.getString("rarity"));
                                        }

                                        for(int i=0;i<daily.length();i++){
                                            JSONObject object = daily.getJSONObject(i);
                                            itemNames.add(object.getString("name"));
                                            itemPrice.add(object.getString("price"));
                                            urls.add(object.getString("id"));
                                            outfitType.add(object.getString("type"));
                                            rarity.add(object.getString("rarity"));
                                        }
                                        urls2 = urls;
                                        outfitType2 = outfitType;
                                        rarity2 = rarity;
                                        itemNames2 = itemNames;
                                        boolean isInShop = false;
                                        SharedPreferences prefs = getSharedPreferences("fshop.wishlist", Context.MODE_PRIVATE);
                                        int i = 0;
                                        int p = 0;
                                        for(String s : InitializeWishlist.key){
                                            for(String s2 : urls){
                                                if (s2.equals(s)) {
                                                    prefs.edit().remove(InitializeWishlist.key.get(i)).apply();
                                                    if(!isInShop){
                                                        showNotification(itemNames.get(p),rarity.get(p),outfitType.get(p));
                                                    }
                                                    isInShop = true;
                                                }
                                                p++;
                                            }
                                            i++;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Log.d(TAG, error.toString());
                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<>();

                            headers.put("x-api-key", "76a21e36-d55f-479d-94a8-52cc063bf99d");
                            return headers;
                        }
                    };
            requestQueue.add(jsonObjectRequest);
            return urls;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result){
            super.onPostExecute(result);
            try {
                 adapter = new ItemList(FortniteShop.this, result, itemNames, itemPrice,
                        Typeface.createFromAsset(getAssets(), "burbank.otf"), rarity,outfitType,id);
                listOfItems.setAdapter(adapter);

                listOfItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            if (!outfitType.get(position).equals("misc") && !outfitType.get(position).equals("backpack")) {
                                Intent intent = new Intent(FortniteShop.this, SkinIntentFragment.class);
                                intent.putExtra("START_POSITION", position);
                                startActivity(intent);
                            }
                        }catch(Exception e){

                        }
                    }
                });
                //adapter.notifyDataSetChanged();
                ProgressBar loading = (ProgressBar) findViewById(R.id.shop_loading);
                loading.setVisibility(View.GONE);

                listOfItems.setVisibility(View.VISIBLE);
            }catch(Exception e){

            }

        }
    }
    public void showNotification(String itemName, String rarity, String outfitType){
        //PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), SkinIntent.class),0);
        Intent showFullQuoteIntent = new Intent(this, SkinIntent.class);
        showFullQuoteIntent.putExtra(INTENT_ID, itemName);
        showFullQuoteIntent.putExtra(OUTFIT_TYPE, rarity);
        showFullQuoteIntent.putExtra(RARITY, outfitType);

        // both of these approaches now work: FLAG_CANCEL, FLAG_UPDATE; the uniqueInt may be the real solution.
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, showFullQuoteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, showFullQuoteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("ITEM SHOP")
                .setContentText("A skin you want is in the shop!")
                .setSmallIcon(R.drawable.all_skins_icon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManagerCompat.from(getApplicationContext()).notify(new Random().nextInt(),notification);
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
        //startActivity(new Intent(FortniteShop.this, FortniteShop2.class));
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
    public void setId(int id){
        this.featuredId = id;
    }
}
