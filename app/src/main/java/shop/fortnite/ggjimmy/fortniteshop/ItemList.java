package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ggjimmy on 3/26/18.
 */

public class ItemList extends BaseAdapter {
    public Context context;
    public ArrayList<String> items;
    public ArrayList<String> itemNames;
    public ImageView itemImg;
    public TextView itemText;

    //constructor param: list of items
    public ItemList(Context context, ArrayList<String> items, ArrayList<String> itemNames){
        this.context = context;
        this.items = items;
        this.itemNames = itemNames;
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
        itemImg = (ImageView) v.findViewById(R.id.item_img);
        itemText = (TextView) v.findViewById(R.id.item_name);
        new DownloadItemImageTask(itemImg).execute(items.get(position));
        itemText.setText(itemNames.get(position));
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
