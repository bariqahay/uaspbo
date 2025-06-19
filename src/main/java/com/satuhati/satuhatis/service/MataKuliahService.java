package com.satuhati.satuhatis.service;

import com.satuhati.satuhatis.model.MataKuliah;
import com.satuhati.satuhatis.repository.MataKuliahRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MataKuliahService {

    private final MataKuliahRepository mataKuliahRepository;

    public List<MataKuliah> findAll() {
        return mataKuliahRepository.findAll();
    }

    public Optional<MataKuliah> findById(Long id) {
        return mataKuliahRepository.findById(id);
    }

    public Optional<MataKuliah> findByKode(String kode) {
        return mataKuliahRepository.findByKode(kode);
    }

    public MataKuliah save(MataKuliah mk) {
        return mataKuliahRepository.save(mk);
    }

    public void deleteById(Long id) {
        mataKuliahRepository.deleteById(id);
    }

    public boolean existsByKode(String kode) {
        return mataKuliahRepository.existsByKode(kode);
    }
}
