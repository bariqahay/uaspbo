package com.satuhati.satuhatis.controller;

import com.satuhati.satuhatis.model.Prodi;
import com.satuhati.satuhatis.repository.ProdiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prodi")
@RequiredArgsConstructor
@CrossOrigin // penting buat akses dari JS di HTML
public class ProdiController {

    private final ProdiRepository prodiRepository;

    @GetMapping("/by-fakultas/{fakultasId}")
    public List<Prodi> getByFakultas(@PathVariable Long fakultasId) {
        return prodiRepository.findByFakultas_Id(fakultasId);
    }
}
