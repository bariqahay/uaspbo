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
    private final com.satuhati.satuhatis.repository.UserRepository userRepository;

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
        var user = mahasiswa.getUser();

        if (user == null) throw new IllegalArgumentException("User tidak boleh null");

        if (user.getId() == null) {
            // CREATE
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("MAHASISWA");
            user = userRepository.save(user);
        } else {
            // UPDATE
            var existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

            existingUser.setUsername(user.getUsername());

            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            user = userRepository.save(existingUser);
        }

        mahasiswa.setUser(user);
        return mahasiswaRepository.save(mahasiswa);
    }


    public void deleteById(Long id) {
        mahasiswaRepository.findById(id).ifPresent(m -> {
            var user = m.getUser();
            mahasiswaRepository.deleteById(id);
            if (user != null) {
                userRepository.delete(user);
            }
        });
    }

    public boolean existsByNim(String nim) {
        return mahasiswaRepository.existsByNim(nim);
    }
}

