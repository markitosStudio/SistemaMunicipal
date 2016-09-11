package curso.android.sistemamunicipal.control;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import curso.android.sistemamunicipal.R;

/**
 * Clase que carga imagenes
 */
public class Bienvenida extends Activity {

    protected boolean active = true;
    protected int splashTime = 2000;

    /**
     * Método que se ejecuta al iniciar la actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bienvenida);
        Thread splashThread = new Thread(){
            @Override
            public void run(){
                try{
                    int waited = 0;
                    while(active && (waited < splashTime)){
                        sleep(100);
                        if(active){
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e){

                } finally{
                    openApp();
                }

            }
        };
        splashThread.start();
    }

    /**
     * Clase que inicia la actividad
     */
    private void openApp(){
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }
}
