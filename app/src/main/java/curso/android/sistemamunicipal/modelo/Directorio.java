package curso.android.sistemamunicipal.modelo;

/**
 * Created by Geomayra Yagual on 10/09/2016.
 */
public class Directorio {

    private Integer numero;
    private String cargo;
    private String nombres;


    public Directorio(Integer numero, String cargo, String nombres) {
        this.numero = numero;
        this.cargo = cargo;
        this.nombres = nombres;
    }

    public Directorio() {

    }


    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}
