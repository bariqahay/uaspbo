package com.satuhati.satuhatis.controller;

import com.satuhati.satuhatis.model.MataKuliah;
import com.satuhati.satuhatis.service.MataKuliahService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/matakuliah")
public class MataKuliahController {

    private final MataKuliahService mataKuliahService;

    // === LIST SEMUA MATAKULIAH ===
    @GetMapping
    public String listMatkul(Model model) {
        model.addAttribute("matkulList", mataKuliahService.findAll());
        return "matakuliah/list"; // lo sesuaikan view-nya ya
    }

    // === FORM TAMBAH MATAKULIAH ===
    @GetMapping("/admin/add")
    public String showAddForm(Model model) {
        model.addAttribute("matkul", new MataKuliah());
        return "matakuliah/admin-add"; // view form
    }

    // === SUBMIT TAMBAH / EDIT MATAKULIAH ===
    @PostMapping("/admin/save")
    public String saveMatkul(@ModelAttribute("matkul") MataKuliah matkul) {
        // Validasi kode unik (bisa juga di JS atau pakai exception handler)
        if (!matkul.getId().equals(null) || !mataKuliahService.existsByKode(matkul.getKode())) {
            mataKuliahService.save(matkul);
        }
        return "redirect:/matakuliah";
    }

    // === EDIT MATAKULIAH ===
    @GetMapping("/admin/edit/{id}")
    public String editMatkul(@PathVariable Long id, Model model) {
        Optional<MataKuliah> mk = mataKuliahService.findById(id);
        if (mk.isPresent()) {
            model.addAttribute("matkul", mk.get());
            return "matakuliah/admin-add"; // pake form yang sama
        }
        return "redirect:/matakuliah";
    }

    // === DELETE MATAKULIAH ===
    @GetMapping("/admin/delete/{id}")
    public String deleteMatkul(@PathVariable Long id) {
        mataKuliahService.deleteById(id);
        return "redirect:/matakuliah";
    }

    @GetMapping("/api/matakuliah/by-prodi/{prodiId}")
    @ResponseBody
    public List<MataKuliah> getByProdi(@PathVariable Long prodiId) {
        return mataKuliahService.findByProdiId(prodiId);
    }
}
