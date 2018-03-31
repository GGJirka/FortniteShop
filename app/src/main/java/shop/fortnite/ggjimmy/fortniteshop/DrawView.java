package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

/**
 * Created by ggjimmy on 3/31/18.
 */
public class DrawView extends ImageView {
    public String rarity;

    public DrawView(Context context,String rarity) {
        super(context);
        this.rarity = rarity;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        final float scale = getResources().getDisplayMetrics().density;
        switch(this.rarity) {
            case "legendary":
                paint.setColor(Color.parseColor("#ac592f"));
                break;
            case "epic":
                paint.setColor(Color.parseColor("#833ca4"));
                break;
            case "rare":
                paint.setColor(Color.parseColor("#2474b1"));
                break;
            case "uncommon":
                paint.setColor(Color.parseColor("#1c8b2f"));
                break;

        }
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
        float leftx = 0;
        float topy = 0;
        float rightx = 110;
        float bottomy = 200;
        canvas.drawRect(leftx, topy, rightx, bottomy, paint);
        canvas.drawRect(scale, topy, rightx, bottomy, paint);
    }
}
