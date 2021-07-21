package com.invoices.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoice")
@EntityListeners(AuditingEntityListener.class)
public class Invoice {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @Column(name = "num")
    private Integer num;

    @Column(name = "name", length = 500)
    private String name;

    @Column(name = "nif")
    private String nif;

    @Column(name = "address", length = 2000)
    private String address;

    @Column(name = "zip_code")
    private String zip_code;

    @Column(name = "state")
    private String state;

    @Column(name = "fecha")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @Column(name = "iva")
    private String iva;

    @Column(name = "total")
    private String total;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice")
    private List<InvoiceLine> lines = new ArrayList<InvoiceLine>();

    public Invoice() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<InvoiceLine> getLines() {
        return lines;
    }

    public void setLines(List<InvoiceLine> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", num=" + num +
                ", name='" + name + '\'' +
                ", nif='" + nif + '\'' +
                ", address='" + address + '\'' +
                ", zip_code='" + zip_code + '\'' +
                ", state='" + state + '\'' +
                ", fecha=" + fecha +
                ", iva='" + iva + '\'' +
                ", total='" + total + '\'' +
                ", lines=" + lines +
                '}';
    }

    public static class InvoiceFactory {

        private Invoice invoice = new Invoice();

        public static InvoiceFactory create() {
            return new InvoiceFactory();
        }

        public static InvoiceFactory update(Invoice invoice) {
            InvoiceFactory invoiceFactory = new InvoiceFactory();
            invoiceFactory.invoice = invoice;
            return invoiceFactory;
        }

        public InvoiceFactory number(int num) {
            invoice.num = num;
            return this;
        }

        public InvoiceFactory name(String name) {
            if(name != null && !name.trim().isEmpty())
                invoice.name = name;
            return this;
        }

        public InvoiceFactory nif(String nif) {
            if(nif != null && !nif.trim().isEmpty())
                invoice.nif = nif;
            return this;
        }

        public InvoiceFactory address(String address) {
            if(address != null && !address.trim().isEmpty())
                invoice.address = address;
            return this;
        }

        public InvoiceFactory state(String state) {
            if(state != null && !state.trim().isEmpty())
                invoice.state = state;
            return this;
        }

        public InvoiceFactory fecha(LocalDate fecha) {
            if(fecha != null)
                invoice.fecha = fecha;
            return this;
        }

        public InvoiceFactory zip_code(String zip_code) {
            if(zip_code != null && !zip_code.trim().isEmpty())
                invoice.zip_code = zip_code;
            return this;
        }

        public InvoiceFactory iva(String iva) {
            if(iva != null && !iva.trim().isEmpty())
                invoice.iva = iva;
            return this;
        }

        public InvoiceFactory total(String total) {
            if(total != null && !total.trim().isEmpty())
                invoice.total = total;
            return this;
        }

        public InvoiceFactory addItem(InvoiceLine invoiceLine) {
            if(invoiceLine != null) {
                invoiceLine.setInvoice(invoice);
                invoice.lines.add(invoiceLine);
            }
            return this;
        }

        public InvoiceFactory addItems(List<InvoiceLine> invoiceLines) {
            if(invoiceLines != null && !invoiceLines.isEmpty()){
                for(InvoiceLine invoiceLine : invoiceLines)
                    invoiceLine.setInvoice(invoice);
                invoice.lines.addAll(invoiceLines);
            }
            return this;
        }

        public Invoice build() {
            return invoice;
        }

    }
}
