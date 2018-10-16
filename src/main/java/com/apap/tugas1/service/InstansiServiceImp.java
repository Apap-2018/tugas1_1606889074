package com.apap.tugas1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.repository.InstansiDB;
@Service
@Transactional
public class InstansiServiceImp implements InstansiService{
	@Autowired
	private InstansiDB instansiDb;
	
	@Override
	public List<InstansiModel> getAllInstansi() {
		return instansiDb.findAll();
	}

}
