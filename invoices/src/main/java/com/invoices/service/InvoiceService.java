package com.invoices.service;

import com.invoices.model.Invoice;
import com.invoices.model.Invoice.InvoiceFactory;
import com.invoices.model.InvoiceLine;
import com.invoices.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private FileService fileService;

    public Invoice create() throws Exception {
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

    public Invoice create(Integer num, String name, String nif, String address, String zip_code, String state, String total, List<InvoiceLine> invoiceLines) throws Exception {
        Invoice invoice = InvoiceFactory.create()
                .number(num)
                .name(name)
                .nif(nif)
                .address(address)
                .zip_code(zip_code)
                .state(state)
                .total(total)
                .addItems(invoiceLines)
                .build();

        invoiceRepository.save(invoice);
        return invoice;
    }

    public Invoice getById(Integer id) throws Exception {
        if(id != null) {
            Optional<Invoice> invoice = invoiceRepository.findById(id);
            if(invoice.isPresent())
                return invoice.get();
            else
                return InvoiceFactory.create().build();
        }
        throw new Exception("Especifica una ID con la que buscar la factura.");
    }

    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

}
