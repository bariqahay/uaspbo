package com.satuhati.satuhatis.service;

import com.satuhati.satuhatis.model.Mahasiswa;
import com.satuhati.satuhatis.repository.MahasiswaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MahasiswaService {

    private final MahasiswaRepository mahasiswaRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Mahasiswa> findAll() {
        return mahasiswaRepository.findAll();
    }

    public Optional<Mahasiswa> findById(Long id) {
        return mahasiswaRepository.findById(id);
    }

    public Optional<Mahasiswa> findByNim(String nim) {
        return mahasiswaRepository.findByNim(nim);
    }

    public Optional<Mahasiswa> findByUsername(String username) {
        return mahasiswaRepository.findByUser_Username(username);
    }

    public Mahasiswa save(Mahasiswa mahasiswa) {
        if (mahasiswa.getUser() != null && mahasiswa.getUser().getPassword() != null) {
            String encoded = passwordEncoder.encode(mahasiswa.getUser().getPassword());
            mahasiswa.getUser().setPassword(encoded);
        }
        return mahasiswaRepository.save(mahasiswa);
    }

    public void deleteById(Long id) {
        mahasiswaRepository.deleteById(id);
    }

    public boolean existsByNim(String nim) {
        return mahasiswaRepository.existsByNim(nim);
    }
}
