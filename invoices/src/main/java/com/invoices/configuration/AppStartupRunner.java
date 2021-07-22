package com.invoices.configuration;

import com.invoices.model.Config;
import com.invoices.model.Invoice;
import com.invoices.model.InvoiceLine;
import com.invoices.repository.ConfigRepository;
import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
                Invoice.InvoiceFactory.create()
                        .number(754)
                        .name("nombre cliente 1")
                        .nif("71473203J")
                        .address("Avda. Picos de Europa, nº16")
                        .zip_code("24210")
                        .city("mansilla de las mulas")
                        .state("León")
                        .fecha(LocalDate.now())
                        .addItems(
                                List.of(
                                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 1 de la factura numero 754. Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754").price("100.0").build(),
                                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 2 de la factura numero 754").price("120.11").build(),
                                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 3 de la factura numero 754").price("131.14").build()
                                )
                        )
                        .build());

        invoiceService.create(
                Invoice.InvoiceFactory.create()
                        .number(755)
                        .name("nombre cliente 1")
                        .nif("71473203J")
                        .address("Avda. Picos de Europa, nº16")
                        .zip_code("24210")
                        .city("mansilla de las mulas")
                        .state("León")
                        .fecha(LocalDate.now())
                        .addItems(
                                List.of(
                                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 1 de la factura numero 754. Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754").price("100.0").build(),
                                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 2 de la factura numero 754").price("120.11").build(),
                                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 3 de la factura numero 754").price("131.14").build()
                                )
                        )
                        .build());

        invoiceService.create(
                Invoice.InvoiceFactory.create()
                        .number(756)
                        .name("nombre cliente 1")
                        .nif("71473203J")
                        .address("Avda. Picos de Europa, nº16")
                        .zip_code("24210")
                        .city("mansilla de las mulas")
                        .state("León")
                        .fecha(LocalDate.now())
                        .addItems(
                                List.of(
                                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 1 de la factura numero 754. Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754").price("100.0").build(),
                                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 2 de la factura numero 754").price("120.11").build(),
                                        InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 3 de la factura numero 754").price("131.14").build()
                                )
                        )
                        .build());
    }
}
