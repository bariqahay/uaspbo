// AdminController.java
package com.satuhati.satuhatis.controller;

import com.satuhati.satuhatis.model.*;
import com.satuhati.satuhatis.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final KelasRepository kelasRepository;
    private final DosenRepository dosenRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final KelasMahasiswaRepository kelasMahasiswaRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalKelas = kelasRepository.count();
        long totalDosen = dosenRepository.count();
        long totalMahasiswa = mahasiswaRepository.count();
        long totalMataKuliah = mataKuliahRepository.count();

        model.addAttribute("totalKelas", totalKelas);
        model.addAttribute("totalDosen", totalDosen);
        model.addAttribute("totalMahasiswa", totalMahasiswa);
        model.addAttribute("totalMataKuliah", totalMataKuliah);

        return "admin/dashboard";
    }

    @GetMapping("/kelas")
    public String listKelas(Model model) {
        List<Kelas> kelasList = kelasRepository.findAll();
        model.addAttribute("kelasList", kelasList);
        return "admin/kelas-list";
    }

    @GetMapping("/kelas/tambah")
    public String formTambahKelas(Model model) {
        Kelas kelas = new Kelas();
        kelas.setDosen(new Dosen());
        kelas.setMatakuliah(new MataKuliah());
        model.addAttribute("kelas", kelas);
        model.addAttribute("dosenList", dosenRepository.findAll());
        model.addAttribute("matakuliahList", mataKuliahRepository.findAll());
        return "admin/kelas-form";
    }

    @PostMapping("/kelas/simpan")
    public String simpanKelas(@ModelAttribute Kelas kelas) {
        kelasRepository.save(kelas);
        return "redirect:/admin/kelas";
    }

    @GetMapping("/kelas/edit/{id}")
    public String formEditKelas(@PathVariable Long id, Model model) {
        Kelas kelas = kelasRepository.findById(id).orElseThrow();
        model.addAttribute("kelas", kelas);
        model.addAttribute("dosenList", dosenRepository.findAll());
        model.addAttribute("matakuliahList", mataKuliahRepository.findAll());
        return "admin/kelas-form";
    }

    @GetMapping("/kelas/hapus/{id}")
    public String hapusKelas(@PathVariable Long id) {
        kelasRepository.deleteById(id);
        return "redirect:/admin/kelas";
    }

    @GetMapping("/kelas/{id}")
    public String detailKelas(@PathVariable Long id, Model model) {
        Kelas kelas = kelasRepository.findById(id).orElseThrow();
        List<KelasMahasiswa> peserta = kelasMahasiswaRepository.findByKelas(kelas);
        model.addAttribute("kelas", kelas);
        model.addAttribute("peserta", peserta);
        model.addAttribute("mahasiswaList", mahasiswaRepository.findAll());
        return "admin/kelas-detail";
    }

    @PostMapping("/kelas/{id}/hapus-peserta")
    public String hapusPeserta(@PathVariable Long id, @RequestParam Long pesertaId) {
        kelasMahasiswaRepository.deleteById(pesertaId);
        return "redirect:/admin/kelas/" + id;
    }

    @GetMapping("/kelas/atur-pengajar")
    public String aturPengajar(Model model) {
        List<Kelas> kelasTanpaDosen = kelasRepository.findByDosenIsNull();
        List<Dosen> dosenList = dosenRepository.findAll();

        model.addAttribute("kelasTanpaDosen", kelasTanpaDosen);
        model.addAttribute("dosenList", dosenList);
        return "admin/atur-pengajar";
    }

    @PostMapping("/kelas/atur-pengajar")
    public String simpanPengajar(@RequestParam Long kelasId, @RequestParam Long dosenId, RedirectAttributes redirectAttributes) {
        Kelas kelas = kelasRepository.findById(kelasId).orElseThrow();
        
        if (kelas.getDosen() != null) {
            redirectAttributes.addFlashAttribute("error", "Kelas ini sudah memiliki pengajar.");
            return "redirect:/admin/kelas/atur-pengajar";
        }

        Dosen dosen = dosenRepository.findById(dosenId).orElseThrow();
        kelas.setDosen(dosen);
        kelasRepository.save(kelas);

        redirectAttributes.addFlashAttribute("success", "Pengajar berhasil di-assign ke kelas.");
        return "redirect:/admin/kelas";
    }

    @PostMapping("/kelas/{id}/tambah-peserta")
    public String tambahPeserta(@PathVariable Long id,
                                @RequestParam Long mahasiswaId,
                                RedirectAttributes redirectAttributes) {
        Kelas kelas = kelasRepository.findById(id).orElseThrow();
        Mahasiswa mahasiswa = mahasiswaRepository.findById(mahasiswaId).orElseThrow();

        boolean sudahTerdaftar = kelasMahasiswaRepository.existsByMahasiswaAndKelas(mahasiswa, kelas);
        if (sudahTerdaftar) {
            redirectAttributes.addFlashAttribute("error", "Mahasiswa sudah terdaftar di kelas ini.");
            return "redirect:/admin/kelas/" + id;
        }

        KelasMahasiswa km = new KelasMahasiswa();
        km.setKelas(kelas);
        km.setMahasiswa(mahasiswa);
        kelasMahasiswaRepository.save(km);

        redirectAttributes.addFlashAttribute("success", "Mahasiswa berhasil ditambahkan ke kelas.");
        return "redirect:/admin/kelas/" + id;
    }

    // =============================
    // Manajemen Dosen (CRUD)
    // =============================
    @GetMapping("/dosen")
    public String listDosen(Model model) {
        model.addAttribute("daftarDosen", dosenRepository.findAll());
        return "admin/dosen-list";
    }

    @GetMapping("/dosen/tambah")
    public String formTambahDosen(Model model) {
        model.addAttribute("dosen", new Dosen());
        return "admin/dosen-form";
    }

    @PostMapping("/dosen/simpan")
    public String simpanDosen(@ModelAttribute Dosen dosen) {
        dosenRepository.save(dosen);
        return "redirect:/admin/dosen";
    }

    @GetMapping("/dosen/edit/{id}")
    public String editDosen(@PathVariable Long id, Model model) {
        Dosen dosen = dosenRepository.findById(id).orElseThrow();
        model.addAttribute("dosen", dosen);
        return "admin/dosen-form";
    }

    @GetMapping("/dosen/hapus/{id}")
    public String hapusDosen(@PathVariable Long id) {
        dosenRepository.deleteById(id);
        return "redirect:/admin/dosen";
    }

        // =============================
    // Manajemen Mahasiswa (CRUD)
    // =============================
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/mahasiswa")
    public String listMahasiswa(Model model) {
        model.addAttribute("mahasiswaList", mahasiswaRepository.findAll());
        return "admin/mahasiswa-list";
    }

    @GetMapping("/mahasiswa/tambah")
    public String formTambahMahasiswa(Model model) {
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setUser(new User()); // buat init field user
        model.addAttribute("mahasiswa", mahasiswa);
        return "admin/mahasiswa-form";
    }

    @PostMapping("/mahasiswa/simpan")
    public String simpanMahasiswa(@ModelAttribute Mahasiswa mahasiswa) {
        User user = mahasiswa.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("MAHASISWA");
        userRepository.save(user);
        mahasiswaRepository.save(mahasiswa);
        return "redirect:/admin/mahasiswa";
    }

    @GetMapping("/mahasiswa/edit/{id}")
    public String formEditMahasiswa(@PathVariable Long id, Model model) {
        Mahasiswa mahasiswa = mahasiswaRepository.findById(id).orElseThrow();
        model.addAttribute("mahasiswa", mahasiswa);
        return "admin/mahasiswa-form";
    }

    @GetMapping("/mahasiswa/hapus/{id}")
    public String hapusMahasiswa(@PathVariable Long id) {
        mahasiswaRepository.deleteById(id);
        return "redirect:/admin/mahasiswa";
    }



}
