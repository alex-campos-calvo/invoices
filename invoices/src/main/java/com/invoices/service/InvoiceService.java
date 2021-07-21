package com.invoices.service;

import com.invoices.model.Config;
import com.invoices.model.Invoice;
import com.invoices.model.Invoice.InvoiceFactory;
import com.invoices.model.InvoiceLine;
import com.invoices.repository.ConfigRepository;
import com.invoices.repository.InvoiceRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class InvoiceService {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice getById(long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        return invoice.orElseGet(() -> InvoiceFactory.create().build());
    }

    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> getAllByTrimester(LocalDate date) {
        Map<String, Map<String, LocalDate>> trimester = initializeTrimesters(date);
        String i = chooseTrimester(date);
        LocalDate start = trimester.get(i).get("start");
        LocalDate end = trimester.get(i).get("end");
        return invoiceRepository.findByFechaBetween(start, end);
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

    public Invoice create(Invoice invoice) throws Exception {
        return create(invoice.getNum(), invoice.getName(), invoice.getNif(), invoice.getAddress(), invoice.getZip_code(), invoice.getState(), invoice.getFecha(), invoice.getLines());
    }

    public Invoice create(Integer num, String name, String nif, String address, String zip_code, String state, LocalDate fecha, List<InvoiceLine> invoiceLines) throws Exception {
        if(!checkIfCreatable(num, name, nif, address, zip_code, state, fecha, invoiceLines)) throw new Exception("No es posible crear la factura con esos datos");

        BigDecimal total = total(invoiceLines);
        BigDecimal iva = iva(total);

        Invoice invoice = InvoiceFactory.create()
                .number(num != null ? num : 0)
                .name(name != null ? StringUtils.capitalizeWords(name.trim()) : "")
                .nif(nif != null ? nif.trim().toUpperCase(Locale.ROOT) : "")
                .address(address != null ? StringUtils.capitalizeWords(address.trim()) : "")
                .zip_code(zip_code != null ? zip_code : "")
                .state(state != null ? StringUtils.capitalizeWords(state.trim()) : "")
                .fecha(fecha != null ? fecha : LocalDate.now())
                .iva(iva.setScale(2, RoundingMode.HALF_UP).toString())
                .total(total.setScale(2, RoundingMode.HALF_UP).toString())
                .addItems(invoiceLines != null ? invoiceLines : new ArrayList<>())
                .build();

        invoiceRepository.save(invoice);
        return invoice;
    }

    public Invoice update(Invoice invoice) throws Exception {
        return update(invoice.getId(), invoice.getNum(), invoice.getName(), invoice.getNif(), invoice.getAddress(), invoice.getZip_code(), invoice.getState(), invoice.getFecha(), invoice.getLines());
    }

    public Invoice update(long id, Integer num, String name, String nif, String address, String zip_code, String state, LocalDate fecha, List<InvoiceLine> invoiceLines) {
        BigDecimal total = total(invoiceLines);
        BigDecimal iva = iva(total);

        Invoice invoice = getById(id);
        InvoiceFactory.update(invoice)
                .number(num != null ? num : 0)
                .name(name != null ? StringUtils.capitalizeWords(name.trim()) : "")
                .nif(nif != null ? nif.trim().toUpperCase(Locale.ROOT) : "")
                .address(address != null ? StringUtils.capitalizeWords(address.trim()) : "")
                .zip_code(zip_code != null ? zip_code : "")
                .state(state != null ? StringUtils.capitalizeWords(state.trim()) : "")
                .fecha(fecha != null ? fecha : LocalDate.now())
                .iva(iva.setScale(2, RoundingMode.HALF_UP).toString())
                .total(total.setScale(2, RoundingMode.HALF_UP).toString())
                .addItems(invoiceLines != null ? invoiceLines : new ArrayList<>())
                .build();

        invoiceRepository.save(invoice);
        return invoice;
    }

    private boolean checkIfCreatable(Integer num, String name, String nif, String address, String zip_code, String state, LocalDate fecha, List<InvoiceLine> invoiceLines) {
        if((num != null)
                || (name != null && !name.trim().isEmpty())
                || (nif != null && !nif.trim().isEmpty())
                || (state != null && !state.trim().isEmpty())
                || (fecha != null)
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

    private BigDecimal total(List<InvoiceLine> invoiceLines){
        BigDecimal total = BigDecimal.valueOf(0);
        if(invoiceLines != null)
            for(InvoiceLine invoiceLine : invoiceLines) {
                if((invoiceLine.getPrice() != null && !invoiceLine.getPrice().trim().isEmpty())
                        && (invoiceLine.getQuantity() != null && !invoiceLine.getQuantity().trim().isEmpty())){
                    BigDecimal price = BigDecimal.valueOf(Double.parseDouble(invoiceLine.getPrice()));
                    BigDecimal quantity = BigDecimal.valueOf(Double.parseDouble(invoiceLine.getQuantity()));
                    total = total.add(price.multiply(quantity));
                }
            }
        return total;
    }

    private BigDecimal iva(BigDecimal total){
        Config config = configRepository.findAll().get(0);
        return total != null ? total.multiply(BigDecimal.valueOf(config.getIva())) : BigDecimal.valueOf(0);
    }

    public String chooseTrimester(LocalDate date){
        date = date != null ? date : LocalDate.now();
        Map<String, Map<String, LocalDate>> trimester = initializeTrimesters(date);
        if ((date.isAfter(trimester.get("1").get("start")) && date.isBefore(trimester.get("1").get("end")))
                || date.isEqual(trimester.get("1").get("start")) || date.isEqual(trimester.get("1").get("end")))
            return "1";
        else if ((date.isAfter(trimester.get("2").get("start")) && date.isBefore(trimester.get("2").get("end")))
                || date.isEqual(trimester.get("2").get("start")) || date.isEqual(trimester.get("2").get("end")))
            return "2";
        else if ((date.isAfter(trimester.get("3").get("start")) && date.isBefore(trimester.get("3").get("end")))
                || date.isEqual(trimester.get("3").get("start")) || date.isEqual(trimester.get("3").get("end")))
            return "3";
        else if ((date.isAfter(trimester.get("4").get("start")) && date.isBefore(trimester.get("4").get("end")))
                || date.isEqual(trimester.get("4").get("start")) || date.isEqual(trimester.get("4").get("end")))
            return "4";
        return "0";
    }

    private Map<String, Map<String, LocalDate>> initializeTrimesters(LocalDate date) {
        Map<String, Map<String, LocalDate>> trimester = new HashMap<String, Map<String, LocalDate>>();
        trimester.put("0", new HashMap<String, LocalDate>());
        trimester.get("0").put("start", LocalDate.now());
        trimester.get("0").put("end", LocalDate.now());
        trimester.put("1", new HashMap<String, LocalDate>());
        trimester.put("2", new HashMap<String, LocalDate>());
        trimester.put("3", new HashMap<String, LocalDate>());
        trimester.put("4", new HashMap<String, LocalDate>());
        trimester.get("1").put("start", LocalDate.of(date.getYear(), 1, 1));
        trimester.get("1").put("end", LocalDate.of(date.getYear(), 3, 31));
        trimester.get("2").put("start", LocalDate.of(date.getYear(), 4, 1));
        trimester.get("2").put("end", LocalDate.of(date.getYear(), 6, 30));
        trimester.get("3").put("start", LocalDate.of(date.getYear(), 7, 1));
        trimester.get("3").put("end", LocalDate.of(date.getYear(), 9, 30));
        trimester.get("4").put("start", LocalDate.of(date.getYear(), 10, 1));
        trimester.get("4").put("end", LocalDate.of(date.getYear(), 12, 31));
        return trimester;
    }
}
