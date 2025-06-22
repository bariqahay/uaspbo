package com.satuhati.satuhatis.service;

import com.satuhati.satuhatis.model.Dosen;
import com.satuhati.satuhatis.repository.DosenRepository;
import com.satuhati.satuhatis.repository.UserRepository;
import com.satuhati.satuhatis.model.User;
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
    private final UserRepository userRepository;

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

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public Dosen save(Dosen dosen) {
        User user = dosen.getUser();

        if (user != null && user.getPassword() != null && !user.getPassword().isEmpty()) {
            // encode password hanya jika belum di-encode
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        if (user.getId() == null) {
            user.setRole("DOSEN");
            user = userRepository.save(user);
            dosen.setUser(user);
        }

        return dosenRepository.save(dosen);
    }

    public Dosen update(Dosen dosenBaru) {
        Dosen dosenLama = dosenRepository.findById(dosenBaru.getId()).orElseThrow();

        User userBaru = dosenBaru.getUser();
        User userLama = dosenLama.getUser();

        // Tetap pakai user yang lama
        if (userLama != null) {
            userLama.setUsername(userBaru.getUsername());

            if (userBaru.getPassword() != null && !userBaru.getPassword().isEmpty()) {
                if (!userBaru.getPassword().startsWith("$2a$")) {
                    userLama.setPassword(passwordEncoder.encode(userBaru.getPassword()));
                } else {
                    userLama.setPassword(userBaru.getPassword());
                }
            }

            userRepository.save(userLama);
            dosenBaru.setUser(userLama);
        }

        return dosenRepository.save(dosenBaru);
    }

    public void deleteById(Long id) {
        dosenRepository.findById(id).ifPresent(dosen -> {
            User user = dosen.getUser();
            dosenRepository.deleteById(id);
            if (user != null) {
                userRepository.delete(user);
            }
        });
    }

    public boolean existsByNip(String nip) {
        return dosenRepository.existsByNip(nip);
    }
}
