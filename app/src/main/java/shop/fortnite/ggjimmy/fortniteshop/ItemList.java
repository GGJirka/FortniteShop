package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;



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
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.item_layout);

        if(position == this.itemNames.size()-7){
            TextView dailyItems = (TextView) v.findViewById(R.id.daily_items);
            dailyItems.setText("DAILY ITEMS"+""+"23:59:59");
            dailyItems.setTypeface(font);
            dailyItems.setBackgroundResource(R.drawable.daily_items);
            dailyItems.setTextSize(32);

        }

        itemImg = (ImageView) v.findViewById(R.id.item_img);
        itemText = (TextView) v.findViewById(R.id.item_name);
        itemPrice = (TextView) v.findViewById(R.id.item_price);
        new DownloadItemImageTask(itemImg).execute(items.get(position));
        itemText.setText(itemNames.get(position));
        itemPrice.setText(price.get(position));
        itemText.setTypeface(font);


        switch(rarity.get(position)) {
            case "legendary":
                linearLayout.setBackgroundResource(R.drawable.legendary);
                break;

            case "epic":
                linearLayout.setBackgroundResource(R.drawable.epic);
                break;

            case "rare":
                linearLayout.setBackgroundResource(R.drawable.rare);
                break;

            case "uncommon":
                linearLayout.setBackgroundResource(R.drawable.uncommon);
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
