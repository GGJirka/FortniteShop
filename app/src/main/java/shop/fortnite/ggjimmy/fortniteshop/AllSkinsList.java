package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by ggjimmy on 4/1/18.
 */

public class AllSkinsList extends BaseAdapter{
    public SharedPreferences.Editor prefs;
    public static final String INTENT_ID = "SKIN_NAME";
    public static final String OUTFIT_TYPE = "OUTFIT_TYPE";
    public static final String RARITY = "RARITY";
    public static final String PRICE = "PRICE";
    public ArrayList<Drawable> drawables;
    public ArrayList<Drawable> drawables2;
    public SkinHolder urls;
    public SkinHolder names;
    public SkinHolder prices;
    public SkinHolder rarity;
    public SkinHolder outfitType;
    public Context context;

    public AllSkinsList(Context context, SkinHolder urls, SkinHolder names, SkinHolder prices,
                        SkinHolder rarity,SkinHolder outfitType,ArrayList<Drawable> drawables, ArrayList<Drawable> drawables2){
        this.context = context;
        this.urls = urls;
        this.names = names;
        this.prices = prices;
        this.rarity = rarity;
        this.outfitType = outfitType;
        this.drawables = drawables;
        this.drawables2 = drawables2;
        prefs = context.getSharedPreferences("fshop.wishlist", Context.MODE_PRIVATE).edit();
    }

    @Override
    public int getCount() {
        return this.urls.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.urls.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        try {
            v = View.inflate(this.context, R.layout.skins_layout, null);


            final ImageView skin1 = (ImageView) v.findViewById(R.id.item_img);
            final ImageView skin2 = (ImageView) v.findViewById(R.id.item_img2);

            TextView name1 = (TextView) v.findViewById(R.id.item_name1);
            TextView name2 = (TextView) v.findViewById(R.id.item_name2);

            TextView price1 = (TextView) v.findViewById(R.id.item_price1);
            TextView price2 = (TextView) v.findViewById(R.id.item_price2);

            Drawable vbucksIcon = context.getResources().getDrawable(R.drawable.vbucks_mini);

            if (prices.list.get(position).contains(",") || prices.list.get(position).length() < 4) {
                if (!prices.list.get(position).contains("?")) {
                    price1.setCompoundDrawablesWithIntrinsicBounds(vbucksIcon, null, null, null);
                }
            }
            if (prices.list2.get(position).contains(",") || prices.list2.get(position).length() < 4) {
                if (!prices.list2.get(position).contains("?")) {
                    price2.setCompoundDrawablesWithIntrinsicBounds(vbucksIcon, null, null, null);
                }
            }
            LinearLayout layout1 = (LinearLayout) v.findViewById(R.id.item_layout1);
            LinearLayout layout2 = (LinearLayout) v.findViewById(R.id.item_layout2);

            setDrawable(rarity.list.get(position), layout1, 1, position);
            setDrawable(rarity.list2.get(position), layout2, 2, position);

            price1.setText(prices.list.get(position));
            price2.setText(prices.list2.get(position));

            name1.setText(names.list.get(position));
            name2.setText(names.list2.get(position));
            setFont(name1);
            setFont(name2);
            setFont(price1);
            setFont(price2);

            String[] split = urls.list.get(position).split("/");
            String[] split2 = urls.list2.get(position).split("/");
            Picasso.with(skin1.getContext()).load("file:///android_asset/a"+split[split.length-2]+".png").into(skin1);
            Picasso.with(skin2.getContext()).load("file:///android_asset/a"+split2[split2.length-2]+".png").into(skin2);
            v.setTag(position);

        }catch(Exception e){
            Log.e("IMAGE",e.getMessage());
        }
        return v;
    }


    public void setFont(TextView text){
        text.setTypeface(Typeface.createFromAsset(context.getAssets(),"burbank.otf"));
    }

    public void setDrawable(final String rarita, final LinearLayout layout, final int index, final int position){
        try {
            switch (rarita) {
                case "legendary":
                    layout.setBackgroundResource(R.drawable.legendary);
                    break;

                case "epic":
                    layout.setBackgroundResource(R.drawable.epic);
                    break;

                case "rare":
                    layout.setBackgroundResource(R.drawable.rare);
                    break;

                case "uncommon":
                    layout.setBackgroundResource(R.drawable.uncommon);
                    break;
            }

            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Set<String> set = new HashSet<>();
                    ArrayList<String> data = new ArrayList<>();

                    if(index == 1) {
                        if(prices.list.get(position).startsWith("1") || prices.list.get(position).startsWith("2")
                                || prices.list.get(position).startsWith("8") || prices.list.get(position).startsWith("5")) {
                            data.add(names.list.get(position));
                            data.add(prices.list.get(position));
                            data.add(rarity.list.get(position));
                            String[] split = urls.list.get(position).split("/");
                            data.add(split[split.length - 2]);
                            set.addAll(data);
                            prefs.putStringSet(names.list.get(position), set);
                            prefs.apply();
                        }
                    }else{
                        if(prices.list2.get(position).startsWith("1") || prices.list2.get(position).startsWith("2")
                                || prices.list2.get(position).startsWith("8") || prices.list2.get(position).startsWith("5")) {
                            data.add(names.list2.get(position));
                            data.add(prices.list2.get(position));
                            data.add(rarity.list2.get(position));
                            String[] split = urls.list2.get(position).split("/");
                            data.add(split[split.length - 2]);
                            set.addAll(data);
                            prefs.putStringSet(names.list2.get(position), set);
                            prefs.apply();
                        }
                    }
                    Toast.makeText(context,"Added to wishlist",Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    switch (rarita) {
                        case "legendary":
                            layout.setBackgroundResource(R.drawable.legendary_selected);
                            break;

                        case "epic":
                            layout.setBackgroundResource(R.drawable.epic_selected);
                            break;

                        case "rare":
                            layout.setBackgroundResource(R.drawable.rare_selected);
                            break;

                        case "uncommon":
                            layout.setBackgroundResource(R.drawable.uncommon_selected);
                            break;
                    }

                    Intent skinIntent = new Intent(context, SkinIntent.class);
                    if (index == 1) {
                        skinIntent.putExtra(INTENT_ID, names.list.get(position));
                        skinIntent.putExtra(OUTFIT_TYPE, rarity.list.get(position));
                        skinIntent.putExtra(RARITY, outfitType.list.get(position));
                        skinIntent.putExtra(PRICE, prices.list.get(position));
                    } else {
                        skinIntent.putExtra(INTENT_ID, names.list2.get(position));
                        skinIntent.putExtra(OUTFIT_TYPE, rarity.list2.get(position));
                        skinIntent.putExtra(RARITY, outfitType.list2.get(position));
                        skinIntent.putExtra(PRICE, prices.list2.get(position));
                    }
                    context.startActivity(skinIntent);
                }
            });
        }catch(Exception e){

        }
    }
}