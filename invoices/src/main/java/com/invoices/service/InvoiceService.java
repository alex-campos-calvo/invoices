package com.invoices.service;

import com.invoices.model.Config;
import com.invoices.model.Invoice;
import com.invoices.model.Invoice.InvoiceFactory;
import com.invoices.model.InvoiceLine;
import com.invoices.repository.ConfigRepository;
import com.invoices.repository.InvoiceLineRepository;
import com.invoices.repository.InvoiceRepository;
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

    @Autowired
    private InvoiceLineRepository invoiceLineRepository;

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

    public List<Invoice> getAllByYear(Integer year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return invoiceRepository.findByFechaBetween(start, end);
    }

    public boolean deleteById(long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if(invoice.isPresent()) {
            invoiceRepository.delete(invoice.get());
            return true;
        } else
            return false;
    }

    public InvoiceLine deleteLineById(long id) {
        Optional<InvoiceLine> invoiceLine = invoiceLineRepository.findById(id);
        if(invoiceLine.isPresent()) {
            invoiceLineRepository.delete(invoiceLine.get());
            return invoiceLine.get();
        } else
            return InvoiceLine.InvoiceLineFactory.create().build();
    }

    public Invoice create() {
        Invoice invoice = InvoiceFactory.create()
                .number(0)
                .name("")
                .nif("")
                .address("")
                .zip_code("")
                .state("")
                .subtotal("")
                .iva("")
                .total("")
                .addItems(new ArrayList<InvoiceLine>())
                .build();

        invoiceRepository.save(invoice);
        return invoice;
    }

    public Invoice create(Invoice invoice) throws Exception {
        if(invoice != null)
            return create(invoice.getNum(), invoice.getName(), invoice.getNif(), invoice.getAddress(), invoice.getZip_code(), invoice.getCity(), invoice.getState(), invoice.getFecha(), invoice.getLines());
        else {
            return Invoice.InvoiceFactory.create().build();
        }
    }

    private Invoice create(Integer num, String name, String nif, String address, String zip_code, String city, String state, LocalDate fecha, List<InvoiceLine> invoiceLines) throws Exception {
        if(!checkIfCreatable(num, name, nif, address, zip_code, city, state, fecha, invoiceLines)) throw new Exception("No es posible crear la factura con esos datos");

        BigDecimal subtotal = subtotal(invoiceLines);
        BigDecimal iva = iva(subtotal);
        BigDecimal total = total(subtotal, iva);

        Invoice invoice = InvoiceFactory.create()
                .number(num != null ? num : 0)
                .name(name != null ? StringUtils.capitalizeWords(name.trim()) : "")
                .nif(nif != null ? nif.trim().toUpperCase(Locale.ROOT) : "")
                .address(address != null ? StringUtils.capitalizeWords(address.trim()) : "")
                .zip_code(zip_code != null ? zip_code : "")
                .city(city != null ? StringUtils.capitalizeWords(city.trim()) : "")
                .state(state != null ? StringUtils.capitalizeWords(state.trim()) : "")
                .fecha(fecha != null ? fecha : LocalDate.now())
                .subtotal(subtotal.setScale(2, RoundingMode.HALF_UP).toString())
                .iva(iva.setScale(2, RoundingMode.HALF_UP).toString())
                .total(total.setScale(2, RoundingMode.HALF_UP).toString())
                .addItems(invoiceLines != null ? invoiceLines : new ArrayList<>())
                .build();

        invoiceRepository.save(invoice);
        return invoice;
    }

    public Invoice update(Invoice invoice) throws Exception {
        if(invoice != null)
            return update(invoice.getId(), invoice.getNum(), invoice.getName(), invoice.getNif(), invoice.getAddress(), invoice.getZip_code(), invoice.getCity(), invoice.getState(), invoice.getFecha(), invoice.getLines());
        else
            return Invoice.InvoiceFactory.create().build();
    }

    private Invoice update(long id, Integer num, String name, String nif, String address, String zip_code, String city, String state, LocalDate fecha, List<InvoiceLine> invoiceLines) {
        BigDecimal subtotal = subtotal(invoiceLines);
        BigDecimal iva = iva(subtotal);
        BigDecimal total = total(subtotal, iva);

        Invoice invoice = getById(id);
        InvoiceFactory.update(invoice)
                .number(num != null ? num : 0)
                .name(name != null ? StringUtils.capitalizeWords(name.trim()) : "")
                .nif(nif != null ? nif.trim().toUpperCase(Locale.ROOT) : "")
                .address(address != null ? StringUtils.capitalizeWords(address.trim()) : "")
                .zip_code(zip_code != null ? zip_code : "")
                .city(city != null ? StringUtils.capitalizeWords(city.trim()) : "")
                .state(state != null ? StringUtils.capitalizeWords(state.trim()) : "")
                .fecha(fecha != null ? fecha : LocalDate.now())
                .subtotal(subtotal.setScale(2, RoundingMode.HALF_UP).toString())
                .iva(iva.setScale(2, RoundingMode.HALF_UP).toString())
                .total(total.setScale(2, RoundingMode.HALF_UP).toString())
                .addItems(invoiceLines != null ? invoiceLines : new ArrayList<>())
                .build();

        invoiceRepository.save(invoice);
        return invoice;
    }

    private boolean checkIfCreatable(Integer num, String name, String nif, String address, String zip_code, String city, String state, LocalDate fecha, List<InvoiceLine> invoiceLines) {
        if((num != null)
                || (name != null && !name.trim().isEmpty())
                || (nif != null && !nif.trim().isEmpty())
                || (city != null && !city.trim().isEmpty())
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

    private BigDecimal subtotal(List<InvoiceLine> invoiceLines){
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

    private BigDecimal total(BigDecimal subtotal, BigDecimal iva){
        return subtotal != null && iva != null ? subtotal.add(iva) : BigDecimal.valueOf(0);
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
