package com.invoices.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invoice_lines")
@EntityListeners(AuditingEntityListener.class)
public class InvoiceLine {

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

    @ManyToOne
    @JoinColumn(name = "FK_INVOICE", nullable = false, updatable = false)
    private Invoice invoice;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "price")
    private String price;

    public InvoiceLine() {

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

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "InvoiceLine{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    public static class InvoiceLineFactory {

        private InvoiceLine invoiceLine = new InvoiceLine();

        public static InvoiceLine.InvoiceLineFactory create() {
            return new InvoiceLine.InvoiceLineFactory();
        }

        public InvoiceLine.InvoiceLineFactory quantity(String quantity) {
            if(quantity != null && !quantity.trim().isEmpty())
                invoiceLine.quantity = quantity;
            return this;
        }

        public InvoiceLine.InvoiceLineFactory description(String description) {
            if(description != null && !description.trim().isEmpty())
                invoiceLine.description = description;
            return this;
        }

        public InvoiceLine.InvoiceLineFactory price(String price) {
            if(price != null && !price.trim().isEmpty())
                invoiceLine.price = price;
            return this;
        }

        public InvoiceLine build() {
            return invoiceLine;
        }

    }

}
