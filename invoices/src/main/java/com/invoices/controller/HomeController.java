package com.invoices.controller;

import com.invoices.model.Invoice;
import com.invoices.model.InvoiceLine;
import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;

@Controller
public class HomeController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("invoices", invoiceService.getAll());
        return "home";
    }

    @PutMapping(path = "/delete-invoice/{id}")
    public ResponseEntity<Invoice> deleteInvoice(@PathVariable("id") Integer id) {
        invoiceService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/save-new-invoice", consumes = "application/json")
    public ResponseEntity<Invoice> saveInvoice(@RequestBody Invoice invoice) {
        System.out.println("LOG: Creating Invoice: " + invoice);
        invoiceService.create(invoice);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/update-invoice", consumes = "application/json")
    public ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice invoice) {
        System.out.println("LOG: Updating Invoice: " + invoice);
        invoiceService.update(invoice);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-invoice/edit/{id}")
    public String getInvoiceEdit(Model model, @PathVariable("id") Integer id) {
        if(id != null) {
            Invoice invoice = invoiceService.getById(id);
            invoice.getLines().sort(Comparator.comparing(InvoiceLine::getDescription));
            model.addAttribute("invoice", invoice);
        } else model.addAttribute("invoice", invoiceService.create());

        return "fragments/modal/edit/frag-invoice-edit-modal :: frag-invoice-edit-modal";
    }

    @GetMapping("/get-invoice/send/{id}")
    public String getInvoiceSend(Model model, @PathVariable("id") Integer id) {
        if(id != null) {
            Invoice invoice = invoiceService.getById(id);
            invoice.getLines().sort(Comparator.comparing(InvoiceLine::getDescription));
            model.addAttribute("invoice", invoice);
        } else model.addAttribute("invoice", invoiceService.create());

        return "fragments/modal/send/frag-invoice-send-modal :: frag-invoice-send-modal";
    }

}
