package com.apap.tugas1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.PegawaiService;

@Controller
public class PegawaiController {
	@Autowired 
	private PegawaiService pegawaiService;
	
	@RequestMapping("/")
	private String home(Model model) {
		model.addAttribute("title", "");
		return "home";
	}
	
	@RequestMapping("/pegawai/view")
	private String viewPegawaiByNip(@RequestParam(value = "nip") String nip, Model model) {
		PegawaiModel archive = pegawaiService.getPegawaiDetailByNip(nip);
		
		//menghitung gaji pokok
		double gaji=0;
		for(JabatanModel jabatan : archive.getJabatanList()) {
			double container = jabatan.getGajiPokok();
			if(gaji<container) {
				gaji = container;
			}
		}
		double tunjangan = archive.getInstansi().getProvinsi().getPresentaseTunjangan();
		gaji = gaji + (gaji*tunjangan/100);
		
		model.addAttribute("pegawai", archive);
		model.addAttribute("gaji", gaji);
		return "view-pegawai";
	}
}
