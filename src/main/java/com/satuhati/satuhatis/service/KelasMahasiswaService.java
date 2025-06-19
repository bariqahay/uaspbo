package com.satuhati.satuhatis.service;

import com.satuhati.satuhatis.model.KelasMahasiswa;
import com.satuhati.satuhatis.model.Mahasiswa;
import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.repository.KelasMahasiswaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KelasMahasiswaService {

    private final KelasMahasiswaRepository repo;

    public KelasMahasiswa save(KelasMahasiswa km) {
        return repo.save(km);
    }

    public List<KelasMahasiswa> findByMahasiswa(Mahasiswa mhs) {
        return repo.findByMahasiswa(mhs);
    }

    public boolean isAlreadyRegistered(Mahasiswa mhs, Kelas kelas) {
        return repo.existsByMahasiswaAndKelas(mhs, kelas);
    }
}
