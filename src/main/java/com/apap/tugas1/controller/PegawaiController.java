package com.apap.tugas1.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apap.tugas1.model.InstansiModel;
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
		model.addAttribute("instansiList", instansiService.getAllInstansi());
		return "home";
	}
	
	@RequestMapping("/pegawai")
	private String viewPegawaiByNip(@RequestParam(value = "nip") String nip, Model model) {
		PegawaiModel archive = pegawaiService.getPegawaiDetailByNip(nip);
		
		model.addAttribute("pegawai", archive);
		model.addAttribute("gaji", archive.getCalculatedGaji());
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
	
	@RequestMapping("/pegawai/termuda-tertua")
	private String viewPegawaiTermudaTertua(@RequestParam(value = "idInstansi") long idInstansi, Model model) {
		InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi);
		
		List<PegawaiModel> pegawaiList = instansi.getPegawaiInstansi();
		
		//menentukan pegawaiTermuda dan Tertua
		PegawaiModel pegawaiTermuda = new PegawaiModel();
		PegawaiModel pegawaiTertua = new PegawaiModel();
		
		int umurTermuda = 2018;
		int umurTertua = 0;
		for(PegawaiModel pegawai : pegawaiList) {
			int container = pegawai.getUmur();
			if(container < umurTermuda) {
				umurTermuda = container;
				pegawaiTermuda = pegawai;
			}
			
			if(container > umurTertua) {
				umurTertua = container;
				pegawaiTertua = pegawai;
			}
		}

		model.addAttribute("pegawaiTermuda", pegawaiTermuda);
		model.addAttribute("pegawaiTertua", pegawaiTertua);
		model.addAttribute("gajiTermuda", pegawaiTermuda.getCalculatedGaji());
		model.addAttribute("gajiTertua", pegawaiTertua.getCalculatedGaji());
		return "view-pegawai-termuda-tertua";
	}
}
