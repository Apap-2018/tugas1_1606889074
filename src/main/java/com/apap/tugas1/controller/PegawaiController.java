package com.apap.tugas1.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
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
		
		PegawaiModel pegawai = new PegawaiModel();
		if (pegawai.getJabatanList()==null) {
			pegawai.setJabatanList(new ArrayList<JabatanModel>());
		}
		pegawai.getJabatanList().add(new JabatanModel());
		
		model.addAttribute("pegawai", pegawai);
		
		return "addPegawai";
	}
	

	@RequestMapping(value="/pegawai/tambah", params={"removeRow"}, method = RequestMethod.POST)
	private String removeRowJabatanPegawaiBaru(@ModelAttribute PegawaiModel pegawai, final BindingResult bindingResult, final HttpServletRequest req, Model model) {
		model.addAttribute("provinsiList", provinsiService.getAllProvinsi());
		model.addAttribute("instansiList", instansiService.getAllInstansi());
		model.addAttribute("jabatanList", jabatanService.getAllJabatan());
		
		final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
		pegawai.getJabatanList().remove(rowId.intValue());
		model.addAttribute("pegawai", pegawai);
		
		return "addPegawai";
	}
	
	@RequestMapping(value="/pegawai/tambah", params={"addRow"}, method = RequestMethod.POST)
	private String addRowJabatanPegawaiBaru(@ModelAttribute PegawaiModel pegawai, Model model) {
		model.addAttribute("provinsiList", provinsiService.getAllProvinsi());
		model.addAttribute("instansiList", instansiService.getAllInstansi());
		model.addAttribute("jabatanList", jabatanService.getAllJabatan());
		
		if (pegawai.getJabatanList()==null) {
			pegawai.setJabatanList(new ArrayList<JabatanModel>());
		}
		pegawai.getJabatanList().add(new JabatanModel());
		
		model.addAttribute("pegawai", pegawai);
		
		return "addPegawai";
	}

	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPilotSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
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
		
		//addNiptoPegawai
		pegawai.setNip(nip);
		
		pegawaiService.addPegawai(pegawai);
		
		model.addAttribute("pegawai", pegawai);
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
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.GET)
	private String update(@RequestParam(value ="nip") String nip, Model model) {
		PegawaiModel archive = pegawaiService.getPegawaiDetailByNip(nip);
		
		model.addAttribute("pegawai", archive);
		model.addAttribute("title", "Ubah Pegawai");
		
		model.addAttribute("provinsiList", provinsiService.getAllProvinsi());
		model.addAttribute("instansiList", instansiService.getAllInstansi());
		model.addAttribute("jabatanList", jabatanService.getAllJabatan());
		
		return "updatePegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST)
	private String updatePegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		PegawaiModel archive = pegawaiService.getPegawaiDetailByNip(pegawai.getNip());
		
		archive.setInstansi(pegawai.getInstansi());
		archive.setJabatanList(pegawai.getJabatanList());
		archive.setNama(pegawai.getNama());
		archive.setTahunMasuk(pegawai.getTahunMasuk());
		archive.setTanggalLahir(pegawai.getTanggalLahir());
		archive.setTempatLahir(pegawai.getTempatLahir());
		
		pegawaiService.addPegawai(archive);
		
		model.addAttribute("title", "Ubah Jabatan");
		return "update";
		
	}
	
	@RequestMapping(value="/pegawai/ubah", params={"removeRow"}, method = RequestMethod.POST)
	private String removeRowJabatanPegawaiLama(@ModelAttribute PegawaiModel pegawai, final BindingResult bindingResult, final HttpServletRequest req, Model model) {
		model.addAttribute("provinsiList", provinsiService.getAllProvinsi());
		model.addAttribute("instansiList", instansiService.getAllInstansi());
		model.addAttribute("jabatanList", jabatanService.getAllJabatan());
		
		final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
		pegawai.getJabatanList().remove(rowId.intValue());
		model.addAttribute("pegawai", pegawai);
		
		return "updatePegawai";
	}
	
	@RequestMapping(value="/pegawai/ubah", params={"addRow"}, method = RequestMethod.POST)
	private String addRowJabatanPegawaiLama(@ModelAttribute PegawaiModel pegawai, Model model) {
		model.addAttribute("provinsiList", provinsiService.getAllProvinsi());
		model.addAttribute("instansiList", instansiService.getAllInstansi());
		model.addAttribute("jabatanList", jabatanService.getAllJabatan());
		
		if (pegawai.getJabatanList()==null) {
			pegawai.setJabatanList(new ArrayList<JabatanModel>());
		}
		pegawai.getJabatanList().add(new JabatanModel());
		
		model.addAttribute("pegawai", pegawai);
		
		return "updatePegawai";
	}
	
}
