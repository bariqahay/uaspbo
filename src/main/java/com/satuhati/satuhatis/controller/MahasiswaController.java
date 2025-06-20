package com.satuhati.satuhatis.controller;

import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.model.KelasMahasiswa;
import com.satuhati.satuhatis.model.Mahasiswa;
import com.satuhati.satuhatis.repository.FakultasRepository;
import com.satuhati.satuhatis.repository.ProdiRepository;
import com.satuhati.satuhatis.repository.MataKuliahRepository;
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
    private final FakultasRepository fakultasRepository;
    private final ProdiRepository prodiRepository;
    private final MataKuliahRepository matakuliahRepository;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User user,
                            @RequestParam(required = false) Long fakultasId,
                            @RequestParam(required = false) Long prodiId,
                            @RequestParam(required = false) Long matakuliahId,
                            Model model) {

        Mahasiswa mhs = mahasiswaService.findByUsername(user.getUsername()).orElseThrow();
        List<KelasMahasiswa> kelasTerdaftar = kelasMahasiswaRepository.findByMahasiswa(mhs);

        List<Kelas> semuaKelas = kelasRepository.findAll();

        if (matakuliahId != null) {
            semuaKelas = semuaKelas.stream()
                    .filter(k -> k.getMatakuliah().getId().equals(matakuliahId))
                    .toList();
        }

        if (prodiId != null) {
            semuaKelas = semuaKelas.stream()
                    .filter(k -> k.getMatakuliah().getProdiList().stream()
                            .anyMatch(p -> p.getId().equals(prodiId)))
                    .toList();
        }

        if (fakultasId != null) {
            semuaKelas = semuaKelas.stream()
                    .filter(k -> k.getMatakuliah().getProdiList().stream()
                            .anyMatch(p -> p.getFakultas() != null && p.getFakultas().getId().equals(fakultasId)))
                    .toList();
        }

        model.addAttribute("mahasiswa", mhs);
        model.addAttribute("kelasTerdaftar", kelasTerdaftar);
        model.addAttribute("semuaKelas", semuaKelas);

        model.addAttribute("fakultasList", fakultasRepository.findAll());
        model.addAttribute("prodiList", fakultasId != null ?
                prodiRepository.findByFakultas_Id(fakultasId) : List.of());
        model.addAttribute("matakuliahList", matakuliahRepository.findAll());

        model.addAttribute("selectedFakultasId", fakultasId);
        model.addAttribute("selectedProdiId", prodiId);
        model.addAttribute("selectedMatkulId", matakuliahId);

        return "mahasiswa/dashboard";
    }

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

    @GetMapping("/kelas/{id}")
    public String detailKelas(@PathVariable Long id, Model model) {
        Kelas kelas = kelasRepository.findById(id).orElseThrow();
        model.addAttribute("kelas", kelas);
        model.addAttribute("peserta", kelas.getPeserta());
        return "mahasiswa/kelas-detail";
    }
}
