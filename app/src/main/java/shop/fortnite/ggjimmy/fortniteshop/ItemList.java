package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


/**
 * Created by ggjimmy on 3/26/18.
 */

public class ItemList extends BaseAdapter {
    public Context context;
    public ArrayList<String> items;
    public ArrayList<String> itemNames;
    public ArrayList<String> price;
    public ImageView itemImg;
    public TextView itemText;
    public TextView itemPrice;
    public Typeface font;
    public ArrayList<String> rarity;

    //constructor param: list of items
    public ItemList(Context context, ArrayList<String> items, ArrayList<String> itemNames, ArrayList<String> price,
                    Typeface font, ArrayList<String> rarity){
        this.context = context;
        this.items = items;
        this.itemNames = itemNames;
        this.price = price;
        this.font = font;
        this.rarity = rarity;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(this.context, R.layout.item_list, null);
        final LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.item_layout);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        Date time = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("h:mm:ss");
        String dateAsString = outputFmt.format(time);
        String[] data = dateAsString.split(":");

        if(position == 0){
            TextView featured = (TextView) v.findViewById(R.id.featured_item);
            featured.setTextSize(32);
            featured.setTypeface(font);
            featured.setVisibility(View.VISIBLE);
        }

        final int hours = 24-calendar.get(Calendar.HOUR_OF_DAY)-1;
        final int minutes = 60-Integer.parseInt(data[1])-1;
        final int seconds = 60-Integer.parseInt(data[2]);
        final int millis = (hours*60*60 + minutes*60+seconds)*1000;

        if(position == this.itemNames.size()-7){

            final TextView dailyItems = (TextView) v.findViewById(R.id.daily_item);
            final TextView dailyTimes = (TextView) v.findViewById(R.id.daily_time);
            final LinearLayout daily = (LinearLayout) v.findViewById(R.id.featured);
            dailyItems.setText("DAILY ITEMS");
            dailyItems.setTypeface(font);
            dailyTimes.setTypeface(font);
            dailyTimes.setTextSize(29);
            dailyItems.setTextSize(29);
            daily.setBackgroundResource(R.drawable.daily_items);

            new CountDownTimer(millis, 1000) {
                public void onTick(long millisUntilFinished){
                    int hours = (int) millisUntilFinished/(1000*60*60);
                    int minutes = (int) millisUntilFinished/(1000*60);
                    int seconds = (int) millisUntilFinished/1000;
                    String mtoMin = String.valueOf((minutes-hours*60));
                    String mtoSec = String.valueOf((seconds-minutes*60));

                    if(Integer.valueOf(mtoMin)<10){
                        mtoMin = "0"+Integer.valueOf(mtoMin);
                    }
                    if(Integer.valueOf(mtoSec)<10){
                        mtoSec = "0"+Integer.valueOf(mtoSec);
                    }
                    dailyTimes.setText(hours+":"+mtoMin+":"+mtoSec);
                }

                public void onFinish() {
                    dailyTimes.setText("00:00:00");
                }
            }.start();
        }

        itemImg = (ImageView) v.findViewById(R.id.item_img);
        itemText = (TextView) v.findViewById(R.id.item_name);
        itemPrice = (TextView) v.findViewById(R.id.item_price);
        new DownloadItemImageTask(itemImg).execute(items.get(position));
        itemText.setText(itemNames.get(position));
        itemPrice.setText(price.get(position));
        itemText.setTypeface(font);

        switch(rarity.get(position)){
            case "legendary":
                linearLayout.setBackgroundResource(R.drawable.legendary_onclick);
                break;

            case "epic":
                linearLayout.setBackgroundResource(R.drawable.list_selector);
                break;

            case "rare":
                linearLayout.setBackgroundResource(R.drawable.rare_onclick);
                break;

            case "uncommon":
                linearLayout.setBackgroundResource(R.drawable.uncommon_onclick);
                break;
        }

        v.setTag(position);

        return v;
    }
    private class DownloadItemImageTask extends AsyncTask<String, Void, Bitmap>{

        private ImageView img;

        public DownloadItemImageTask(ImageView img){
            this.img = img;
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
            img.setImageBitmap(result);
        }
    }
}
