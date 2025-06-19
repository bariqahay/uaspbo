package com.satuhati.satuhatis.controller;

import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.model.KelasMahasiswa;
import com.satuhati.satuhatis.model.Mahasiswa;
import com.satuhati.satuhatis.service.KelasMahasiswaService;
import com.satuhati.satuhatis.service.KelasService;
import com.satuhati.satuhatis.service.MahasiswaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kelas-mahasiswa")
public class KelasMahasiswaController {

    private final MahasiswaService mahasiswaService;
    private final KelasService kelasService;
    private final KelasMahasiswaService kelasMahasiswaService;

    @PostMapping("/daftar/{kelasId}")
    public String daftarKelas(@AuthenticationPrincipal User user, @PathVariable Long kelasId) {
        Mahasiswa mhs = mahasiswaService.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Mahasiswa gak ditemukan"));
        Kelas kelas = kelasService.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas gak ditemukan"));

        if (!kelasMahasiswaService.isAlreadyRegistered(mhs, kelas)) {
            KelasMahasiswa km = new KelasMahasiswa();
            km.setMahasiswa(mhs);
            km.setKelas(kelas);
            kelasMahasiswaService.save(km);
        }

        return "redirect:/kelas"; // ganti sesuai route lo buat list kelas
    }

    @GetMapping("/saya")
    public String lihatKelasSaya(@AuthenticationPrincipal User user, Model model) {
        Mahasiswa mhs = mahasiswaService.findByUsername(user.getUsername())
                .orElseThrow();
        List<KelasMahasiswa> daftar = kelasMahasiswaService.findByMahasiswa(mhs);
        model.addAttribute("daftarKelas", daftar);
        return "kelas/mahasiswa-kelas-saya"; // lo bisa bikin html ini
    }
}
