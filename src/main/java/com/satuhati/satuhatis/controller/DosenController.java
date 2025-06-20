package com.satuhati.satuhatis.controller;

import com.satuhati.satuhatis.model.Dosen;
import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.repository.FakultasRepository;
import com.satuhati.satuhatis.repository.KelasRepository;
import com.satuhati.satuhatis.repository.MataKuliahRepository;
import com.satuhati.satuhatis.repository.ProdiRepository;
import com.satuhati.satuhatis.service.DosenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dosen")
@RequiredArgsConstructor
public class DosenController {

    private final FakultasRepository fakultasRepository;
    private final ProdiRepository prodiRepository;
    private final MataKuliahRepository matakuliahRepository;
    private final DosenService dosenService;
    private final KelasRepository kelasRepository;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        Optional<Dosen> dosenOpt = dosenService.findByUsername(user.getUsername());
        if (dosenOpt.isEmpty()) return "redirect:/logout";

        Dosen dosen = dosenOpt.get();
        List<Kelas> kelasDiampu = kelasRepository.findByDosen(dosen); // ini ambil dari DB
        List<Kelas> kelasKosong = kelasRepository.findByDosenIsNull();

        model.addAttribute("dosen", dosen);
        model.addAttribute("kelasDiampu", kelasDiampu); // <- ini penting
        model.addAttribute("jumlahKelas", kelasDiampu.size());
        model.addAttribute("kelasKosong", kelasKosong);

        return "dosen/dashboard";
    }

    @GetMapping("/kelas")
    public String semuaKelas(@RequestParam(required = false) Long fakultasId,
                            @RequestParam(required = false) Long prodiId,
                            @RequestParam(required = false) Long matakuliahId,
                            Model model) {

        List<Kelas> semuaKelas = kelasRepository.findAll();

        // Filtering
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

        model.addAttribute("semuaKelas", semuaKelas);
        model.addAttribute("fakultasList", fakultasRepository.findAll());
        model.addAttribute("prodiList", fakultasId != null ?
            prodiRepository.findByFakultas_Id(fakultasId) : List.of());
        model.addAttribute("matakuliahList", matakuliahRepository.findAll());

        model.addAttribute("selectedFakultasId", fakultasId);
        model.addAttribute("selectedProdiId", prodiId);
        model.addAttribute("selectedMatkulId", matakuliahId);

        return "dosen/kelas-semua";
    }

    @GetMapping("/kelas/{id}")
    public String detailKelas(@PathVariable Long id, Model model) {
        Kelas kelas = kelasRepository.findById(id).orElseThrow();
        model.addAttribute("kelas", kelas);
        model.addAttribute("peserta", kelas.getPeserta()); // Asumsikan ada getter peserta
        return "dosen/kelas-detail";
    }


    @PostMapping("/daftar-pengajar")
    public String daftarSebagaiPengajar(@AuthenticationPrincipal User user,
                                        @RequestParam Long kelasId,
                                        RedirectAttributes redirectAttributes) {
        Optional<Dosen> dosenOpt = dosenService.findByUsername(user.getUsername());
        Optional<Kelas> kelasOpt = kelasRepository.findById(kelasId);

        if (dosenOpt.isEmpty()) return "redirect:/logout";

        if (kelasOpt.isPresent()) {
            Kelas kelas = kelasOpt.get();
            if (kelas.getDosen() != null) {
                redirectAttributes.addFlashAttribute("error", "❌ Kelas ini sudah ada dosennya.");
            } else {
                kelas.setDosen(dosenOpt.get());
                kelasRepository.save(kelas);
                redirectAttributes.addFlashAttribute("success", "✅ Kamu berhasil daftar sebagai pengajar.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Kelas tidak ditemukan.");
        }

        return "redirect:/dosen/dashboard"; // Atau redirect ke /dosen/kelas terserah lo mau kemana
    }


}
