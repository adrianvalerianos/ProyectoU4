package models;

public class Tarea {
    private int id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String color;

    public Tarea (String titulo, String descripcion, String fecha, String color) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.color = color;
    }

    public Tarea () {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
