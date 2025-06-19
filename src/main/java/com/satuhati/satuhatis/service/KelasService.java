package com.satuhati.satuhatis.service;

import com.satuhati.satuhatis.model.Dosen;
import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.repository.KelasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KelasService {

    private final KelasRepository kelasRepository;

    public List<Kelas> findAll() {
        return kelasRepository.findAll();
    }

    public Optional<Kelas> findById(Long id) {
        return kelasRepository.findById(id);
    }

    public List<Kelas> findByDosenId(Long dosenId) {
        return kelasRepository.findByDosen_Id(dosenId);
    }

    public List<Kelas> findByDosen(Dosen dosen) {
        return kelasRepository.findByDosen(dosen);
    }

    public List<Kelas> findByDosenIsNull() {
        return kelasRepository.findByDosenIsNull();
    }

    public Kelas save(Kelas kelas) {
        return kelasRepository.save(kelas);
    }

    public void deleteById(Long id) {
        kelasRepository.deleteById(id);
    }

    public boolean existsByNamaKelas(String namaKelas) {
        return kelasRepository.existsByNamaKelas(namaKelas);
    }
}
