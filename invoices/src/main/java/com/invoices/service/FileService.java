package com.invoices.service;

import com.invoices.model.Invoice;
import com.invoices.model.InvoiceLine;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {

    private Invoice invoice = Invoice.InvoiceFactory.create().build();

    public void create(Invoice invoice, HttpServletResponse response) {
        try {
            if(invoice != null) this.invoice = invoice;
            else this.invoice = Invoice.InvoiceFactory.create().build();

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            addContent(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addContent(Document document) throws DocumentException, IOException {
        createHeader(document);
        createHeaderTable(document);
        createClientTable(document);
        createTable(document);
        createTotalsTable(document);
    }

    private void createHeader(Document document) throws DocumentException, IOException {
        Font light_grey = new Font();
        light_grey.setColor(new BaseColor(166, 166, 166));
        light_grey.setSize(10);

        PdfPTable mainTable = new PdfPTable(2);
        mainTable.setTotalWidth(510f);
        mainTable.setLockedWidth(true);
        mainTable.setWidths(new float[] {255f, 255f});
        mainTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        mainTable.getDefaultCell().setPadding(0f);
        mainTable.getDefaultCell().setBorderWidth(0f);
        mainTable.setSpacingAfter(25);

        PdfPTable column1 = new PdfPTable(1);
        column1.setTotalWidth(255f);
        column1.setLockedWidth(true);
        column1.setWidths(new float[] {255f});
        column1.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell cell = new PdfPCell(new Phrase("Roberto Campos Rubio", light_grey));
        cell.setPadding(3);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setBorderWidth(0f);
        column1.addCell(cell);

        cell = new PdfPCell(new Phrase("C.I.F. 09735075A", light_grey));
        cell.setPadding(3);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setBorderWidth(0);
        column1.addCell(cell);

        cell = new PdfPCell(new Phrase("Carretera de Valladolid, S/N", light_grey));
        cell.setPadding(3);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setBorderWidth(0f);
        column1.addCell(cell);

        cell = new PdfPCell(new Phrase("24210, Mansilla De Las Mulas", light_grey));
        cell.setPadding(3);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setBorderWidth(0f);
        column1.addCell(cell);

        cell = new PdfPCell(new Phrase("Tel. 607248778", light_grey));
        cell.setPadding(3);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setBorderWidth(0f);
        column1.addCell(cell);

        Image jpg = Image.getInstance(ResourceUtils.getFile("classpath:static/img/image.png").getPath());
        jpg.scaleAbsolute(120, 60);

        PdfPTable column2 = new PdfPTable(1);
        column2.setTotalWidth(255f);
        column2.setLockedWidth(true);
        column2.setWidths(new float[] {255f});
        column2.setHorizontalAlignment(Element.ALIGN_RIGHT);

        cell = new PdfPCell(jpg);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(3);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setBorderWidth(0f);
        column2.addCell(cell);

        mainTable.addCell(column1);
        mainTable.addCell(column2);

        document.add(mainTable);
    }

    private void createHeaderTable(Document document) throws DocumentException {
        Font white = new Font();
        white.setColor(BaseColor.WHITE);
        white.setSize(13);

        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(510f);
        table.setLockedWidth(true);
        table.setWidths(new float[] {300f, 210f});
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setSpacingAfter(20);

        PdfPCell c1 = new PdfPCell(new Phrase("FACTURA " + invoice.getNum(), white));
        c1.setBackgroundColor(new BaseColor(126,151,173));
        c1.setBorderColor(new BaseColor(126,151,173));
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setPadding(5);
        c1.setUseAscender(true);
        c1.setUseDescender(true);
        c1.setFixedHeight(35);
        c1.setBorderWidth(0.3f);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(invoice.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), white));
        c1.setBackgroundColor(new BaseColor(126,151,173));
        c1.setBorderColor(new BaseColor(126,151,173));
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPadding(5);
        c1.setUseAscender(true);
        c1.setUseDescender(true);
        c1.setFixedHeight(35);
        c1.setBorderWidth(0.3f);
        table.addCell(c1);

        document.add(table);
    }

    private void createClientTable(Document document) throws DocumentException {
        Font light_blue = new Font();
        light_blue.setColor(new BaseColor(126, 151, 173));
        light_blue.setSize(10);

        Font black = new Font();
        black.setColor(BaseColor.BLACK);
        black.setSize(10);

        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(510f);
        table.setLockedWidth(true);
        table.setWidths(new float[] {510f});
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setSpacingAfter(20);

        PdfPCell cell = new PdfPCell(new Phrase("FACTURAR A", light_blue));
        cell.setPadding(3);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.setBorderWidth(0.3f);
        cell.setBorderWidthTop(0f);
        cell.setBorderWidthLeft(0f);
        cell.setBorderWidthRight(0f);
        cell.setBorderColor(new BaseColor(216, 223, 232));
        table.addCell(cell);

        PdfPCell name = new PdfPCell(new Phrase(invoice.getName() != null ? invoice.getName() : "", black));
        name.setPadding(3);
        name.setPaddingTop(5);
        name.setUseAscender(true);
        name.setUseDescender(true);
        name.setBorderWidth(0f);
        table.addCell(name);

        PdfPCell nif = new PdfPCell(new Phrase(invoice.getNif() != null ? invoice.getNif() : "", black));
        nif.setPadding(3);
        nif.setUseAscender(true);
        nif.setUseDescender(true);
        nif.setBorderWidth(0f);
        table.addCell(nif);

        PdfPCell address = new PdfPCell(
                new Phrase((invoice.getAddress() != null && !invoice.getAddress().trim().isEmpty() ? invoice.getAddress() + ", " : "") + (invoice.getZip_code() != null ? invoice.getZip_code() : ""), black)
        );
        address.setPadding(3);
        address.setUseAscender(true);
        address.setUseDescender(true);
        address.setBorderWidth(0f);
        table.addCell(address);

        PdfPCell city = new PdfPCell(
                new Phrase((invoice.getCity() != null && !invoice.getCity().trim().isEmpty() ? invoice.getCity() + ", " : "") + (invoice.getState() != null ? invoice.getState() : ""), black)
        );
        city.setPadding(3);
        city.setUseAscender(true);
        city.setUseDescender(true);
        city.setBorderWidth(0f);
        table.addCell(city);

        document.add(table);
    }

    private void createTable(Document document) throws DocumentException {
        Font white = new Font();
        white.setColor(BaseColor.WHITE);
        white.setSize(10);

        Font black = new Font();
        black.setColor(BaseColor.BLACK);
        black.setSize(10);

        PdfPTable table = new PdfPTable(3);
        table.setTotalWidth(510f);
        table.setLockedWidth(true);
        table.setWidths(new float[] {80f, 350f, 80f});
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell c1 = new PdfPCell(new Phrase("CANTIDAD", white));
        c1.setBackgroundColor(new BaseColor(126,151,173));
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setPadding(8);
        c1.setUseAscender(true);
        c1.setUseDescender(true);
        c1.setFixedHeight(35);
        c1.setBorderWidth(0.3f);
        c1.setBorderColor(new BaseColor(216, 223, 232));
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("DESCRIPCIÓN", white));
        c1.setBackgroundColor(new BaseColor(126,151,173));
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setPadding(8);
        c1.setUseAscender(true);
        c1.setUseDescender(true);
        c1.setFixedHeight(35);
        c1.setBorderWidth(0.3f);
        c1.setBorderColor(new BaseColor(216, 223, 232));
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("PRECIO", white));
        c1.setBackgroundColor(new BaseColor(126,151,173));
        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c1.setPadding(8);
        c1.setUseAscender(true);
        c1.setUseDescender(true);
        c1.setFixedHeight(35);
        c1.setBorderWidth(0.3f);
        c1.setBorderColor(new BaseColor(216, 223, 232));
        table.addCell(c1);

        table.setHeaderRows(1);

        int rows = 12;
        for(InvoiceLine lines : invoice.getLines()){
            PdfPCell quantity = new PdfPCell(new Phrase(lines.getQuantity() != null ? lines.getQuantity() : "", black));
            quantity.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            quantity.setPadding(8);
            quantity.setUseAscender(true);
            quantity.setUseDescender(true);
            quantity.setBorderWidth(0.3f);
            quantity.setBorderColor(new BaseColor(216, 223, 232));
            table.addCell(quantity);

            PdfPCell description = new PdfPCell(new Phrase(lines.getDescription() != null ? lines.getDescription() : "", black));
            description.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            description.setPadding(8);
            description.setUseAscender(true);
            description.setUseDescender(true);
            description.setBorderWidth(0.3f);
            description.setBorderColor(new BaseColor(216, 223, 232));
            table.addCell(description);

            PdfPCell price = new PdfPCell(new Phrase(lines.getPrice() != null ? lines.getPrice() + " €" : "", black));
            price.setHorizontalAlignment(Element.ALIGN_RIGHT);
            price.setPadding(8);
            price.setUseAscender(true);
            price.setUseDescender(true);
            price.setBorderWidth(0.3f);
            price.setBorderColor(new BaseColor(216, 223, 232));
            table.addCell(price);
            rows--;
        }

        if(rows > 0) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < 3; j++) {
                    PdfPCell cell = new PdfPCell();
                    cell.setPadding(0);
                    cell.setFixedHeight(20);
                    cell.setUseAscender(true);
                    cell.setUseDescender(true);
                    cell.setBorderWidth(0.3f);
                    cell.setBorderColor(new BaseColor(216, 223, 232));
                    table.addCell(cell);
                }
            }
        }

        document.add(table);
    }

    private void createTotalsTable(Document document) throws DocumentException {
        Font light_blue = new Font();
        light_blue.setColor(new BaseColor(126, 151, 173));
        light_blue.setSize(10);

        Font black = new Font();
        black.setColor(BaseColor.BLACK);
        black.setSize(10);

        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(200f);
        table.setLockedWidth(true);
        table.setWidths(new float[] {100f, 100f});
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell subtotal = new PdfPCell(new Phrase("SUBTOTAL", light_blue));
        subtotal.setPadding(8);
        subtotal.setUseAscender(true);
        subtotal.setUseDescender(true);
        subtotal.setBorderWidth(0.3f);
        subtotal.setBorderWidthLeft(0f);
        subtotal.setBorderWidthRight(0f);
        subtotal.setBorderColor(new BaseColor(216, 223, 232));
        table.addCell(subtotal);

        subtotal = new PdfPCell(new Phrase(invoice.getSubtotal() != null ? invoice.getSubtotal() + " €" : "", black));
        subtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subtotal.setPadding(8);
        subtotal.setUseAscender(true);
        subtotal.setUseDescender(true);
        subtotal.setBorderWidth(0.3f);
        subtotal.setBorderWidthLeft(0f);
        subtotal.setBorderWidthRight(0f);
        subtotal.setBorderColor(new BaseColor(216, 223, 232));
        table.addCell(subtotal);

        PdfPCell iva = new PdfPCell(new Phrase("IVA", light_blue));
        iva.setPadding(8);
        iva.setUseAscender(true);
        iva.setUseDescender(true);
        iva.setBorderWidth(0.3f);
        iva.setBorderWidthLeft(0f);
        iva.setBorderWidthRight(0f);
        iva.setBorderColor(new BaseColor(216, 223, 232));
        table.addCell(iva);

        iva = new PdfPCell(new Phrase(invoice.getIva() != null ? invoice.getIva() + " €" : "", black));
        iva.setHorizontalAlignment(Element.ALIGN_RIGHT);
        iva.setPadding(8);
        iva.setUseAscender(true);
        iva.setUseDescender(true);
        iva.setBorderWidth(0.3f);
        iva.setBorderWidthLeft(0f);
        iva.setBorderWidthRight(0f);
        iva.setBorderColor(new BaseColor(216, 223, 232));
        table.addCell(iva);

        PdfPCell total = new PdfPCell(new Phrase("TOTAL", light_blue));
        total.setPadding(8);
        total.setUseAscender(true);
        total.setUseDescender(true);
        total.setBorderWidth(0.3f);
        total.setBorderWidthLeft(0f);
        total.setBorderWidthRight(0f);
        total.setBorderColor(new BaseColor(216, 223, 232));
        table.addCell(total);

        total = new PdfPCell(new Phrase(invoice.getTotal() != null ? invoice.getTotal() + " €" : "", black));
        total.setHorizontalAlignment(Element.ALIGN_RIGHT);
        total.setPadding(8);
        total.setUseAscender(true);
        total.setUseDescender(true);
        total.setBorderWidth(0.3f);
        total.setBorderWidthLeft(0f);
        total.setBorderWidthRight(0f);
        total.setBorderColor(new BaseColor(216, 223, 232));
        table.addCell(total);

        document.add(table);
    }

    private void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

}
