package com.invoices.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Config")
public class Config {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "num")
    private Integer num;

    public Config() {

    }

    public static class ConfigFactory {

        private Config config = new Config();

        public static Config.ConfigFactory create() {
            return new Config.ConfigFactory();
        }

        public Config.ConfigFactory number(Integer num) {
            if(num != null)
                config.num = Integer.valueOf(num);
            return this;
        }

        public Config build() {
            return config;
        }

    }

}
