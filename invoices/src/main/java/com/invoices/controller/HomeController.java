package com.invoices.controller;

import com.invoices.model.Invoice;
import com.invoices.service.FileService;
import com.invoices.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private FileService fileService;

    @GetMapping(path = {"/", "/t/{fecha}"})
    public String homePage(Model model, @PathVariable(required = false) @DateTimeFormat(pattern = "d-M-yyyy") LocalDate fecha) {
        logger.debug("ACCESS HOME");
        fecha = fecha != null ? fecha : LocalDate.now();
        logger.info("LOADING TRIMESTER OF DATE " + fecha);
        model.addAttribute("fecha", fecha);
        model.addAttribute("trimester", invoiceService.chooseTrimester(fecha));
        model.addAttribute("invoices", invoiceService.getAllByTrimester(fecha));
        return "home";
    }

    @GetMapping("/invoice/{id}")
    public String invoicePage(Model model, @PathVariable("id") Integer id) {
        logger.debug("ACCESS INVOICE ID " + id);
        model.addAttribute("invoice", invoiceService.getById(id));
        return "invoice";
    }

    @GetMapping(path = "/invoice-pdf/{id}")
    public void pdf(Model model, @PathVariable("id") Integer id, HttpServletResponse response) {
        logger.info("LOADING PDF - ID " + id);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=users_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()) + ".pdf");
        fileService.create(invoiceService.getById(id), response);
    }

    @PostMapping(path = "/save")
    public String save(Model model, Invoice invoice, RedirectAttributes attributes) {
        logger.info("SAVE: " + invoice);
        try{
            invoiceService.update(invoice);
            attributes.addFlashAttribute("success","GUARDADO CORRECTAMENTE");
        } catch (Exception e) {
            attributes.addFlashAttribute("error","HA OCURRIDO UN PROBLEMA DURANTE EL GUARDADO");
            logger.error("ERROR: Ha ocurrido un error durante el guardado de la factura:");
            logger.error("ERROR: " + e.getCause());
        }
        return "redirect:/invoice/" + invoice.getId();
    }

    /*
    @PutMapping(path = "/delete-invoice/{id}")
    public ResponseEntity<Invoice> deleteInvoice(@PathVariable("id") Integer id) {
        invoiceService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/save/{id}", consumes = "application/json")
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
    */

}
