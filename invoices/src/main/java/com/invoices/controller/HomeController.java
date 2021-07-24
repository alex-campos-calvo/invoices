package com.invoices.controller;

import com.invoices.model.Invoice;
import com.invoices.model.InvoiceLine;
import com.invoices.service.FileService;
import com.invoices.service.GraphService;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private GraphService graphService;

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
    public String invoicePage(Model model, @PathVariable(required = false) Integer id) {
        if(id == null) return "redirect:/";
        logger.debug("ACCESS INVOICE ID " + id);
        model.addAttribute("invoice", invoiceService.getById(id));
        return "invoice";
    }

    @GetMapping(path = {"/invoice", "/invoice/"})
    public String createInvoicePage(Model model) {
        logger.debug("ACCESS CREATE INVOICE");
        Invoice invoice = Invoice.InvoiceFactory.create().build();
        model.addAttribute("invoice", invoice);
        return "redirect:/invoice/" + invoice.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteInvoice(Model model, @PathVariable("id") Integer id) {
        logger.info("DELETE INVOICE ID " + id);
        invoiceService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping(path = "/line/{id}")
    public String deleteLine(Model model, @PathVariable("id") Integer id) {
        logger.info("DELETE INVOICE LINE ID " + id);
        InvoiceLine invoiceLine = invoiceService.deleteLineById(id);
        return "redirect:/invoice/" + invoiceLine.getInvoice().getId();
    }

    @GetMapping(path = "/invoice-pdf/{id}")
    public void pdf(Model model, @PathVariable("id") Integer id, HttpServletResponse response) {
        logger.info("LOADING PDF - ID " + id);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=users_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()) + ".pdf");
        fileService.create(invoiceService.getById(id), response);
    }

    @PostMapping(path = "/save", params="action=line")
    public String line(Model model, Invoice invoice) {
        logger.info("ADDING LINE: " + invoice);
        Invoice i = Invoice.InvoiceFactory.create().build();
        try{
            invoice.getLines().add(
                    InvoiceLine.InvoiceLineFactory.create()
                            .quantity("")
                            .description("")
                            .price("")
                            .build());
            i = invoiceService.update(invoice);
        } catch (Exception e) {
            logger.error("ERROR: Ha ocurrido un error durante el guardado de una linea para la factura");
            logger.error("ERROR: " + e.getCause());
        }
        return "redirect:/invoice/" + i.getId();
    }

    @PostMapping(path = "/save", params="action=save")
    public String save(Model model, Invoice invoice, RedirectAttributes attributes) {
        logger.info("SAVING: " + invoice);
        Invoice i = Invoice.InvoiceFactory.create().build();
        try {
            i = invoiceService.update(invoice);
            attributes.addFlashAttribute("success", "GUARDADO CORRECTAMENTE");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "HA OCURRIDO UN PROBLEMA DURANTE EL GUARDADO");
            logger.error("ERROR: Ha ocurrido un error durante el guardado de la factura");
            logger.error("ERROR: " + e.getCause());
        }
        return "redirect:/invoice/" + i.getId();
    }

    @GetMapping("/graphs")
    public String graphsPage(Model model) {
        logger.debug("ACCESS GRAPHS");
        List<Invoice> invoices = invoiceService.getAllByYear(LocalDate.now().getYear());
        model.addAttribute("lineGraphData", graphService.lineGraphData(invoices));
        model.addAttribute("barGraphData", graphService.barGraphData(invoices));
        model.addAttribute("dateGraphData", graphService.dateGraphData(invoices));
        return "graphs";
    }

}
