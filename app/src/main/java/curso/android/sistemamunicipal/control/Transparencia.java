package curso.android.sistemamunicipal.control;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import curso.android.sistemamunicipal.R;
import curso.android.sistemamunicipal.modelo.Directorio;
import curso.android.sistemamunicipal.modelo.Remuneracion;
import curso.android.sistemamunicipal.modelo.Servicio;

/**
 * Created by Geomayra Yagual on 11/09/2016.
 */
public class Transparencia extends AppCompatActivity {
    private static final String NOMBRE_CARPETA_APP="curso.android.pruebapdf";
    private static final String GENERADOS="misarchivos";

    protected Button BtnPdf;
    protected TextView txt4, txt5, txt6;
    List<Directorio> directorios = new ArrayList<Directorio>();
    List<Remuneracion> remuneraciones = new ArrayList<Remuneracion>();
    List<Servicio> servicios = new ArrayList<Servicio>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparencia);

        txt4 = (TextView) findViewById(R.id.txt4);
        txt5 = (TextView) findViewById(R.id.txt5);
        txt6 = (TextView) findViewById(R.id.txt6);

        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPdfOnClick(v);
            }
        });
        txt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPdfOnClick(v);
            }
        });
        txt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPdfOnClick(v);
            }
        });
    }

    public void generarPdfOnClick(View v) {
        Document documento= new Document(PageSize.LETTER);
        String nombreArchivo = null;
        switch (v.getId()) {
            case R.id.txt4:
                nombreArchivo = "directorio.pdf";
                break;
            case R.id.txt5:
                nombreArchivo = "remuneracion.pdf";
                break;
            case R.id.txt6:
                nombreArchivo = "servicio.pdf";
                break;
        }
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(tarjetaSD + File.separator + NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfSubir = new File(pdfDir.getPath() + File.separator + GENERADOS);
        if (!pdfSubir.exists()) {
            pdfSubir.mkdir();
        }

        String nombre_completo = Environment.getExternalStorageDirectory() + File.separator + NOMBRE_CARPETA_APP + File.separator + GENERADOS +
                File.separator + nombreArchivo;

        File outputfile= new File(nombre_completo);
        if(outputfile.exists()){
            outputfile.delete();
        }
        try {
            PdfWriter pdfWriter= PdfWriter.getInstance(documento,new FileOutputStream(nombre_completo));

            /*Creas el documento*/

            documento.open();
            /*documento.addAuthor("Geomayra Yagual");
            documento.addCreator("Kreator");
            documento.addSubject("Funciono");
            documento.addCreationDate();
            documento.addTitle("Funcionando");*/

            switch (v.getId()){
                case R.id.txt4:
                    directorios = null;
                    try {
                        Resources res = this.getResources();
                        XmlResourceParser xmlDirectorio = res.getXml(R.xml.directorio);
                        xmlDirectorio.next();
                        int evento = xmlDirectorio.getEventType();
                        Directorio dir = null;
                        while (evento!= XmlPullParser.END_DOCUMENT){
                            String etiqueta = null;
                            switch (evento){
                                case XmlPullParser.START_DOCUMENT:
                                    directorios = new ArrayList<Directorio>();
                                    break;
                                case XmlPullParser.START_TAG:
                                    etiqueta=xmlDirectorio.getName();
                                    if(etiqueta.equals("directorio")){
                                        dir = new Directorio();
                                    }else if (dir!= null){
                                        if(etiqueta.equals("numero")){
                                            dir.setNumero(new Integer(xmlDirectorio.nextText()));
                                        }
                                        if(etiqueta.equals("cargo")){
                                            dir.setCargo(xmlDirectorio.nextText());
                                        }
                                        if(etiqueta.equals("numero")){
                                            dir.setNombres(xmlDirectorio.nextText());
                                        }
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    etiqueta=xmlDirectorio.getName();
                                    if(etiqueta.equals("directorio")  && dir != null){
                                        directorios.add(dir);
                                    }
                                    break;
                            }
                            evento=xmlDirectorio.next();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case R.id.txt5:
                    remuneraciones = null;
                    try {
                        Resources res = this.getResources();
                        XmlResourceParser xmlRemuneracion = res.getXml(R.xml.remuneracion);
                        xmlRemuneracion.next();
                        int evento = xmlRemuneracion.getEventType();
                        Remuneracion remuneracion = null;
                        while (evento!= XmlPullParser.END_DOCUMENT){
                            String etiqueta = null;
                            switch (evento){
                                case XmlPullParser.START_DOCUMENT:
                                    remuneraciones = new ArrayList<Remuneracion>();
                                    break;
                                case XmlPullParser.START_TAG:
                                    etiqueta=xmlRemuneracion.getName();
                                    if(etiqueta.equals("remuneracion")){
                                        remuneracion = new Remuneracion();
                                    }else if (remuneracion!= null){
                                        if(etiqueta.equals("numero")){
                                            remuneracion.setNumero(new Integer(xmlRemuneracion.nextText()));
                                        }
                                        if(etiqueta.equals("nombre")){
                                            remuneracion.setNombre(xmlRemuneracion.nextText());
                                        }
                                        if(etiqueta.equals("cedula")){
                                            remuneracion.setCedula(xmlRemuneracion.nextText());
                                        }
                                        if(etiqueta.equals("cargo")){
                                            remuneracion.setCargo(xmlRemuneracion.nextText());
                                        }
                                        if(etiqueta.equals("cedula")){
                                            remuneracion.setSueldoMensual(new Double(xmlRemuneracion.nextText().toString()));
                                        }
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    etiqueta=xmlRemuneracion.getName();
                                    if(etiqueta.equals("remuneracion")  && remuneracion != null){
                                        remuneraciones.add(remuneracion);
                                    }
                                    break;
                            }
                            evento=xmlRemuneracion.next();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case R.id.txt6:
                    servicios = null;
                    try {
                        Resources res = this.getResources();
                        XmlResourceParser xmlServicio = res.getXml(R.xml.servicios);
                        xmlServicio.next();
                        int evento = xmlServicio.getEventType();
                        Servicio servicio = null;
                        while (evento!= XmlPullParser.END_DOCUMENT){
                            String etiqueta = null;
                            switch (evento){
                                case XmlPullParser.START_DOCUMENT:
                                    servicios = new ArrayList<Servicio>();
                                    break;
                                case XmlPullParser.START_TAG:
                                    etiqueta=xmlServicio.getName();
                                    if(etiqueta.equals("servicio")){
                                        servicio = new Servicio();
                                    }else if (servicio!= null){
                                        if(etiqueta.equals("descripcion")){
                                            servicio.setDescripcion(xmlServicio.nextText());
                                        }
                                        if(etiqueta.equals("descripcion1")){
                                            servicio.setDescripcion1(xmlServicio.nextText());
                                        }
                                        if(etiqueta.equals("horario")){
                                            servicio.setHorario(xmlServicio.nextText());
                                        }
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    etiqueta=xmlServicio.getName();
                                    if(etiqueta.equals("servicio")  && servicio != null){
                                        servicios.add(servicio);
                                    }
                                    break;
                            }
                            evento=xmlServicio.next();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }


            String titulo = null;
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            switch (v.getId()) {
                case R.id.txt4:
                    titulo = "Directorio";
                    break;
                case R.id.txt5:
                    titulo = "Remuneraciones";
                    break;
                case R.id.txt6:
                    titulo = "Servicio";
                    break;
            }
            String htmlPdf="<html><head></head><body><h1>"+titulo+"</h1>";
            for (Directorio lista : directorios){
                htmlPdf = htmlPdf + "<p>"+lista.getNumero()+"\t"+lista.getCargo()+"\t"+lista.getNombres()+"</p>";
            }
            for (Remuneracion lista: remuneraciones){
                htmlPdf = htmlPdf + "<p>"+lista.getNumero()+"\t"+lista.getCedula()+"\t"+lista.getNombre()+lista.getCargo()+"\t"+lista.getSueldoMensual()+"</p>";
            }
            for (Servicio lista: servicios){
                htmlPdf = htmlPdf + "<p>"+lista.getDescripcion()+"</p>";
                htmlPdf = htmlPdf + "<p>"+lista.getDescripcion1()+"</p>";
                htmlPdf = htmlPdf + "<p>"+lista.getHorario()+"</p>";
            }

            htmlPdf = htmlPdf + "</body></html>";
            try {
                worker.parseXHtml(pdfWriter,documento,new StringReader(htmlPdf));
                documento.close();
                Toast.makeText(this,"PDf generado",Toast.LENGTH_LONG).show();
                muestaPdf(nombre_completo,this);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void muestaPdf(String archivo, Context context){
        Toast.makeText(this,"Leyendo Archivo",Toast.LENGTH_LONG).show();
        File file = new File(archivo);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(file),"application/pdf");
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("MainActivity", Uri.fromFile(file)+"application/pdf");
        try {
            context.startActivity(i);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this,"No tiene una app para abir este tipo de datos",Toast.LENGTH_LONG).show();
        }
    }
}
