package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.content.Intent;
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

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by ggjimmy on 4/1/18.
 */

public class AllSkinsListFake {

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
    public int position;

    public AllSkinsListFake(Context context, SkinHolder urls, SkinHolder names, SkinHolder prices,
                        SkinHolder rarity,SkinHolder outfitType,int position){
        this.context = context;
        this.urls = urls;
        this.names = names;
        this.prices = prices;
        this.rarity = rarity;
        this.outfitType = outfitType;
        this.position = position;
    }


    public View getView() {
        View v = null;
        try {
            v = View.inflate(this.context, R.layout.skins_layout, null);


            final ImageView skin1 = (ImageView) v.findViewById(R.id.item_img);
            final ImageView skin2 = (ImageView) v.findViewById(R.id.item_img2);

            TextView name1 = (TextView) v.findViewById(R.id.item_name1);
            TextView name2 = (TextView) v.findViewById(R.id.item_name2);

            TextView price1 = (TextView) v.findViewById(R.id.item_price1);
            TextView price2 = (TextView) v.findViewById(R.id.item_price2);

            //Drawable vbucksIcon = context.getResources().getDrawable(R.drawable.vbucks_icon);

            if (prices.list.get(position).contains(",") || prices.list.get(position).length() < 4) {
                if (!prices.list.get(position).contains("?")) {
                    //price1.setCompoundDrawablesWithIntrinsicBounds(vbucksIcon, null, null, null);
                }
            }
            if (prices.list2.get(position).contains(",") || prices.list2.get(position).length() < 4) {
                if (!prices.list2.get(position).contains("?")) {
                    //price2.setCompoundDrawablesWithIntrinsicBounds(vbucksIcon, null, null, null);
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

            final String[] split = urls.list.get(position).split("/");
            final String[] split2 = urls.list2.get(position).split("/");


            new SetImageAsyncTask(skin1,"a"+split[split.length-2]).execute();
            new SetImageAsyncTask(skin2,"a"+split2[split2.length-2]).execute();


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

            layout.setOnClickListener(new View.OnClickListener() {
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

    private class SetImageAsyncTask extends AsyncTask<Void, Void, Drawable>{
        ImageView imageView;
        String name;

        public SetImageAsyncTask(ImageView imageView,String name){
            this.imageView = imageView;
            this.name = name;
        }

        @Override
        protected Drawable doInBackground(Void... params){
            int id = 0;
            try {
                id = imageView.getContext().getResources().getIdentifier(name, "drawable", imageView.getContext().getPackageName());
            }catch(Exception e){
                Log.e("POST","exception",e);
            }
            return imageView.getContext().getResources().getDrawable(id);
        }

        @Override
        protected void onPostExecute(Drawable drawable){
            super.onPostExecute(drawable);
            try {
                imageView.setImageDrawable(drawable);
            }catch(Exception n){
                Log.e("POST","exception",n);
            }
        }
    }
}