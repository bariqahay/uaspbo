package com.satuhati.satuhatis.seed;

import com.satuhati.satuhatis.model.*;
import com.satuhati.satuhatis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DosenRepository dosenRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ========== ADMIN ==========
        if (userRepository.findByUsername("admin1").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin1");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }

        // ========== DOSEN ==========
        if (userRepository.findByUsername("dosen1").isEmpty()) {
            User dosenUser = new User();
            dosenUser.setUsername("dosen1");
            dosenUser.setPassword(passwordEncoder.encode("dosen123"));
            dosenUser.setRole("DOSEN");
            dosenUser = userRepository.save(dosenUser); // dapetin ID

            Dosen dosen = new Dosen();
            dosen.setUser(dosenUser);
            dosen.setNama("Bapak Dosen");
            dosen.setNip("D123");
            dosen.setPassword(dosenUser.getPassword()); // optional kalau entity lu butuh
            dosenRepository.save(dosen);
        }

        // ========== MAHASISWA ==========
        if (userRepository.findByUsername("mhs1").isEmpty()) {
            User mhsUser = new User();
            mhsUser.setUsername("mhs1");
            mhsUser.setPassword(passwordEncoder.encode("mahasiswa123"));
            mhsUser.setRole("MAHASISWA");
            mhsUser = userRepository.save(mhsUser);

            Mahasiswa mhs = new Mahasiswa();
            mhs.setUser(mhsUser);
            mhs.setNama("Mahasiswa Satu");
            mhs.setNim("M123");
            mahasiswaRepository.save(mhs);
        }
    }
}
