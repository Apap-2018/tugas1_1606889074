package com.apap.tugas1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.repository.JabatanDB;

@Service
@Transactional
public class JabatanServiceImp implements JabatanService{

	@Autowired
	private JabatanDB jabatanDb;
	
	@Override
	public void addJabatan(JabatanModel jabatan) {
		jabatanDb.save(jabatan);
	}

}
