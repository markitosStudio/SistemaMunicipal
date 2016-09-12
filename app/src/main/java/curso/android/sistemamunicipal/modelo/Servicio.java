package curso.android.sistemamunicipal.modelo;

/**
 * Created by Geomayra Yagual on 11/09/2016.
 */
public class Servicio {
    private String descripcion;
    private String descripcion1;
    private String horario;

    public Servicio(String descripcion, String descripcion1, String horario) {
        this.descripcion = descripcion;
        this.descripcion1 = descripcion1;
        this.horario = horario;
    }

    public Servicio() {
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion1() {
        return descripcion1;
    }

    public void setDescripcion1(String descripcion1) {
        this.descripcion1 = descripcion1;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
