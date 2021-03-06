package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ggjimmy on 4/11/18.
 */

public class WishlistAdapter extends BaseAdapter {

    public ArrayList<Set<String>> items;
    public Context context;
    public SharedPreferences.Editor prefs;
    public String name;
    public String url;
    public ArrayList<String> key;

    public WishlistAdapter(Context context, ArrayList<Set<String>> items, ArrayList<String> key){
        this.context = context;
        this.items = items;
        this.key = key;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View v = View.inflate(this.context, R.layout.wishlist_list, null);
        prefs = context.getSharedPreferences("fshop.wishlist", Context.MODE_PRIVATE).edit();
        Set<String> set = items.get(position);
        String price = "";
        String rarity = "";
        for(Iterator<String> it = set.iterator();it.hasNext();){
            String data = it.next();
            if(data.startsWith("5a")){
                url = data;
            }else if(data.startsWith("1") || data.startsWith("2") || data.startsWith("8") ||
                    data.startsWith("5") || data.equals("???")){
                price = data;
            }else if(data.equals("legendary") || data.equals("epic") || data.equals("rare")
                    || data.equals("uncommon")){
                rarity = data;
            }else if(data.equals("outfit") || data.equals("pickaxe") || data.equals("glider")
                    || data.equals("emote")) {

            }else{
                name = data;
            }
        }

        TextView itemName = (TextView) v.findViewById(R.id.wishlist_list_skinname);
        itemName.setTypeface(Typeface.createFromAsset(context.getAssets(), "burbank.otf"));
        itemName.setText(name);
        TextView itemPrice = (TextView) v.findViewById(R.id.wishlist_list_skinprice);
        itemPrice.setTypeface(Typeface.createFromAsset(context.getAssets(), "burbank.otf"));
        itemPrice.setText(price);
        ImageView imageView = (ImageView) v.findViewById(R.id.wishlist_list_image);
        Picasso.with(imageView.getContext()).load("file:///android_asset/a"+url+".png").into(imageView);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.wishlist_list_layout_image);
        ImageView remove = (ImageView) v.findViewById(R.id.wishlist_remove);
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View va) {
                items.remove(position);
                notifyDataSetChanged();
                prefs.remove(key.get(position)).apply();
                key.remove(position);
            }
        });

        switch (rarity) {
            case "legendary":
                layout.setBackgroundResource(R.drawable.wishlist_legendary);
                break;

            case "epic":
                layout.setBackgroundResource(R.drawable.wishlist_epic);
                break;

            case "rare":
                layout.setBackgroundResource(R.drawable.wishlist_rare);
                break;

            case "uncommon":
                layout.setBackgroundResource(R.drawable.wishlist_uncommon);
                break;
        }
        return v;
    }

}
