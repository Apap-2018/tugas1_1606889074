package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	PegawaiModel getPegawaiDetailByNip(String nip);

	void addPegawai(PegawaiModel pegawai);
	
	String getNip(PegawaiModel pegawai);
	
	List<PegawaiModel> getAllPegawai();
	
	List<PegawaiModel> findByInstansiAndJabatanPegawai(InstansiModel instansi, JabatanModel jabatanPegawai);
	
	List<PegawaiModel> getListPegawaiByInstansi(InstansiModel instansi);
	
	List<PegawaiModel> getListPegawaiByJabatan(List<JabatanModel> jabatanList);

}
