package com.apap.tugas1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apap.tugas1.model.JabatanModel;

/**
 * JabatanDB
 */
@Repository
public interface JabatanDB extends JpaRepository<JabatanModel, Long> {
}
