package com.apap.tugas1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;

/**
 * PegawaiDB
 */
@Repository
public interface PegawaiDB extends JpaRepository<PegawaiModel, Long> {
	PegawaiModel findByNip(String nip);
	List<PegawaiModel> findAll();
	List<PegawaiModel> findByInstansi(InstansiModel instansi);
	List<PegawaiModel> findByJabatanList(List<JabatanModel> jabatanList);
}
