package com.satuhati.satuhatis.controller;

import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.model.KelasMahasiswa;
import com.satuhati.satuhatis.model.Mahasiswa;
import com.satuhati.satuhatis.repository.KelasMahasiswaRepository;
import com.satuhati.satuhatis.repository.KelasRepository;
import com.satuhati.satuhatis.service.MahasiswaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mahasiswa")
@RequiredArgsConstructor
public class MahasiswaController {

    private final MahasiswaService mahasiswaService;
    private final KelasRepository kelasRepository;
    private final KelasMahasiswaRepository kelasMahasiswaRepository;

    // Dashboard Mahasiswa
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        Mahasiswa mhs = mahasiswaService.findByUsername(user.getUsername()).orElseThrow();
        List<KelasMahasiswa> kelasTerdaftar = kelasMahasiswaRepository.findByMahasiswa(mhs);
        List<Kelas> semuaKelas = kelasRepository.findAll();

        model.addAttribute("mahasiswa", mhs);
        model.addAttribute("kelasTerdaftar", kelasTerdaftar);
        model.addAttribute("semuaKelas", semuaKelas); // Buat daftar peluang ikut kelas
        return "mahasiswa/dashboard";
    }

    // POST: Mahasiswa daftar ke kelas
    @PostMapping("/daftar-kelas")
    public String daftarKelas(@AuthenticationPrincipal User user,
                              @RequestParam Long kelasId,
                              RedirectAttributes redirectAttributes) {
        Mahasiswa mhs = mahasiswaService.findByUsername(user.getUsername()).orElseThrow();
        Kelas kelas = kelasRepository.findById(kelasId).orElseThrow();

        boolean sudahIkut = kelas.getPeserta()
            .stream()
            .anyMatch(km -> km.getMahasiswa().getId().equals(mhs.getId()));

        if (sudahIkut) {
            redirectAttributes.addFlashAttribute("error", "❌ Kamu sudah ikut kelas ini.");
        } else {
            KelasMahasiswa km = new KelasMahasiswa();
            km.setKelas(kelas);
            km.setMahasiswa(mhs);
            kelasMahasiswaRepository.save(km);
            redirectAttributes.addFlashAttribute("success", "✅ Berhasil daftar ke kelas.");
        }

        return "redirect:/mahasiswa/dashboard";
    }

    // Detail kelas yang diikuti mahasiswa
    @GetMapping("/kelas/{id}")
    public String detailKelas(@PathVariable Long id, Model model) {
        Kelas kelas = kelasRepository.findById(id).orElseThrow();
        model.addAttribute("kelas", kelas);
        model.addAttribute("peserta", kelas.getPeserta());
        return "mahasiswa/kelas-detail";
    }
}

