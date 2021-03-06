package com.invoices.repository;

import com.invoices.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findById(long id);
    List<Invoice> findByFechaBetween(LocalDate start, LocalDate stop);
}