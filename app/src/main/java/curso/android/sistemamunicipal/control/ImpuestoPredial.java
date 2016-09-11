package curso.android.sistemamunicipal.control;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import curso.android.sistemamunicipal.R;
import curso.android.sistemamunicipal.modelo.DeudaContribuyente;

public class ImpuestoPredial extends AppCompatActivity implements View.OnClickListener {

    RadioButton rdCedula;
    RadioButton rdClaveCatastral;
    LinearLayout linerClave;
    EditText txtCedula;
    TextView lbl1, lbl2, lbl3, lbl4, lbl5, lblTexto;
    EditText txt1, txt2, txt3, txt4, txt5, txt6;
    Button btnBuscar;
    TableLayout tabla;

    String info[][];

    ProgressDialog dialog;
    private MiTareaAsincronaDialog tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impuesto_predial);

        rdCedula = (RadioButton) findViewById(R.id.rdCedula);
        rdClaveCatastral = (RadioButton) findViewById(R.id.rdClaveCatastral);
        linerClave = (LinearLayout) findViewById(R.id.linerClave);
        txtCedula = (EditText) findViewById(R.id.txtCedula);
        lblTexto = (TextView) findViewById(R.id.lblTexto);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        tabla=(TableLayout) findViewById(R.id.tabla);

        lbl1 = (TextView) findViewById(R.id.lbl1);
        lbl2 = (TextView) findViewById(R.id.lbl2);
        lbl3 = (TextView) findViewById(R.id.lbl3);
        lbl4 = (TextView) findViewById(R.id.lbl4);
        lbl5 = (TextView) findViewById(R.id.lbl5);

        txt1 = (EditText) findViewById(R.id.txt1);
        txt2 = (EditText) findViewById(R.id.txt2);
        txt3 = (EditText) findViewById(R.id.txt3);
        txt4 = (EditText) findViewById(R.id.txt4);
        txt5 = (EditText) findViewById(R.id.txt5);
        txt6 = (EditText) findViewById(R.id.txt6);


        //chequea la busqueda por cedula primero
        rdCedula.setChecked(true);
        txtCedula.setVisibility(View.VISIBLE);
        linerClave.setVisibility(View.INVISIBLE);

        rdCedula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdCedula.isChecked()) {
                    limpiar(true);
                    lblTexto.setText("Cedula/Ruc");
                    linerClave.setVisibility(View.INVISIBLE);
                    txtCedula.setVisibility(View.VISIBLE);
                }
            }
        });

        rdClaveCatastral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdClaveCatastral.isChecked()) {
                    limpiar(false);
                    lblTexto.setText("Clave Catastro");
                    linerClave.setVisibility(View.VISIBLE);
                    txtCedula.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnBuscar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Recupera el identificador del boton
        switch (v.getId()){
            case R.id.btnBuscar:
                ConsultaImpuesto();
                break;
        }

    }


    public void limpiar (boolean cajaCedula){
        tabla.removeAllViews();
        if (cajaCedula){
            txtCedula.setText(null);
        }else{
            txt1.setText(null);
            txt2.setText(null);
            txt3.setText(null);
            txt4.setText(null);
            txt5.setText(null);
            txt6.setText(null);
        }
    }

    public void ConsultaImpuesto(){

        tabla.removeAllViews();
        if(rdCedula.isChecked()){
            if(txtCedula.length()<=0){
                Toast.makeText(ImpuestoPredial.this,"No se a ingresado un número de cédula válido", Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            if(txt1.length()<=0 || txt2.length()<=0 || txt3.length()<=0 || txt4.length()<=0 || txt5.length()<=0 || txt6.length()<=0){
                Toast.makeText(ImpuestoPredial.this,"No se a ingresado un número de cédula válido", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        dialog = new ProgressDialog(ImpuestoPredial.this);
        tarea= new MiTareaAsincronaDialog();
        tarea.execute();
    }


    public List<DeudaContribuyente> buscaDeudaFiltro(){
        List<DeudaContribuyente> Deudas=null;
        try {
            Resources res = this.getResources();
            XmlResourceParser xpp = res.getXml(R.xml.prediourbano);
            xpp.next();
            int evento=xpp.getEventType();
            DeudaContribuyente deudaContribuyente=null;

            while (evento!= XmlPullParser.END_DOCUMENT){
                String etiqueta = null;
                switch (evento){
                    case XmlPullParser.START_DOCUMENT:
                        Deudas=new ArrayList<DeudaContribuyente>();
                        break;
                    case XmlPullParser.START_TAG:
                        etiqueta=xpp.getName();
                        if(etiqueta.equals("deudapredio")){
                            deudaContribuyente=new DeudaContribuyente();
                        }else if (deudaContribuyente!= null){
                            if(etiqueta.equals("cedula")){
                                deudaContribuyente.setCedula(xpp.nextText());
                            }else if(etiqueta.equals("tipo")) {
                                deudaContribuyente.setTipo(xpp.nextText());
                            }else if(etiqueta.equals("contribuyente")) {
                                deudaContribuyente.setContribuyente(xpp.nextText());
                            }else if(etiqueta.equals("clavecatastral")){
                                deudaContribuyente.setClavecatastral(xpp.nextText());
                            }else if(etiqueta.equals("detalletitulo")){
                                deudaContribuyente.setDetalletitulo(xpp.nextText());
                            }else if(etiqueta.equals("valortitulo")){
                                deudaContribuyente.setValortitulo(Double.parseDouble(xpp.nextText()));
                            }else if(etiqueta.equals("anio")){
                                deudaContribuyente.setAnio(xpp.nextText());
                            }else if(etiqueta.equals("interes")){
                                deudaContribuyente.setInteres(Double.parseDouble(xpp.nextText()));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        etiqueta=xpp.getName();
                        if(etiqueta.equals("deudapredio")  && deudaContribuyente != null){
                            Deudas.add(deudaContribuyente);
                        }
                        break;
                }
                evento=xpp.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Deudas;

    }




    //Tarea Asincronona para sacar los puestos del ranking.
    private class MiTareaAsincronaDialog extends AsyncTask<Void, Integer, List<DeudaContribuyente>> {
        @Override
        protected List<DeudaContribuyente> doInBackground(Void... params) {
            return buscaDeudaFiltro();
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Procesando...");
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(List<DeudaContribuyente> result) {

            String cabeceras[] = { "Año", "Descripción", "Subtotal" };
            tabla.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            tabla.setColumnStretchable(1, true);

            // Cabecera de la tabla
            TableRow cabecera = new TableRow(ImpuestoPredial.this);
            cabecera.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tabla.addView(cabecera);

            // Textos de la cabecera
            for (int i = 0; i < 3; i++)
            {
                TextView columna = new TextView(ImpuestoPredial.this);
                columna.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                columna.setText(cabeceras[i]);
                columna.setTextColor(Color.parseColor("#005500"));
                columna.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                columna.setGravity(Gravity.CENTER_HORIZONTAL);
                columna.setPadding(5, 5, 5, 5);
                cabecera.addView(columna);
            }

            // Linea que separa la cabecera de los datos
            TableRow separador_cabecera = new TableRow(ImpuestoPredial.this);
            separador_cabecera.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            FrameLayout linea_cabecera = new FrameLayout(ImpuestoPredial.this);
            TableRow.LayoutParams linea_cabecera_params =
                    new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2);
            linea_cabecera_params.span = 6;
            linea_cabecera.setBackgroundColor(Color.parseColor("#000000"));
            separador_cabecera.addView(linea_cabecera, linea_cabecera_params);
            tabla.addView(separador_cabecera);
            double total=0;
            DecimalFormat df = new DecimalFormat("####0.00");
            if(rdCedula.isChecked()) {
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).getCedula().equals(txtCedula.getText().toString()) && result.get(i).getTipo().equals("PU")  ) {
                        TableRow tablaFila1 = new TableRow(getBaseContext());
                        TableLayout.LayoutParams param = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.MATCH_PARENT);

                        TextView colP1 = new TextView(getBaseContext());
                        TextView colP2 = new TextView(getBaseContext());
                        TextView colP3 = new TextView(getBaseContext());


                        colP1.setText((result.get(i).getContribuyente().length() > 19) ? result.get(i).getContribuyente().substring(0, 19) : result.get(i).getContribuyente());
                        colP1.setTextColor(Color.parseColor("#000000"));
                        colP1.setTextSize(14);

                        colP2.setText(result.get(i).getAnio());
                        colP2.setTextColor(Color.parseColor("#000000"));
                        colP2.setTextSize(14);


                        total = total + (result.get(i).getValortitulo() + result.get(i).getInteres());
                        colP3.setText(df.format(result.get(i).getValortitulo() + result.get(i).getInteres()));
                        colP3.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                        colP3.setTextColor(Color.parseColor("#000000"));
                        colP3.setTextSize(14);


                        tablaFila1.setLayoutParams(param);
                        tablaFila1.addView(colP2);
                        tablaFila1.addView(colP1);
                        tablaFila1.addView(colP3);
                        tabla.addView(tablaFila1);
                    }
                }
            }else{
                String claveCatastral=txt1.getText().toString()+"-"+txt2.getText().toString()+"-"+txt3.getText().toString()+"-"+txt4.getText().toString()+"-"+txt5.getText().toString()+"-"+txt6.getText().toString();

                for (int i = 0; i < result.size(); i++) {
                    String clave=result.get(i).getClavecatastral().replace("\n","");

                    if (clave.toString().trim().equals(claveCatastral.toString()) &&  result.get(i).getTipo().equals("PU")) {
                        TableRow tablaFila1 = new TableRow(getBaseContext());
                        TableLayout.LayoutParams param = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.MATCH_PARENT);

                        TextView colP1 = new TextView(getBaseContext());
                        TextView colP2 = new TextView(getBaseContext());
                        TextView colP3 = new TextView(getBaseContext());


                        colP1.setText((result.get(i).getContribuyente().length() > 19) ? result.get(i).getContribuyente().substring(0, 19) : result.get(i).getContribuyente());
                        colP1.setTextColor(Color.parseColor("#000000"));
                        colP1.setTextSize(14);

                        colP2.setText(result.get(i).getAnio());
                        colP2.setTextColor(Color.parseColor("#000000"));
                        colP2.setTextSize(14);


                        total = total + (result.get(i).getValortitulo() + result.get(i).getInteres());
                        colP3.setText(df.format(result.get(i).getValortitulo() + result.get(i).getInteres()));
                        colP3.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                        colP3.setTextColor(Color.parseColor("#000000"));
                        colP3.setTextSize(14);


                        tablaFila1.setLayoutParams(param);
                        tablaFila1.addView(colP2);
                        tablaFila1.addView(colP1);
                        tablaFila1.addView(colP3);
                        tabla.addView(tablaFila1);
                    }
                }
            }
            // Linea que separa la cabecera de los datos
            TableRow separador_totales= new TableRow(ImpuestoPredial.this);
            separador_totales.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            FrameLayout linea_cabecera1 = new FrameLayout(ImpuestoPredial.this);
            TableRow.LayoutParams linea_cabecera_params1 =
                    new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2);
            linea_cabecera_params1.span = 6;
            linea_cabecera1.setBackgroundColor(Color.parseColor("#000000"));
            separador_totales.addView(linea_cabecera1, linea_cabecera_params1);
            tabla.addView(separador_totales);


            // Fila de totales
            TableRow totales = new TableRow(ImpuestoPredial.this);
            totales.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView texto_total = new TextView(ImpuestoPredial.this);
            TableRow.LayoutParams texto_total_params =
                    new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT);
            texto_total_params.span = 2;
            texto_total.setText("Deuda total a cancelar por el usuario: ");
            texto_total.setTextColor(Color.parseColor("#005500"));
            texto_total.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            texto_total.setGravity(Gravity.RIGHT);
            totales.addView(texto_total, texto_total_params);


            TextView columna = new TextView(ImpuestoPredial.this);
            columna.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            columna.setText(String.valueOf(df.format(total)));
            columna.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            columna.setGravity(Gravity.CENTER);
            totales.addView(columna);
            dialog.dismiss();
            tabla.addView(totales);
        }

    }
}
