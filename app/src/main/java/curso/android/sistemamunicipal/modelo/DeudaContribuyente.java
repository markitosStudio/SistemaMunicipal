package curso.android.sistemamunicipal.modelo;

/**
 * Created by David on 09/09/2016.
 */
public class DeudaContribuyente {

    private String tipo;
    private String cedula;
    private String contribuyente;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private String clavecatastral;
    private String detalletitulo;
    private double valortitulo;
    private String anio;
    private double interes;

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(String contribuyente) {
        this.contribuyente = contribuyente;
    }

    public String getClavecatastral() {
        return clavecatastral;
    }

    public void setClavecatastral(String clavecatastral) {
        this.clavecatastral = clavecatastral;
    }

    public String getDetalletitulo() {
        return detalletitulo;
    }

    public void setDetalletitulo(String detalletitulo) {
        this.detalletitulo = detalletitulo;
    }

    public double getValortitulo() {
        return valortitulo;
    }

    public void setValortitulo(double valortitulo) {
        this.valortitulo = valortitulo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }
}
