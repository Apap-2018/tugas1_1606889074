package com.apap.tugas1.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.repository.PegawaiDB;

@Service
@Transactional
public class PegawaiServiceImp implements PegawaiService{
	@Autowired
	private PegawaiDB pegawaiDb;
	
	@Override
	public PegawaiModel getPegawaiDetailByNip(String nip) {
		return pegawaiDb.findByNip(nip);
	}

	@Override
	public void addPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);	
	}

	@Override
	public List<PegawaiModel> getAllPegawai() {
		// TODO Auto-generated method stub
		return pegawaiDb.findAll();
	}

	@Override
	public String getNip(PegawaiModel pegawai) {
		String nip = "";
		//kode provinsi
		ProvinsiModel provinsi = pegawai.getInstansi().getProvinsi();
		nip+=provinsi.getId();
		
		//urutan instansi di provinsi tsb
		int urutanInstansi = provinsi.getInstansiList().indexOf(pegawai.getInstansi()) + 1;
		
		if(urutanInstansi < 10) { nip+="0"+urutanInstansi;}
		else { nip+=urutanInstansi; }
		
		//tanggalLahir pegawai
		//format dateLama = "yyyy-mm-dd"
		String dateLama = pegawai.getTanggalLahir().toString();
		String ddmmyy = dateLama.substring(8) + dateLama.substring(5, 7) + dateLama.substring(2, 4);
		nip+=ddmmyy;
		
		//tahunMasuk
		nip+=pegawai.getTahunMasuk();
		
		//urutanMasuk
		InstansiModel instansi = pegawai.getInstansi();
		int jumlahNipAwalSama=1;
		for(PegawaiModel pegawaiCek : instansi.getPegawaiInstansi()) {
			if(nip.equals(pegawaiCek.getNip().substring(0, 14))) {
				jumlahNipAwalSama+=1;
			}
		}
		
		if(jumlahNipAwalSama < 10) {nip+="0"+jumlahNipAwalSama;}
		else {nip+=jumlahNipAwalSama;}
		
		return nip;
	}

	@Override
	public List<PegawaiModel> findByInstansiAndJabatanPegawai(InstansiModel instansi, JabatanModel jabatanPegawai) {
		List<PegawaiModel> listPegawaiByInstansiAndJabatanPegawai = new ArrayList<>();
		List<PegawaiModel> listPegawaiByInstansi = pegawaiDb.findByInstansi(instansi);
		
		for (int i = 0; i < listPegawaiByInstansi.size(); i++) {
			if (listPegawaiByInstansi.get(i).getJabatanList().contains(jabatanPegawai)) {
				listPegawaiByInstansiAndJabatanPegawai.add(listPegawaiByInstansi.get(i));
			}
		}
		
		return listPegawaiByInstansiAndJabatanPegawai;
	}

	@Override
	public List<PegawaiModel> getListPegawaiByInstansi(InstansiModel instansi) {
		// TODO Auto-generated method stub
		return pegawaiDb.findByInstansi(instansi);
	}

	@Override
	public List<PegawaiModel> getListPegawaiByJabatan(List<JabatanModel> jabatanList) {
		return pegawaiDb.findByJabatanList(jabatanList);
	}
}
