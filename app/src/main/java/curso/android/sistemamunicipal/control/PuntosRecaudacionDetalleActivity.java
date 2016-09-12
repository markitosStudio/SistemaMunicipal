package curso.android.sistemamunicipal.control;


import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;

import curso.android.sistemamunicipal.R;

import curso.android.sistemamunicipal.personalizado.BitmapUtils;
import curso.android.sistemamunicipal.personalizado.GalleryAdapter;
import curso.android.sistemamunicipal.personalizado.GridViewImageAdapter;

/**
 * Created by markitos on 11/09/16.
 */
public class PuntosRecaudacionDetalleActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnTouchListener {

    protected TextView lblPunto;
    protected TabHost tabs;
    protected ImageView imagenSeleccionada;
    protected GridView gridGaleria;
    protected Gallery gallery;


    private GoogleMap mMap;
    LatLng geoPunto;
    private String vTitulo, vDetalle;
    private int vLugar;
    private ArrayList<Integer> listPhoto = new ArrayList<Integer>();


    private static final String TAG = "Touch";

    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;


    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();


    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;


    PointF  start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punto_recaudacion_detalle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lblPunto = (TextView)findViewById(R.id.lblPunto);
        tabs = (TabHost) findViewById(R.id.tabHost);
        imagenSeleccionada = (ImageView) findViewById(R.id.imgSeleccionada);
        imagenSeleccionada.setOnTouchListener(this);


        tabs.setup();

        TabHost.TabSpec spec1 = tabs.newTabSpec("mapa");
        spec1.setContent(R.id.tabMapa);
        spec1.setIndicator("MAPA");
        tabs.addTab(spec1);

        TabHost.TabSpec spec2 = tabs.newTabSpec("galeria");
        spec2.setContent(R.id.tabImagen);
        spec2.setIndicator("GALERIA");
        tabs.addTab(spec2);
        tabs.setCurrentTab(0);

        Bundle b = this.getIntent().getExtras();
        geoPunto = new LatLng(b.getDouble((PuntosRecaudacionActivity.LATITUD)),
                b.getDouble((PuntosRecaudacionActivity.LONGITUD)));
        vTitulo=b.getString("Titulo");
        vDetalle=b.getString("Detalle");
        vLugar=b.getInt("lugar");
        lblPunto.setText(vTitulo);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDetalle);
        mapFragment.getMapAsync(this);


        switch (vLugar){
            case 1:
                listPhoto.add(R.drawable.cac1);
                listPhoto.add(R.drawable.cac2);
                listPhoto.add(R.drawable.cac3);
                listPhoto.add(R.drawable.cac4);
                imagenSeleccionada.setImageResource(R.drawable.cac1);
                break;
            case 2:
                listPhoto.add(R.drawable.cc1);
                listPhoto.add(R.drawable.cc2);
                listPhoto.add(R.drawable.cc3);
                listPhoto.add(R.drawable.cc4);
                imagenSeleccionada.setImageResource(R.drawable.cc1);
                break;
            case 3:
                listPhoto.add(R.drawable.tt1);
                listPhoto.add(R.drawable.tt2);
                listPhoto.add(R.drawable.tt3);
                listPhoto.add(R.drawable.tt4);
                imagenSeleccionada.setImageResource(R.drawable.tt1);
                break;
        }


        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new GalleryAdapter(this, listPhoto));

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imagenSeleccionada.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), listPhoto.get(position), 300, 0));
            }
        });


        /*gridGaleria = (GridView) findViewById( R.id.gridGaleria );
        gridGaleria.setAdapter( new GridViewImageAdapter(this,listPhoto) );


       gridGaleria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView parent, View v, int position, long id) {
               imagenSeleccionada.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), listPhoto.get(position), 300, 0));
           }
       });*/


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        PuntosRecaudacionActivity.agregarGeoPunto(googleMap,geoPunto,vTitulo,vDetalle);
        googleMap.setMaxZoomPreference(10.0f);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);


        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_POINTER_UP:

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist;

                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);

        return true;

    }

    private float spacing(MotionEvent event)
    {
        double x = event.getX(0) - event.getX(1);
        double y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((x*x+y*y));
    }



    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


    private void dumpEvent(MotionEvent event)
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }
}
