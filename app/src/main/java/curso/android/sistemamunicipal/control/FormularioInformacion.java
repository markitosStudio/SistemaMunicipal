package curso.android.sistemamunicipal.control;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import curso.android.sistemamunicipal.R;
import curso.android.sistemamunicipal.modelo.Informacion;
import curso.android.sistemamunicipal.modelo.Queja;

/**
 * Created by Propietario on 11/09/2016.
 */
public class FormularioInformacion extends AppCompatActivity implements View.OnClickListener{

    private Button btnEnviar;
    private EditText txtNombre, txtCedula, txtTelefono, txtCelular, txtCorreo, txtDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtCedula = (EditText) findViewById(R.id.txtCedula);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtCelular = (EditText) findViewById(R.id.txtCelular);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);

        //Adding click listener
        btnEnviar.setOnClickListener(this);

    }

    private void enviarCorreo() {
        //Contenido del correo
        String email = txtCorreo.getText().toString().trim();
        String subject = "Confirmacion de recepcion de solicitud de informacion publica";
        String message = "Estimado(a) " + txtNombre.getText().toString().trim() + ",\n\n" +
                "Hemos recibido su solicitud.  Dentro de las próximas horas atenderemos su petición.\n\n\n" +
                "Administrador del Sistema Municipal";

        //Creating SendMail object
        EnvioCorreo sm = new EnvioCorreo(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();

        limpiaObjetos();
    }

    private void limpiaObjetos() {
        txtNombre.setText(null);
        txtCedula.setText(null);
        txtTelefono.setText(null);
        txtCelular.setText(null);
        txtCorreo.setText(null);
        txtDescripcion.setText(null);
    }

    @Override
    public void onClick(View v) {
        try {
            //Validaciones del formulario
            if (txtNombre.getText() == null || txtNombre.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Ingrese el nombre.", Toast.LENGTH_LONG).show();
                return;
            }
            if (txtCedula.getText() == null || txtCedula.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Ingrese el cédula.", Toast.LENGTH_LONG).show();
                return;
            }else{
                if(txtCedula.getText().toString().trim().length()<10){
                    Toast.makeText(getApplicationContext(), "Ingrese una cédula válida.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if (txtCorreo.getText() == null || txtCorreo.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Ingrese el correo electrónico.", Toast.LENGTH_LONG).show();
                return;
            }
            if (txtDescripcion.getText() == null || txtDescripcion.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Ingrese la petición.", Toast.LENGTH_LONG).show();
                return;
            }

            File directorio = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sistemaMunicipal");
            if (!directorio.exists()) {
                directorio.mkdir();
            }

            File xmlFile = new File(directorio + "/solicitudInformacion.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = null;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = null;

            if(!xmlFile.exists()){
                xmlFile.createNewFile();
                document = documentBuilder.newDocument();
                Element rootElement = document.createElement("solicitudes");
                document.appendChild(rootElement);
            }else{
                document = documentBuilder.parse(xmlFile);
            }
            Element root = document.getDocumentElement();

            List<Informacion> listaInformacion = new ArrayList<Informacion>();

            Informacion nuevoRegistro = new Informacion();
            nuevoRegistro.setFecha(new Date());
            nuevoRegistro.setNombre(txtNombre.getText().toString().trim());
            nuevoRegistro.setCedula(txtCedula.getText().toString().trim());
            nuevoRegistro.setTelefono(txtTelefono.getText().toString().trim());
            nuevoRegistro.setCelular(txtCelular.getText().toString().trim());
            nuevoRegistro.setCorreo(txtCorreo.getText().toString().trim());
            nuevoRegistro.setDescripcion(txtDescripcion.getText().toString().trim());

            listaInformacion.add(nuevoRegistro);

            for (Informacion registro : listaInformacion) {
                // Nuev registro
                Element nuevaRegistro = document.createElement("solicitud");

                Element fecha = document.createElement("fecha");
                fecha.appendChild(document.createTextNode(nuevoRegistro.getFecha().toString()));
                nuevaRegistro.appendChild(fecha);

                Element nombre = document.createElement("nombre");
                nombre.appendChild(document.createTextNode(registro.getNombre()));
                nuevaRegistro.appendChild(nombre);

                Element cedula = document.createElement("cedula");
                cedula.appendChild(document.createTextNode(registro.getCedula()));
                nuevaRegistro.appendChild(cedula);

                if(!(registro.getTelefono()==null || registro.getTelefono().trim().toString().equals("")) ){
                    Element telefono = document.createElement("telefono");
                    telefono.appendChild(document.createTextNode(registro.getTelefono()));
                    nuevaRegistro.appendChild(telefono);
                }

                if(!(registro.getCelular()==null || registro.getCelular().trim().toString().equals("")) ){
                    Element celular = document.createElement("celular");
                    celular.appendChild(document.createTextNode(registro.getCelular()));
                    nuevaRegistro.appendChild(celular);
                }

                Element correo = document.createElement("correo");
                correo.appendChild(document.createTextNode(registro.getCorreo()));
                nuevaRegistro.appendChild(correo);

                Element descripcion = document.createElement("descripcion");
                descripcion.appendChild(document.createTextNode(registro.getDescripcion()));
                nuevaRegistro.appendChild(descripcion);

                root.appendChild(nuevaRegistro);
            }

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (TransformerException e1) {
            e1.printStackTrace();
        }

        enviarCorreo();
    }

}
