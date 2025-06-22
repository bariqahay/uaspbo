// AdminController.java
package com.satuhati.satuhatis.controller;

import com.satuhati.satuhatis.dto.DosenDTO;
import com.satuhati.satuhatis.dto.KelasSimpleDTO;
import com.satuhati.satuhatis.model.*;
import com.satuhati.satuhatis.repository.*;
import com.satuhati.satuhatis.service.DosenService;
import com.satuhati.satuhatis.service.MahasiswaService;

import lombok.RequiredArgsConstructor;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MahasiswaService mahasiswaService;
    private final ProdiRepository prodiRepository;
    private final FakultasRepository fakultasRepository;
    private final KelasRepository kelasRepository;
    private final DosenRepository dosenRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final KelasMahasiswaRepository kelasMahasiswaRepository;
    private final DosenMatakuliahRepository dosenMatakuliahRepository; // ✅
    private final DosenService dosenService;


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
        model.addAttribute("fakultasList", fakultasRepository.findAll());
        model.addAttribute("prodiList", prodiRepository.findAll());
        return "admin/kelas-list";
    }

    @GetMapping("/kelas/tambah")
    public String formTambahKelas(Model model) {
        Kelas kelas = new Kelas();
        kelas.setDosen(new Dosen());
        kelas.setMatakuliah(new MataKuliah());

        model.addAttribute("kelas", kelas);
        model.addAttribute("matakuliahList", mataKuliahRepository.findAll());
        model.addAttribute("fakultasList", fakultasRepository.findAll());
        model.addAttribute("prodiList", prodiRepository.findAll()); // atau List.of() kalau mau kosong dulu
        return "admin/kelas-form";
    }

    @PostMapping("/kelas/simpan")
    public String simpanKelas(@ModelAttribute Kelas kelas) {
        if (kelas.getFakultas() == null || kelas.getProdi() == null) {
            // bisa tambahin validasi error di sini kalau mau
            return "redirect:/admin/kelas/tambah";
        }

        if (kelas.getId() != null) {
            Kelas existingKelas = kelasRepository.findById(kelas.getId()).orElseThrow();
            existingKelas.setNamaKelas(kelas.getNamaKelas());
            existingKelas.setMatakuliah(kelas.getMatakuliah());
            existingKelas.setKapasitas(kelas.getKapasitas());
            existingKelas.setRuangan(kelas.getRuangan());
            existingKelas.setFakultas(kelas.getFakultas());
            existingKelas.setProdi(kelas.getProdi());

            kelasRepository.save(existingKelas);
        } else {
            kelasRepository.save(kelas);
        }

        return "redirect:/admin/kelas";
    }

    // Tambahkan ini di bawah @PostMapping("/kelas/simpan")
    @GetMapping("/kelas/simpan")
    public String handleSalahRoute() {
        // Redirect agar akses GET ke /kelas/simpan tidak dianggap /kelas/{id}
        return "redirect:/admin/kelas";
    }


    @GetMapping("/kelas/edit/{id}")
    public String formEditKelas(@PathVariable Long id, Model model) {
        Kelas kelas = kelasRepository.findById(id).orElseThrow();
        model.addAttribute("kelas", kelas);
        model.addAttribute("matakuliahList", mataKuliahRepository.findAll());

        List<Dosen> dosenList = List.of(); // default kosong

        if (kelas.getMatakuliah() != null && kelas.getMatakuliah().getProdi() != null) {
            Long prodiId = kelas.getMatakuliah().getProdi().getId();
            dosenList = dosenRepository.findByProdi_Id(prodiId);
        }

        model.addAttribute("dosenList", dosenList);
        return "admin/kelas-form";
    }
    @GetMapping("/kelas/hapus/{id}")
    public String hapusKelas(@PathVariable Long id) {
        kelasRepository.deleteById(id);
        return "redirect:/admin/kelas";
    }

    // Revisi method detailKelas:
    @GetMapping("/kelas/{id}")
    public String detailKelas(@PathVariable String id, Model model) {
        Long kelasId;

        try {
            kelasId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            // Kalau id gak valid (misal: "simpan"), langsung return 400
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID kelas harus berupa angka");
        }

        Kelas kelas = kelasRepository.findById(kelasId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kelas tidak ditemukan"));

        List<KelasMahasiswa> peserta = kelasMahasiswaRepository.findByKelas(kelas);
        model.addAttribute("kelas", kelas);
        model.addAttribute("peserta", peserta);
        model.addAttribute("mahasiswaList", mahasiswaRepository.findAll());

        return "admin/kelas-detail";
    }

    @GetMapping("/dosen/by-matakuliah/{id}")
    @ResponseBody
    public List<Dosen> getDosenByMatkul(@PathVariable("id") Long matakuliahId) {
        List<DosenMatakuliah> list = dosenMatakuliahRepository.findByMatakuliah_Id(matakuliahId);

        return list.stream()
                .map(DosenMatakuliah::getDosen)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }



    @PostMapping("/kelas/{id}/hapus-peserta")
    public String hapusPeserta(@PathVariable Long id, @RequestParam Long pesertaId) {
        kelasMahasiswaRepository.deleteById(pesertaId);
        return "redirect:/admin/kelas/" + id;
    }

    @GetMapping("/kelas/atur-pengajar")
    public String aturPengajar(Model model) {
        List<Kelas> kelasTanpaDosen = kelasRepository.findByDosenIsNull()
            .stream()
            .filter(k -> k.getMatakuliah() != null && k.getMatakuliah().getProdi() != null)
            .toList();

        model.addAttribute("kelasTanpaDosen", kelasTanpaDosen);
        model.addAttribute("dosenList", List.of()); // kosong dulu, isi pas select kelas via JS
        model.addAttribute("fakultasList", fakultasRepository.findAll()); // ← ini dia
        return "admin/atur-pengajar";
    }

    @PostMapping("/kelas/atur-pengajar")
    public String simpanPengajar(@RequestParam Long kelasId, @RequestParam Long dosenId, RedirectAttributes redirectAttributes) {
        Kelas kelas = kelasRepository.findById(kelasId).orElseThrow();
        
        if (kelas.getDosen() != null) {
            redirectAttributes.addFlashAttribute("error", "Kelas ini sudah memiliki pengajar.");
            return "redirect:/admin/kelas/atur-pengajar";
        }

        if (kelas.getMatakuliah() == null || kelas.getMatakuliah().getProdi() == null) {
            redirectAttributes.addFlashAttribute("error", "Kelas belum memiliki matakuliah atau prodi.");
            return "redirect:/admin/kelas/atur-pengajar";
        }

        Dosen dosen = dosenRepository.findById(dosenId).orElseThrow();

        if (!Objects.equals(dosen.getProdi().getId(), kelas.getMatakuliah().getProdi().getId())) {
            redirectAttributes.addFlashAttribute("error", "Dosen dan kelas harus dari prodi yang sama.");
            return "redirect:/admin/kelas/atur-pengajar";
        }

        kelas.setDosen(dosen);
        kelasRepository.save(kelas);
        redirectAttributes.addFlashAttribute("success", "Pengajar berhasil di-assign ke kelas.");
        return "redirect:/admin/kelas";
    }

    @GetMapping("/kelas/{id}/dosen-available")
    @ResponseBody
    public List<DosenDTO> getDosenByKelas(@PathVariable Long id) {
        Kelas kelas = kelasRepository.findById(id).orElseThrow();
        if (kelas.getMatakuliah() == null || kelas.getMatakuliah().getProdi() == null) {
            return List.of();
        }

        Long prodiId = kelas.getMatakuliah().getProdi().getId();
        List<Dosen> dosenList = dosenRepository.findByProdi_Id(prodiId);

        return dosenList.stream()
                .map(d -> new DosenDTO(d.getId(), d.getNama()))
                .toList();
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

        @GetMapping("/kelas/daftar")
    public String daftarKelasUntukPeserta(Model model) {
        List<Kelas> kelasList = kelasRepository.findAll();
        model.addAttribute("kelasList", kelasList);
        return "admin/kelas-daftar-peserta";
    }

    @GetMapping("/kelas/filter")
    public String filterKelas(
        @RequestParam(required = false) String fakultasId,
        @RequestParam(required = false) String prodiId,
        Model model
    ) {
        List<Kelas> semuaKelas = kelasRepository.findAll();

        final Long fId;
        final Long pId;

        try {
            fId = (fakultasId != null && !fakultasId.isEmpty()) ? Long.parseLong(fakultasId) : null;
            pId = (prodiId != null && !prodiId.isEmpty()) ? Long.parseLong(prodiId) : null;
        } catch (NumberFormatException e) {
            // fallback kalau ada input aneh
            return "redirect:/admin/kelas";
        }

        if (pId != null) {
            semuaKelas = semuaKelas.stream()
                .filter(k -> k.getMatakuliah().getProdi() != null &&
                            k.getMatakuliah().getProdi().getId().equals(pId))
                .toList();
        }

        if (fId != null) {
            semuaKelas = semuaKelas.stream()
                .filter(k -> {
                    var prodi = k.getMatakuliah().getProdi();
                    return prodi != null &&
                        prodi.getFakultas() != null &&
                        prodi.getFakultas().getId().equals(fId);
                })
                .toList();
        }

        List<Prodi> prodiList = (fId != null)
            ? prodiRepository.findByFakultas_Id(fId)
            : prodiRepository.findAll();

        model.addAttribute("kelasList", semuaKelas);
        model.addAttribute("prodiList", prodiList);
        model.addAttribute("fakultasList", fakultasRepository.findAll());
        model.addAttribute("selectedFakultasId", fId);
        model.addAttribute("selectedProdiId", pId);

        return "admin/kelas-list";
    }

    // =============================
    // Manajemen Dosen (CRUD)
    // =============================
@GetMapping("/dosen")
public String listDosen(Model model) {
    model.addAttribute("daftarDosen", dosenService.findAll());
    return "admin/dosen-list";
}

@GetMapping("/dosen/tambah")
public String formTambahDosen(Model model) {
    model.addAttribute("dosen", new Dosen());
    model.addAttribute("prodiList", prodiRepository.findAll());
    return "admin/dosen-form";
}

@PostMapping("/dosen/simpan")
public String simpanDosen(@ModelAttribute Dosen dosen) {
    if (dosen.getId() == null) {
        // CREATE baru
        if (dosenService.usernameExists(dosen.getUser().getUsername())) {
            return "redirect:/admin/dosen/tambah?error=username_taken";
        }
        dosenService.save(dosen);
    } else {
        // UPDATE
        dosenService.update(dosen);
    }
    return "redirect:/admin/dosen";
}

@GetMapping("/dosen/edit/{id}")
public String editDosen(@PathVariable Long id, Model model) {
    Dosen dosen = dosenService.findById(id).orElseThrow();
    model.addAttribute("dosen", dosen);
    model.addAttribute("prodiList", prodiRepository.findAll());
    return "admin/dosen-form";
}

@GetMapping("/dosen/hapus/{id}")
public String hapusDosen(@PathVariable Long id) {
    dosenService.deleteById(id);
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
        model.addAttribute("prodiList", prodiRepository.findAll());
        return "admin/mahasiswa-form";
    }

    @PostMapping("/mahasiswa/simpan")
    public String simpanMahasiswa(@ModelAttribute Mahasiswa mahasiswa) {
        mahasiswaService.save(mahasiswa);
        return "redirect:/admin/mahasiswa";
    }

    @GetMapping("/mahasiswa/edit/{id}")
    public String formEditMahasiswa(@PathVariable Long id, Model model) {
        Mahasiswa mahasiswa = mahasiswaRepository.findById(id).orElseThrow();
        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("prodiList", prodiRepository.findAll());
        return "admin/mahasiswa-form";
    }

    @GetMapping("/mahasiswa/hapus/{id}")
    public String hapusMahasiswa(@PathVariable Long id) {
        mahasiswaRepository.findById(id).ifPresent(m -> {
            User user = m.getUser();
            mahasiswaRepository.deleteById(id);
            if (user != null) userRepository.delete(user);
        });
        return "redirect:/admin/mahasiswa";
    }



}
