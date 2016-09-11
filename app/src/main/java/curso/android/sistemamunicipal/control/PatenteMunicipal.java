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

public class PatenteMunicipal extends AppCompatActivity implements View.OnClickListener {

    EditText txtCedulaP;
    Button btnBuscarP,btnLimpiarP;
    TableLayout tablaP;

    String info[][];

    ProgressDialog dialog;
    private MiTareaAsincronaDialog tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patente_municipal);

        txtCedulaP = (EditText) findViewById(R.id.txtCedulaP);
        btnBuscarP = (Button) findViewById(R.id.btnBuscarP);
        btnLimpiarP = (Button) findViewById(R.id.btnLimpiarP);
        tablaP=(TableLayout) findViewById(R.id.tablaP);


        btnBuscarP.setOnClickListener(this);
        btnLimpiarP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Recupera el identificador del boton
        switch (v.getId()){
            case R.id.btnBuscarP:
                ConsultaImpuesto();
                break;
            case R.id.btnLimpiarP:
                limpiar();
                break;
        }
    }

    public void limpiar (){
        tablaP.removeAllViews();
        txtCedulaP.setText(null);
    }

    public void ConsultaImpuesto(){

        tablaP.removeAllViews();
        if(txtCedulaP.length()<=0){
                Toast.makeText(PatenteMunicipal.this,"No se a ingresado un número de cédula válido", Toast.LENGTH_SHORT).show();
                return;
        }
        dialog = new ProgressDialog(PatenteMunicipal.this);
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
            tablaP.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT));
            tablaP.setColumnStretchable(1, true);

            // Cabecera de la tabla
            TableRow cabecera = new TableRow(PatenteMunicipal.this);
            cabecera.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tablaP.addView(cabecera);

            // Textos de la cabecera
            for (int i = 0; i < 3; i++)
            {
                TextView columna = new TextView(PatenteMunicipal.this);
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
            TableRow separador_cabecera = new TableRow(PatenteMunicipal.this);
            separador_cabecera.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            FrameLayout linea_cabecera = new FrameLayout(PatenteMunicipal.this);
            TableRow.LayoutParams linea_cabecera_params =
                    new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2);
            linea_cabecera_params.span = 6;
            linea_cabecera.setBackgroundColor(Color.parseColor("#000000"));
            separador_cabecera.addView(linea_cabecera, linea_cabecera_params);
            tablaP.addView(separador_cabecera);
            double total=0;
            DecimalFormat df = new DecimalFormat("####0.00");
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).getCedula().equals(txtCedulaP.getText().toString()) && result.get(i).getTipo().equals("PM")  ) {
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
                        tablaP.addView(tablaFila1);
                    }
                }
            // Linea que separa la cabecera de los datos
            TableRow separador_totales= new TableRow(PatenteMunicipal.this);
            separador_totales.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            FrameLayout linea_cabecera1 = new FrameLayout(PatenteMunicipal.this);
            TableRow.LayoutParams linea_cabecera_params1 =
                    new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 2);
            linea_cabecera_params1.span = 6;
            linea_cabecera1.setBackgroundColor(Color.parseColor("#000000"));
            separador_totales.addView(linea_cabecera1, linea_cabecera_params1);
            tablaP.addView(separador_totales);


            // Fila de totales
            TableRow totales = new TableRow(PatenteMunicipal.this);
            totales.setLayoutParams(new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView texto_total = new TextView(PatenteMunicipal.this);
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


            TextView columna = new TextView(PatenteMunicipal.this);
            columna.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            columna.setText(String.valueOf(df.format(total)));
            columna.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            columna.setGravity(Gravity.CENTER);
            totales.addView(columna);
            dialog.dismiss();
            tablaP.addView(totales);
        }
    }
}
