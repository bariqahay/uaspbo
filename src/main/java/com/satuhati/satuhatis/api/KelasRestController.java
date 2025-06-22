package com.satuhati.satuhatis.api;

import com.satuhati.satuhatis.dto.KelasSimpleDTO;
import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.repository.KelasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kelas")
@RequiredArgsConstructor
@CrossOrigin // Penting untuk fetch dari browser
public class KelasRestController {

    private final KelasRepository kelasRepository;

    @GetMapping("/by-prodi/{prodiId}")
    public List<KelasSimpleDTO> getKelasByProdi(@PathVariable Long prodiId) {
        return kelasRepository.findByDosenIsNull().stream()
                .filter(k -> k.getMatakuliah() != null &&
                             k.getMatakuliah().getProdi() != null &&
                             k.getMatakuliah().getProdi().getId().equals(prodiId))
                .map(k -> new KelasSimpleDTO(k.getId(), k.getNamaKelas()))
                .toList();
    }
}
