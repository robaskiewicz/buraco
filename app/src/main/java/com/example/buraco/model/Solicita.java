package com.example.buraco.model;

import com.orm.SugarRecord;

import java.util.Date;

public class Solicita extends SugarRecord {

    private String titulo;
    private String descricao;
    private Double longitude;
    private Double latitude;
    private String imagem1;
    private String status;
    private Date dataCadastro = new Date();
    private String email;
    private String chave;
    private String cep;


    public Solicita(String titulo, String descricao,
                    Double longitude, Double latitude,
                    String imagem1, String status,
                    Date dataCadastro, String email,
                    String chave, String cep) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.longitude = longitude;
        this.latitude = latitude;
        this.imagem1 = imagem1;
        this.status = status;
        this.dataCadastro = dataCadastro;
        this.email = email;
        this.chave = chave;
        this.cep = cep;
    }

    public Solicita() {
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getImagem1() {
        return imagem1;
    }

    public void setImagem1(String imagem1) {
        this.imagem1 = imagem1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
