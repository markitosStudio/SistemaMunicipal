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
import curso.android.sistemamunicipal.modelo.Queja;

/**
 * Created by Propietario on 11/09/2016.
 */
public class FormularioQueja extends AppCompatActivity implements View.OnClickListener{

    private Button btnEnviar;
    private EditText txtNombre, txtCedula, txtTelefono, txtCelular, txtCorreo, txtDescripcion, txtComentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queja);

        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtCedula = (EditText) findViewById(R.id.txtCedula);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtCelular = (EditText) findViewById(R.id.txtCelular);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        txtComentario = (EditText) findViewById(R.id.txtComentario);

        //Adding click listener
        btnEnviar.setOnClickListener(this);

    }

    private void enviarCorreo() {
        //Contenido del correo
        String email = txtCorreo.getText().toString().trim();
        String subject = "Confirmacion de recepcion de Quejas";
        String message = "Estimado(a) " + txtNombre.getText().toString().trim() + ",\n\n" +
                "Valoramos mucho su opinión. Gracias por ayudarnos a mejorar.\n\n\n" +
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
        txtComentario.setText(null);
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
                Toast.makeText(getApplicationContext(), "Ingrese la descripción.", Toast.LENGTH_LONG).show();
                return;
            }

            File directorio = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sistemaMunicipal");
            if (!directorio.exists()) {
                directorio.mkdir();
            }

            File xmlFile = new File(directorio + "/queja.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = null;
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = null;

            if(!xmlFile.exists()){
                xmlFile.createNewFile();
                document = documentBuilder.newDocument();
                Element rootElement = document.createElement("quejas");
                document.appendChild(rootElement);
            }else{
                document = documentBuilder.parse(xmlFile);
            }
            Element root = document.getDocumentElement();

            List<Queja> listaQuejas = new ArrayList<Queja>();

            Queja nuevoRegistro = new Queja();
            nuevoRegistro.setFecha(new Date());
            nuevoRegistro.setNombre(txtNombre.getText().toString().trim());
            nuevoRegistro.setCedula(txtCedula.getText().toString().trim());
            nuevoRegistro.setTelefono(txtTelefono.getText().toString().trim());
            nuevoRegistro.setCelular(txtCelular.getText().toString().trim());
            nuevoRegistro.setCorreo(txtCorreo.getText().toString().trim());
            nuevoRegistro.setDescripcion(txtDescripcion.getText().toString().trim());
            nuevoRegistro.setComentario(txtComentario.getText().toString().trim());

            listaQuejas.add(nuevoRegistro);

            for (Queja registro : listaQuejas) {
                // Nueva queja
                Element nuevaQueja = document.createElement("queja");

                Element fecha = document.createElement("fecha");
                fecha.appendChild(document.createTextNode(nuevoRegistro.getFecha().toString()));
                nuevaQueja.appendChild(fecha);

                Element nombre = document.createElement("nombre");
                nombre.appendChild(document.createTextNode(registro.getNombre()));
                nuevaQueja.appendChild(nombre);

                Element cedula = document.createElement("cedula");
                cedula.appendChild(document.createTextNode(registro.getCedula()));
                nuevaQueja.appendChild(cedula);

                if(!(registro.getTelefono()==null || registro.getTelefono().trim().toString().equals("")) ){
                    Element telefono = document.createElement("telefono");
                    telefono.appendChild(document.createTextNode(registro.getTelefono()));
                    nuevaQueja.appendChild(telefono);
                }

                if(!(registro.getCelular()==null || registro.getCelular().trim().toString().equals("")) ){
                    Element celular = document.createElement("celular");
                    celular.appendChild(document.createTextNode(registro.getCelular()));
                    nuevaQueja.appendChild(celular);
                }

                Element correo = document.createElement("correo");
                correo.appendChild(document.createTextNode(registro.getCorreo()));
                nuevaQueja.appendChild(correo);

                Element descripcion = document.createElement("descripcion");
                descripcion.appendChild(document.createTextNode(registro.getDescripcion()));
                nuevaQueja.appendChild(descripcion);

                if(!(registro.getComentario()==null || registro.getComentario().trim().toString().equals("")) ) {
                    Element comentario = document.createElement("comentario");
                    comentario.appendChild(document.createTextNode(registro.getComentario()));
                    nuevaQueja.appendChild(comentario);
                }

                root.appendChild(nuevaQueja);
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
