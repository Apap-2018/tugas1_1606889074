package com.apap.tugas1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class PegawaiController {
	@Autowired 
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@RequestMapping("/")
	private String home(Model model) {
		model.addAttribute("title", "");
		model.addAttribute("jabatanList", jabatanService.getAllJabatan());
		return "home";
	}
	
	@RequestMapping("/pegawai")
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

	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		
		model.addAttribute("provinsiList", provinsiService.getAllProvinsi());
		model.addAttribute("instansiList", instansiService.getAllInstansi());
		model.addAttribute("jabatanList", jabatanService.getAllJabatan());
		model.addAttribute("pegawai", new PegawaiModel());
		return "addPegawai";
	}

	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPilotSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		pegawaiService.addPegawai(pegawai);
		return "add";
	}
}
