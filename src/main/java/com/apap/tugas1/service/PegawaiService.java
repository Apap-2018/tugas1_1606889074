package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	PegawaiModel getPegawaiDetailByNip(String nip);

	void addPegawai(PegawaiModel pegawai);
	
	String getNip(PegawaiModel pegawai);
	
	List<PegawaiModel> getAllPegawai();
}
