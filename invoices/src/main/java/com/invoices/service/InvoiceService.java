package com.invoices.service;

import com.invoices.model.Config;
import com.invoices.model.Invoice;
import com.invoices.model.Invoice.InvoiceFactory;
import com.invoices.model.InvoiceLine;
import com.invoices.repository.ConfigRepository;
import com.invoices.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private FileService fileService;

    public Invoice getById(long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        return invoice.orElseGet(() -> InvoiceFactory.create().build());
    }

    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    public void deleteById(long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        invoice.ifPresent(value -> invoiceRepository.delete(value));
    }

    public Invoice create() {
        Invoice invoice = InvoiceFactory.create()
                .number(0)
                .name("")
                .nif("")
                .address("")
                .zip_code("")
                .state("")
                .total("")
                .addItems(new ArrayList<InvoiceLine>())
                .build();

        invoiceRepository.save(invoice);
        return invoice;
    }

    public Invoice create(Invoice invoice) {
        return create(invoice.getName(), invoice.getNif(), invoice.getAddress(), invoice.getZip_code(), invoice.getState(), invoice.getLines());
    }

    public Invoice create(String name, String nif, String address, String zip_code, String state, List<InvoiceLine> invoiceLines) {
        if(!checkIfCreatable(name, nif, address, zip_code, state, invoiceLines)) return Invoice.InvoiceFactory.create().build();

        Config config = configRepository.findAll().get(0);

        BigDecimal total = BigDecimal.valueOf(0);
        if(invoiceLines != null)
            for(InvoiceLine invoiceLine : invoiceLines) {
                if(invoiceLine.getPrice() != null && !invoiceLine.getPrice().trim().isEmpty())
                    total = total.add(BigDecimal.valueOf(Double.parseDouble(invoiceLine.getPrice())));
            }

        BigDecimal iva = total.multiply(BigDecimal.valueOf(config.getIva()));

        Invoice invoice = InvoiceFactory.create()
                .number(config.getNum())
                .name(name != null ? StringUtils.capitalizeWords(name.trim()) : "")
                .nif(nif != null ? nif.trim().toUpperCase(Locale.ROOT) : "")
                .address(address != null ? StringUtils.capitalizeWords(address.trim()) : "")
                .zip_code(zip_code != null ? zip_code : "")
                .state(state != null ? StringUtils.capitalizeWords(state.trim()) : "")
                .iva(iva.setScale(2, RoundingMode.HALF_UP).toString())
                .total(total.setScale(2, RoundingMode.HALF_UP).toString())
                .addItems(invoiceLines != null ? invoiceLines : new ArrayList<>())
                .build();

        config.setNum(config.getNum()+1);
        invoiceRepository.save(invoice);
        configRepository.save(config);
        return invoice;
    }

    public Invoice update(Invoice invoice) {
        return update(invoice.getId(), invoice.getName(), invoice.getNif(), invoice.getAddress(), invoice.getZip_code(), invoice.getState(), invoice.getLines());
    }

    public Invoice update(long id, String name, String nif, String address, String zip_code, String state, List<InvoiceLine> invoiceLines) {
        Config config = configRepository.findAll().get(0);

        BigDecimal total = BigDecimal.valueOf(0);
        if(invoiceLines != null)
            for(InvoiceLine invoiceLine : invoiceLines) {
                if(invoiceLine.getPrice() != null && !invoiceLine.getPrice().trim().isEmpty())
                    total = total.add(BigDecimal.valueOf(Double.parseDouble(invoiceLine.getPrice())));
            }

        BigDecimal iva = total.multiply(BigDecimal.valueOf(config.getIva()));

        Invoice invoice = getById(id);
        InvoiceFactory.update(invoice)
                .name(name != null ? StringUtils.capitalizeWords(name.trim()) : "")
                .nif(nif != null ? nif.trim().toUpperCase(Locale.ROOT) : "")
                .address(address != null ? StringUtils.capitalizeWords(address.trim()) : "")
                .zip_code(zip_code != null ? zip_code : "")
                .state(state != null ? StringUtils.capitalizeWords(state.trim()) : "")
                .iva(iva.setScale(2, RoundingMode.HALF_UP).toString())
                .total(total.setScale(2, RoundingMode.HALF_UP).toString())
                .addItems(invoiceLines != null ? invoiceLines : new ArrayList<>())
                .build();

        invoiceRepository.save(invoice);
        return invoice;
    }

    private boolean checkIfCreatable(String name, String nif, String address, String zip_code, String state, List<InvoiceLine> invoiceLines) {
        if((name != null && !name.trim().isEmpty())
                || (nif != null && !nif.trim().isEmpty())
                || (state != null && !state.trim().isEmpty())
                || (address != null && !address.trim().isEmpty())
                || (zip_code != null && !zip_code.trim().isEmpty())
        ) return true;

        if(invoiceLines != null && !invoiceLines.isEmpty())
            for(InvoiceLine invoiceLine : invoiceLines) {
                if((invoiceLine.getQuantity() != null && !invoiceLine.getQuantity().trim().isEmpty())
                    || (invoiceLine.getDescription() != null && !invoiceLine.getDescription().trim().isEmpty())
                    || (invoiceLine.getPrice() != null && !invoiceLine.getPrice().trim().isEmpty())
                ) return true;
            }

        return false;
    }
}
