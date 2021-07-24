package com.invoices.service;

import com.invoices.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphService {

    @Autowired
    private InvoiceService invoiceService;

    public String dateGraphData(List<Invoice> invoices) {
        StringBuilder dateGraphData = new StringBuilder("[");
        for(Invoice invoice : invoices) {
            if(invoice.getFecha() != null && invoice.getTotal() != null){
                dateGraphData.append("[new Date(");
                dateGraphData.append(invoice.getFecha().getYear());
                dateGraphData.append(",");
                dateGraphData.append(invoice.getFecha().getMonthValue());
                dateGraphData.append(",");
                dateGraphData.append(invoice.getFecha().getDayOfMonth());
                dateGraphData.append("),");
                dateGraphData.append(invoice.getTotal());
                dateGraphData.append("]");
            }
        }
        dateGraphData.append("]");
        return dateGraphData.toString().replaceAll("]\\[", "],[");
    }

    public List<List<Object>> lineGraphData(List<Invoice> invoices) {
        List<List<Object>> lineGraphData = new ArrayList<List<Object>>();
        for(Invoice invoice : invoices) {
            lineGraphData.add(
                    List.of(
                            "Factura " + invoice.getNum(),
                            invoice.getTotal() != null ? Double.parseDouble(invoice.getTotal()) : 0
                    )
            );
        }
        return lineGraphData;
    }

    public List<List<Object>> barGraphData(List<Invoice> invoices) {
        Map<String, BigDecimal> trimesters = new HashMap<String, BigDecimal>();
        trimesters.put("1", BigDecimal.valueOf(0));
        trimesters.put("2", BigDecimal.valueOf(0));
        trimesters.put("3", BigDecimal.valueOf(0));
        trimesters.put("4", BigDecimal.valueOf(0));
        for(Invoice invoice : invoices) {
            if(invoice.getFecha() != null && invoice.getTotal() != null){
                String trimester = invoiceService.chooseTrimester(invoice.getFecha());
                if(trimesters.containsKey(trimester))
                    trimesters.put(
                            trimester,
                            trimesters.get(trimester).add(BigDecimal.valueOf(Double.parseDouble(invoice.getTotal())))
                    );
            }
        }

        return List.of(
                List.of("Trimestre 1", trimesters.get("1")),
                List.of("Trimestre 2", trimesters.get("2")),
                List.of("Trimestre 3", trimesters.get("3")),
                List.of("Trimestre 4", trimesters.get("4"))
        );
    }
}
