package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.ProvinsiModel;

public interface ProvinsiService {
	List<ProvinsiModel> getAllProvinsi();
	ProvinsiModel getProvinsiDetailById(long id);
}
