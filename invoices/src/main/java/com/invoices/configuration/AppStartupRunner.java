package com.invoices.configuration;

import com.invoices.model.Config;
import com.invoices.model.InvoiceLine;
import com.invoices.repository.ConfigRepository;
import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class AppStartupRunner implements ApplicationRunner {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        configRepository.save(Config.ConfigFactory.create().number(1).iva(.21).build());

        invoiceService.create(
                754,
                "nombre cliente 1",
                "71473203J",
                "Avda. Picos de Europa, nº16",
                "24210",
                "León",
                LocalDate.now(),
                List.of(
                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 1 de la factura numero 754. Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754").price("100.0").build(),
                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 2 de la factura numero 754").price("120.11").build(),
                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 3 de la factura numero 754").price("131.14").build()
                )
        );

        invoiceService.create(
                755,
                "nombre cliente 2",
                "71473203J",
                "Avda. Picos de Europa, nº16",
                "24210",
                "León",
                LocalDate.now(),
                List.of(
                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 1 de la factura numero 755").price("499.99").build(),
                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 2 de la factura numero 755").price("1.0").build(),
                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 3 de la factura numero 755").price("0.1").build()
                )
        );

        invoiceService.create(
                756,
                "nombre cliente 3",
                "71473203J",
                "Avda. Picos de Europa, nº16",
                "24210",
                "León",
                LocalDate.now(),
                List.of(
                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 1 de la factura numero 756").price("100.0").build(),
                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 2 de la factura numero 756").price("100.0").build(),
                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 3 de la factura numero 756").price("100.0").build()
                )
        );
    }
}
