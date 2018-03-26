package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ggjimmy on 3/26/18.
 */

public class ItemList extends BaseAdapter {
    public Context context;
    public ArrayList<String> items;

    //constructor param: list of items
    public ItemList(Context context,ArrayList<String> items){
        this.context = context;
        this.items = items;
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
        TextView itemName = (TextView) v.findViewById(R.id.item_name);
        itemName.setText(this.items.get(position));
        v.setTag(position);
        return v;
    }
}
