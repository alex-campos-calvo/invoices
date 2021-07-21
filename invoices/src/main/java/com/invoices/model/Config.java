package com.invoices.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Config")
public class Config {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "num")
    private Integer num;

    @Column(name = "iva")
    private Double iva;

    public Config() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public static class ConfigFactory {

        private Config config = new Config();

        public static Config.ConfigFactory create() {
            return new Config.ConfigFactory();
        }

        public Config.ConfigFactory number(int num) {
            config.num = num;
            return this;
        }

        public Config.ConfigFactory iva(double iva) {
            config.iva = iva;
            return this;
        }

        public Config build() {
            return config;
        }

    }

}
