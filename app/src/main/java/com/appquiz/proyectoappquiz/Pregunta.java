package com.appquiz.proyectoappquiz;

public class Pregunta {

    private int id;
    private String enunciado;
    private String categoria;
    private String correcto;
    private String incorrecto_1;
    private String incorrecto_2;
    private String incorrecto_3;

    /**
     * Constructor
     *
     * @param enunciado
     * @param categoria
     * @param correcto
     * @param incorrecto_1
     * @param incorrecto_2
     * @param incorrecto_3
     */
    public Pregunta(String enunciado, String categoria, String correcto, String incorrecto_1, String incorrecto_2, String incorrecto_3) {
        this.enunciado = enunciado;
        this.categoria = categoria;
        this.correcto = correcto;
        this.incorrecto_1 = incorrecto_1;
        this.incorrecto_2 = incorrecto_2;
        this.incorrecto_3 = incorrecto_3;
    }

    /**
     * Constructor 2
     *
     * @param id
     * @param enunciado
     * @param categoria
     * @param correcto
     * @param incorrecto_1
     * @param incorrecto_2
     * @param incorrecto_3
     */
    public Pregunta(int id, String enunciado, String categoria, String correcto, String incorrecto_1, String incorrecto_2, String incorrecto_3) {
        this.id = id;
        this.enunciado = enunciado;
        this.categoria = categoria;
        this.correcto = correcto;
        this.incorrecto_1 = incorrecto_1;
        this.incorrecto_2 = incorrecto_2;
        this.incorrecto_3 = incorrecto_3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCorrecto() {
        return correcto;
    }

    public void setCorrecto(String correcto) {
        this.correcto = correcto;
    }

    public String getIncorrecto_1() {
        return incorrecto_1;
    }

    public void setIncorrecto_1(String incorrecto_1) {
        this.incorrecto_1 = incorrecto_1;
    }

    public String getIncorrecto_2() {
        return incorrecto_2;
    }

    public void setIncorrecto_2(String incorrecto_2) {
        this.incorrecto_2 = incorrecto_2;
    }

    public String getIncorrecto_3() {
        return incorrecto_3;
    }

    public void setIncorrecto_3(String incorrecto_3) {
        this.incorrecto_3 = incorrecto_3;
    }
}
