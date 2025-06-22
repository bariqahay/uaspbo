package com.satuhati.satuhatis.api;

import com.satuhati.satuhatis.model.MataKuliah;
import com.satuhati.satuhatis.service.MataKuliahService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matakuliah")
@RequiredArgsConstructor
@CrossOrigin
public class MataKuliahRestController {

    private final MataKuliahService mataKuliahService;

    @GetMapping("/by-prodi/{prodiId}")
    public List<MataKuliah> getByProdi(@PathVariable Long prodiId) {
        return mataKuliahService.findByProdiId(prodiId);
    }
}
