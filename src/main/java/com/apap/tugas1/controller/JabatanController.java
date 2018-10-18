package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.service.JabatanService;

@Controller
public class JabatanController {
	@Autowired
	private JabatanService jabatanService;
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("jabatan", new JabatanModel());
		return "addJabatan";
	}

	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST)
	private String addJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.addJabatan(jabatan);
		return "add";
	}
	
	@RequestMapping("/jabatan/view")
	private String viewJabatanById(@RequestParam(value = "idJabatan") long id, Model model) {
		JabatanModel archive = jabatanService.getJabatanById(id);
		model.addAttribute("jabatan", archive);
		model.addAttribute("jumlahPegawai", archive.getPegawaiList().size());
		model.addAttribute("title", "Detail Jabatan");
		return "view-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.GET)
	private String update(@RequestParam(value ="idJabatan") long id, Model model) {
		JabatanModel archive = jabatanService.getJabatanById(id);
		
		model.addAttribute("jabatan", archive);
		model.addAttribute("title", "Ubah Jabatan");
		return "updateJabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	private String updateJabatanSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		JabatanModel archive = jabatanService.getJabatanById(jabatan.getId());
		
		archive.setDeskripsi(jabatan.getDeskripsi());
		archive.setGajiPokok(jabatan.getGajiPokok());
		archive.setNama(jabatan.getNama());
		
		jabatanService.addJabatan(archive);
		
		model.addAttribute("title", "Ubah Jabatan");
		return "update";
		
	}
	
	@RequestMapping(value="/jabatan/hapus", method= RequestMethod.POST)
	private String deleteJabatan(@ModelAttribute JabatanModel jabatan, Model model) {
		JabatanModel archive = jabatanService.getJabatanById(jabatan.getId());
		
		jabatanService.deleteJabatan(archive);
		
		model.addAttribute("title", "Hapus Jabatan");
		return "delete";
	}
	
	@RequestMapping(value="/jabatan/viewall")
	private String viewAll(Model model) {
		List<JabatanModel> jabatanList = jabatanService.getAllJabatan();
		
		model.addAttribute("jabatanList", jabatanList);
		model.addAttribute("title", "View All Jabatan");
		return "view-jabatan-all";
	}
}
