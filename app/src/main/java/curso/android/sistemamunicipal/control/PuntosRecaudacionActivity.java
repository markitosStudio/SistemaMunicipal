package curso.android.sistemamunicipal.control;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import curso.android.sistemamunicipal.R;

/**
 * Created by markitos on 11/09/16.
 */
public class PuntosRecaudacionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker mCac,mTerminal,mBuenaventura;
    private static final LatLng P_CAC = new LatLng(-2.228851, -80.924450);
    private static final LatLng P_TERMINAL = new LatLng(-2.216482, -80.866746);
    private static final LatLng P_BUENAVENTURA = new LatLng(-2.224123, -80.910148);
    public static String LATITUD;
    public static String LONGITUD;

    public static final String CAC ="Centro de Atención Cuidadana";
    public static final String TT ="Terminal Terrestre Santa Slena";
    public static final String CC ="Centro Comercial Buenaventura M";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_recaudacion);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        agregarGeoPunto(googleMap,P_CAC,CAC,"2do. Piso Oficina 215");
        agregarGeoPunto(googleMap,P_TERMINAL,TT,"Oficina B-P, junto Aneta.");
        agregarGeoPunto(googleMap,P_BUENAVENTURA,CC,"Planta baja. Frente al acuario");

        googleMap.setOnMarkerClickListener(this);
    }

    /**
     * Permite agregar un marcador al mapa
     * @param pPunto
     * @param pTitulo
     * @param pDetalle
     */
    public static void agregarGeoPunto(GoogleMap mMap, LatLng pPunto, String pTitulo, String pDetalle ){
        float color=0;

        mMap.setMinZoomPreference(12.0f);
        if (pPunto.equals(P_CAC)){
            color=BitmapDescriptorFactory.HUE_YELLOW;
        }else if (pPunto.equals(P_BUENAVENTURA)){
            color=BitmapDescriptorFactory.HUE_BLUE;
        }else if (pPunto.equals(P_TERMINAL)){
            color=BitmapDescriptorFactory.HUE_RED;
        }
        mMap.addMarker(new MarkerOptions().position(pPunto)
                .title(pTitulo)
                .snippet(pDetalle)
                .icon(BitmapDescriptorFactory.defaultMarker(color))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pPunto));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int lugar=0;
        Intent intent = new Intent(this, PuntosRecaudacionDetalleActivity.class);
        Bundle b = new Bundle();
        b.putDouble(LATITUD, marker.getPosition().latitude);
        b.putDouble(LONGITUD, marker.getPosition().longitude);
        b.putString("Titulo",marker.getTitle());
        b.putString("Detalle",marker.getSnippet());
        if (marker.getTitle().equals(CAC)){
            lugar=1;
        }else if (marker.getTitle().equals(CC)){
            lugar=2;
        }else if (marker.getTitle().equals(TT)){
            lugar=3;
        }
        b.putInt("lugar",lugar);
        intent.putExtras(b);
        startActivity(intent);
        return false;
    }
}
