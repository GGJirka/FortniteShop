package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by ggjimmy on 4/16/18.
 */

public class InitializeWishlist {

    public SharedPreferences prefs;
    public static ArrayList<String> key;
    public static boolean notification = false;
    public ArrayList<String> urls;
    public Context context;

    public InitializeWishlist(Context context, ArrayList<String> urls){
        this.context = context;
        key = new ArrayList<>();
        this.urls = urls;
    }

    public void init(){
        ArrayList<String> holdKeys  = new ArrayList<>();

        try {
            prefs = context.getSharedPreferences("fshop.wishlist", Context.MODE_PRIVATE);
            Map<String, ?> allEntries = prefs.getAll();

            for (Map.Entry<String, ?> entry : allEntries.entrySet()){
                String s = entry.getKey();
                holdKeys.add(s);
            }
        }catch(Exception e){

        }
        key = holdKeys;
    }


}
