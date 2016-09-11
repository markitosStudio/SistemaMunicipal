package curso.android.sistemamunicipal.control;


import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import curso.android.sistemamunicipal.R;
import curso.android.sistemamunicipal.modelo.InformacionMunicipal;

public class Municipio extends AppCompatActivity {

    TextView txtTitulo;
    TextView txtMunicipio1;
    TextView txtMunicipio2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipio);

        txtTitulo=(TextView) findViewById(R.id.txtTitulo);
        txtMunicipio1=(TextView) findViewById(R.id.txtMunicipio1);
        txtMunicipio2=(TextView) findViewById(R.id.txtMunicipio2);
        parseXML();
    }

    private void parseXML() {
        List<InformacionMunicipal> informacionMunicipales=null;
        try {
            Resources res = this.getResources();
            XmlResourceParser xpp = res.getXml(R.xml.municipio);
            xpp.next();
            int evento=xpp.getEventType();
            InformacionMunicipal informacionActual=null;

            while (evento!= XmlPullParser.END_DOCUMENT){
                String etiqueta = null;
                switch (evento){
                    case XmlPullParser.START_DOCUMENT:
                        informacionMunicipales=new ArrayList<InformacionMunicipal>();
                        break;
                    case XmlPullParser.START_TAG:
                        etiqueta=xpp.getName();
                        if(etiqueta.equals("miMunicipio")){
                            informacionActual=new InformacionMunicipal();
                        }else if (informacionActual!= null){
                            if(etiqueta.equals("titulo")){
                                informacionActual.setTitulo(xpp.nextText());
                            }else if(etiqueta.equals("municipio1")){
                                informacionActual.setMunicipio1(xpp.nextText());
                            }else if(etiqueta.equals("municipio2")){
                                informacionActual.setMunicipio2(xpp.nextText());
                            }else if(etiqueta.equals("mision")){
                                informacionActual.setMision(xpp.nextText());
                            }else if(etiqueta.equals("vision")){
                                informacionActual.setVision(xpp.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        etiqueta=xpp.getName();
                        if(etiqueta.equals("miMunicipio")  && informacionActual != null){
                            informacionMunicipales.add(informacionActual);
                        }
                        break;
                }
                evento=xpp.next();
            }

        }catch (Exception e) {
        e.printStackTrace();
        }


        for ( int i=0;i<informacionMunicipales.size();i++){
            txtTitulo.setText(informacionMunicipales.get(i).getTitulo());
            txtMunicipio1.setText(informacionMunicipales.get(i).getMunicipio1());
            txtMunicipio2.setText(informacionMunicipales.get(i).getMunicipio2());
        }
    }
}
