package com.apap.tugas1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	private String addPilotSubmit(@ModelAttribute JabatanModel jabatan, Model model) {
		jabatanService.addJabatan(jabatan);
		return "add";
	}
	
	@RequestMapping("/jabatan/view")
	private String viewJabatanById(@RequestParam(value = "idJabatan") long id, Model model) {
		JabatanModel archive = jabatanService.getJabatanById(id);
		model.addAttribute("jabatan", archive);
		model.addAttribute("title", "Detail Jabatan");
		return "view-jabatan";
	}
}