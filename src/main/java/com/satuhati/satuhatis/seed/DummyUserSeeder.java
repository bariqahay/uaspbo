package com.satuhati.satuhatis.seed;

import com.satuhati.satuhatis.model.*;
import com.satuhati.satuhatis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DummyUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DosenRepository dosenRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final ProdiRepository prodiRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // === ADMIN ===
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("pwadmin"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }

        // Ambil Prodi
        Optional<Prodi> tiOpt = prodiRepository.findById(1L);
        Optional<Prodi> siOpt = prodiRepository.findById(2L);

        // === DOSEN 1 ===
        if (userRepository.findByUsername("dosen1").isEmpty() && tiOpt.isPresent()) {
            User user = new User();
            user.setUsername("dosen1");
            user.setPassword(passwordEncoder.encode("pwdosen1"));
            user.setRole("DOSEN");
            user = userRepository.save(user);

            Dosen dosen = new Dosen();
            dosen.setUser(user);
            dosen.setNip("D001");
            dosen.setNama("Dr. Asep Setiawan");
            dosen.setPassword(user.getPassword());
            dosen.setProdi(tiOpt.get());
            dosenRepository.save(dosen);
        }

        // === DOSEN 2 ===
        if (userRepository.findByUsername("dosen2").isEmpty() && siOpt.isPresent()) {
            User user = new User();
            user.setUsername("dosen2");
            user.setPassword(passwordEncoder.encode("pwdosen2"));
            user.setRole("DOSEN");
            user = userRepository.save(user);

            Dosen dosen = new Dosen();
            dosen.setUser(user);
            dosen.setNip("D002");
            dosen.setNama("Dr. Budi Santosa");
            dosen.setPassword(user.getPassword());
            dosen.setProdi(siOpt.get());
            dosenRepository.save(dosen);
        }

        // === MAHASISWA 1 ===
        if (userRepository.findByUsername("mahasiswa1").isEmpty() && tiOpt.isPresent()) {
            User user = new User();
            user.setUsername("mahasiswa1");
            user.setPassword(passwordEncoder.encode("pwmahasiswa1"));
            user.setRole("MAHASISWA");
            user = userRepository.save(user);

            Mahasiswa mhs = new Mahasiswa();
            mhs.setUser(user);
            mhs.setNim("M001");
            mhs.setNama("Andi Wijaya");
            mhs.setProdi(tiOpt.get());
            mahasiswaRepository.save(mhs);
        }

        // === MAHASISWA 2 ===
        if (userRepository.findByUsername("mahasiswa2").isEmpty() && tiOpt.isPresent()) {
            User user = new User();
            user.setUsername("mahasiswa2");
            user.setPassword(passwordEncoder.encode("pwmahasiswa2"));
            user.setRole("MAHASISWA");
            user = userRepository.save(user);

            Mahasiswa mhs = new Mahasiswa();
            mhs.setUser(user);
            mhs.setNim("M002");
            mhs.setNama("Fajar Rahman");
            mhs.setProdi(tiOpt.get());
            mahasiswaRepository.save(mhs);
        }

        // === MAHASISWA 3 ===
        if (userRepository.findByUsername("mahasiswa3").isEmpty() && siOpt.isPresent()) {
            User user = new User();
            user.setUsername("mahasiswa3");
            user.setPassword(passwordEncoder.encode("pwmahasiswa3"));
            user.setRole("MAHASISWA");
            user = userRepository.save(user);

            Mahasiswa mhs = new Mahasiswa();
            mhs.setUser(user);
            mhs.setNim("M003");
            mhs.setNama("Sinta Aulia");
            mhs.setProdi(siOpt.get());
            mahasiswaRepository.save(mhs);
        }
    }
}
