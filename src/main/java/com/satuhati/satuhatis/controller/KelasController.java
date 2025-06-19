package com.satuhati.satuhatis.controller;

import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.model.Dosen;
import com.satuhati.satuhatis.service.KelasService;
import com.satuhati.satuhatis.service.DosenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kelas")
public class KelasController {

    private final KelasService kelasService;
    private final DosenService dosenService;

    // === MAHASISWA VIEW ===
    @GetMapping("/list")
    public String listKelas(Model model) {
        List<Kelas> allKelas = kelasService.findAll();
        model.addAttribute("kelasList", allKelas);
        return "kelas/mahasiswa-list"; // view HTML lo di sini
    }

    // === DOSEN: LIHAT KELAS YANG DIA AJAR ===
    @GetMapping("/dosen")
    public String dosenKelas(@AuthenticationPrincipal User userDetails, Model model) {
        Dosen dosen = dosenService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Dosen gak ditemukan"));
        List<Kelas> kelasList = kelasService.findByDosen(dosen);
        model.addAttribute("kelasList", kelasList);
        return "kelas/dosen-list";
    }

    // === DOSEN: LIHAT KELAS KOSONG ===
    @GetMapping("/dosen/available")
    public String kelasTanpaDosen(Model model) {
        List<Kelas> kosongList = kelasService.findByDosenIsNull();
        model.addAttribute("kelasList", kosongList);
        return "kelas/dosen-available";
    }

    // === DOSEN: DAFTAR SEBAGAI PENGAJAR ===
    @PostMapping("/dosen/ajar/{id}")
    public String ajarKelas(@PathVariable Long id, @AuthenticationPrincipal User userDetails) {
        Dosen dosen = dosenService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Dosen gak ditemukan"));
        Kelas kelas = kelasService.findById(id).orElseThrow();

        if (kelas.getDosen() == null) {
            kelas.setDosen(dosen);
            kelasService.save(kelas);
        }

        return "redirect:/kelas/dosen";
    }

    // === ADMIN: LIHAT SEMUA & CRUD ===
    @GetMapping("/admin")
    public String adminKelas(Model model) {
        model.addAttribute("kelasList", kelasService.findAll());
        return "kelas/admin-list";
    }

    @GetMapping("/admin/add")
    public String showAddForm(Model model) {
        model.addAttribute("kelas", new Kelas());
        return "kelas/admin-add";
    }

    @PostMapping("/admin/save")
    public String saveKelas(@ModelAttribute("kelas") Kelas kelas) {
        kelasService.save(kelas);
        return "redirect:/kelas/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteKelas(@PathVariable Long id) {
        kelasService.deleteById(id);
        return "redirect:/kelas/admin";
    }
}
