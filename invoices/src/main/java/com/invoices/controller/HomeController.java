package com.invoices.controller;

import com.invoices.model.Invoice;
import com.invoices.model.InvoiceLine;
import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class HomeController {

        @Autowired
        private InvoiceService invoiceService;

        @GetMapping("/")
        public String homePage(Model model) throws Exception {
            invoiceService.create(
                    754,
                    "nombre cliente 1",
                    "71473203J",
                    "Avda. Picos de Europa, nº16",
                    "24210",
                    "León",
                    "1359.50",
                    List.of(
                            InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 1 de la factura numero 754. Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754 Esto es la descripción 1 de la factura numero 754").price("100.0").build(),
                            InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 2 de la factura numero 754").price("100.0").build(),
                            InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 3 de la factura numero 754").price("100.0").build()
                    )
            );
            invoiceService.create(
                    755,
                    "nombre cliente 2",
                    "71473203J",
                    "Avda. Picos de Europa, nº16",
                    "24210",
                    "León",
                    "1359.50",
                    List.of(
                            InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 1 de la factura numero 755").price("100.0").build(),
                            InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 2 de la factura numero 755").price("100.0").build(),
                            InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 3 de la factura numero 755").price("100.0").build()
                    )
            );
            invoiceService.create(
                    756,
                    "nombre cliente 3",
                    "71473203J",
                    "Avda. Picos de Europa, nº16",
                    "24210",
                    "León",
                    "1359.50",
                    List.of(
                            InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 1 de la factura numero 756").price("100.0").build(),
                            InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 2 de la factura numero 756").price("100.0").build(),
                            InvoiceLine.InvoiceLineFactory.create().quantity("1").description("Esto es la descripción 3 de la factura numero 756").price("100.0").build()
                    )
            );

            model.addAttribute("invoice", Invoice.InvoiceFactory.create().build());
            model.addAttribute("invoices", invoiceService.getAll());
            return "home";
        }

        @PostMapping("/save-invoice")
        public String saveInvoice(final Invoice invoice, Model model) {
            System.out.println(invoice);
            return "home";
        }

        @GetMapping("/get-invoice/edit/{id}")
        public String getInvoiceEdit(Model model, @PathVariable("id") Optional<Integer> id) throws Exception {
            if(id.isPresent()) {
                model.addAttribute("invoice", invoiceService.getById(id.get()));
            } else model.addAttribute("invoice", invoiceService.create());

            return "fragments/modal/edit/frag-invoice-edit-modal :: frag-invoice-edit-modal";
        }

        @GetMapping("/get-invoice/send/{id}")
        public String getInvoiceSend(Model model, @PathVariable("id") Optional<Integer> id) throws Exception {
            if(id.isPresent()) {
                model.addAttribute("invoice", invoiceService.getById(id.get()));
            } else model.addAttribute("invoice", invoiceService.create());

            return "fragments/modal/send/frag-invoice-send-modal :: frag-invoice-send-modal";
        }

}
