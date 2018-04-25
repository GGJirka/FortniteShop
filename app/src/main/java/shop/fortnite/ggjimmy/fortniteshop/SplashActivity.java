package shop.fortnite.ggjimmy.fortniteshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ggjimmy on 4/18/18.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, FortniteShop.class);

        startActivity(intent);
        finish();
    }
}
