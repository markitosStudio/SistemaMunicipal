package curso.android.sistemamunicipal.control;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.Timer;
import java.util.TimerTask;

import curso.android.sistemamunicipal.R;

public class MainActivity extends AppCompatActivity {


    private ImageSwitcher imageSwitcher;
    private int[] gallery = { R.drawable.santa_elena, R.drawable.fondo_iglesia, R.drawable.logo_negro};
    private int position;
    private static final Integer DURATION = 2500;
    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                return new ImageView(MainActivity.this);
            }
        });

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        imageSwitcher.setInAnimation(fadeIn);
        imageSwitcher.setOutAnimation(fadeOut);

        if (timer != null) {
            timer.cancel();
        }
        position = 0;
        startSlider();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        Bundle b= new Bundle();
        switch (item.getItemId()) {
            case R.id.opcMunicipio:
                i= new Intent(MainActivity.this, Municipio.class);
                startActivity(i);
                break;
            case R.id.opcNoticias:
                i= new Intent(MainActivity.this, Municipio.class);
                startActivity(i);
                break;
            case R.id.opcImpPredial:
                i= new Intent(MainActivity.this, ImpuestoPredial.class);
                startActivity(i);
                break;
            case R.id.opcPatMunicipal:
                i= new Intent(MainActivity.this, PatenteMunicipal.class);
                startActivity(i);
                break;
            case R.id.opcBanGuayaquil:
                i= new Intent(MainActivity.this, BancoPagos.class);
                b= new Bundle();
                b.putString("link","http://www.bancoguayaquil.com/responsive/index.asp");
                i.putExtras(b);
                startActivity(i);
                break;
            case R.id.opcBanPacifico:
                i= new Intent(MainActivity.this, BancoPagos.class);
                b= new Bundle();
                b.putString("link","https://www.google.com.ec");
                i.putExtras(b);
                startActivity(i);
                break;
            case R.id.opcBanPichinca:
                i= new Intent(MainActivity.this, BancoPagos.class);
                b= new Bundle();
                b.putString("link","https://www.facebook.com/");
                i.putExtras(b);
                startActivity(i);
                break;
            case R.id.opcPunRecaudacion:
                i= new Intent(MainActivity.this, PuntosRecaudacionActivity.class);
                startActivity(i);
                break;
            case R.id.opcTransparencia:
                i= new Intent(MainActivity.this, Municipio.class);
                startActivity(i);
                break;
            case R.id.opcSugerencia:
                i= new Intent(MainActivity.this, Municipio.class);
                startActivity(i);
                break;
            case R.id.opcQuejas:
                i= new Intent(MainActivity.this, Municipio.class);
                startActivity(i);
                break;
            case R.id.opcInfPublica:
                i= new Intent(MainActivity.this, Municipio.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // ////////////////////BUTTONS

  /*  public void start(View button) {
        if (timer != null) {
            timer.cancel();
        }
        position = 0;
        startSlider();
    }

    public void stop(View button) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }*/

    public void startSlider() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                // avoid exception:
                // "Only the original thread that created a view hierarchy can touch its views"
                runOnUiThread(new Runnable() {
                    public void run() {
                        imageSwitcher.setImageResource(gallery[position]);
                        position++;
                        if (position == gallery.length) {
                            position = 0;
                        }
                    }
                });
            }

        }, 0, DURATION);
    }

    // Stops the slider when the Activity is going into the background
    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            startSlider();
        }

    }
}
