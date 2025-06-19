package com.satuhati.satuhatis.service;

import com.satuhati.satuhatis.model.Dosen;
import com.satuhati.satuhatis.repository.DosenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DosenService {

    private final DosenRepository dosenRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Dosen> findAll() {
        return dosenRepository.findAll();
    }

    public Optional<Dosen> findById(Long id) {
        return dosenRepository.findById(id);
    }

    public Optional<Dosen> findByNip(String nip) {
        return dosenRepository.findByNip(nip);
    }

    public Optional<Dosen> findByUsername(String username) {
        return dosenRepository.findByUser_Username(username);
    }

    public Dosen save(Dosen dosen) {
        if (dosen.getPassword() != null && !dosen.getPassword().isEmpty()) {
            dosen.setPassword(passwordEncoder.encode(dosen.getPassword()));
        }
        return dosenRepository.save(dosen);
    }

    public void deleteById(Long id) {
        dosenRepository.deleteById(id);
    }

    public boolean existsByNip(String nip) {
        return dosenRepository.existsByNip(nip);
    }
}
