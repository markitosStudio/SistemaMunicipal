package curso.android.sistemamunicipal.modelo;

/**
 * Created by Geomayra Yagual on 11/09/2016.
 */
public class Remuneracion {
    private Integer numero;
    private String nombre;
    private String cedula;
    private String cargo;
    private Double sueldoMensual;

    public Remuneracion(Integer numero, String nombre, String cedula, String cargo, Double sueldoMensual) {
        this.numero = numero;
        this.nombre = nombre;
        this.cedula = cedula;
        this.cargo = cargo;
        this.sueldoMensual = sueldoMensual;
    }

    public Remuneracion() {
    }


    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Double getSueldoMensual() {
        return sueldoMensual;
    }

    public void setSueldoMensual(Double sueldoMensual) {
        this.sueldoMensual = sueldoMensual;
    }
}
