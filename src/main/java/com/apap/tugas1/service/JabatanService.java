package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.JabatanModel;

public interface JabatanService {
	void addJabatan(JabatanModel jabatan);
	
	List<JabatanModel> getAllJabatan();
	
	JabatanModel getJabatanById(long id);
	
	void deleteJabatan(JabatanModel jabatan);
}
