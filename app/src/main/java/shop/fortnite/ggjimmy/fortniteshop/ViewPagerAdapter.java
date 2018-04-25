package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by ggjimmy on 4/20/18.
 */

public class ViewPagerAdapter extends PagerAdapter implements FragmentLifecycle{
    Context context;
    VideoView video;
    private boolean isTouched = false;
    private Handler handler = new Handler();

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        try{
            return FortniteShop.outfitType2.size();
        }catch(Exception e){
            return 1;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /*public Fragment getItem(int position) {
        return getItem(position);
    }*/
    @Override
    public Object instantiateItem(ViewGroup group, int position){
        View v = View.inflate(context, R.layout.skin_intent_image, null);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.skin_intent_layout);

        if(!FortniteShop.outfitType2.get(position).toLowerCase().equals("emote")) {
            ImageView skin = new ImageView(context);

            skin.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            if (FortniteShop.outfitType2.get(position).toLowerCase().equals("outfit")) {
                skin.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            Picasso.with(context).load("https://image.fnbr.co/" + FortniteShop.outfitType2.get(position) + "/" + FortniteShop.urls2.get(position) + "/png.png").into(skin);
            layout.addView(skin);
            switch(FortniteShop.rarity2.get(position)) {
                case "legendary":
                    layout.setBackgroundResource(R.drawable.legendary_background);
                    break;
                case "epic":
                    layout.setBackgroundResource(R.drawable.epic_background);
                    break;
                case "rare":
                    layout.setBackgroundResource(R.drawable.rare_background);
                    break;
                case "uncommon":
                    layout.setBackgroundResource(R.drawable.common_background);
                    break;

            }
        }else{
            video = new VideoView(context);

            video.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            video.setVideoURI(Uri.parse(getUrl(position)));
            video.start();
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            video.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(!isTouched) {
                        isTouched = true;
                        if (video.isPlaying()) {
                            video.pause();
                        } else {
                            video.start();
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isTouched = false;
                            }
                        }, 100);
                    }
                    return true;
                }
            });
            layout.addView(video);
        }

        group.addView(v);
        return v;
    }

    @Override
    public void onPauseFragment() {
        if(video != null){
            video.pause();
        }
    }

    @Override
    public void onResumeFragment() {
        if(video != null){
            video.start();
        }
    }
    @Override
    public void destroyItem(ViewGroup group, int position, Object obj){
        ((ViewPager) group).removeView((View)obj);
    }

    public String getUrl(int position) {
        switch (FortniteShop.itemNames2.get(position).toLowerCase()) {
            case "flapper":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.flapper;
            case "fresh":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.shit_dance;
            case "electro shuffle":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.electro_shuffle;
            case "disco fever":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.disco;
            case "reanimated":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.reanimated;
            case "rocket rodeo":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.rocket_ride;
            case "best mates":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.best_mates;
            case "floss":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.floss;
            case "dab":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.dab;
            case "pure salt":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.salt;
            case "take the l":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.take_the_l;
            case "the worm":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.worm;
            case "jubilation":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.help;
            case "click!":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.click;
            case "finger guns":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.shot;
            case "slow clap":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.slow_clap;
            case "breakin'":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.dance;
            case "gun show'":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.gun_show;
            case "rock out":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.rock_out;
            case "the robot":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.robot;
            case "hootenanny":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.hotenanny;
            case "flippin' sexy":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.backflip;
            case "ride the pony":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.ride;
            case "make it rain":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.money_throw;
            case "step it up":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.step_it_up;

            case "kiss kiss":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.kiss;
            case "confused":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.confused;
            case "breaking point":
                return "android.resource://" +context. getPackageName() + "/" + R.raw.breaking_point;
            case "wiggle":
                return "android.resource://" +context. getPackageName() + "/" + R.raw.cool_wave;
            case "brush your shoulders":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.brush_shoulder;
            case "true love":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.true_love;
            case "salute":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.salute;
            case "rock paper scissors":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.rock_paper;
            case "face palm":
                return "android.resource://" + context.getPackageName() + "/" + R.raw.facepalm;

            default:
                return "android.resource://" + context.getPackageName() + "/" + R.raw.confused;
        }
    }
}
