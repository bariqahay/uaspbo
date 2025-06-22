package com.satuhati.satuhatis.seed;

import com.satuhati.satuhatis.model.*;
import com.satuhati.satuhatis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FullDatabaseSeeder implements CommandLineRunner {

    private final FakultasRepository fakultasRepository;
    private final ProdiRepository prodiRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final UserRepository userRepository;
    private final DosenRepository dosenRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 1. Seed Fakultas
        Fakultas fakultas = fakultasRepository.findByNama("Fakultas Ilmu Komputer")
                .orElseGet(() -> fakultasRepository.save(Fakultas.builder().nama("Fakultas Ilmu Komputer").build()));

        // 2. Seed Prodi
        Prodi ti = prodiRepository.findByNama("Teknik Informatika")
                .orElseGet(() -> prodiRepository.save(Prodi.builder().nama("Teknik Informatika").fakultas(fakultas).build()));

        Prodi si = prodiRepository.findByNama("Sistem Informasi")
                .orElseGet(() -> prodiRepository.save(Prodi.builder().nama("Sistem Informasi").fakultas(fakultas).build()));

        // 3. Seed MataKuliah
        List<MataKuliah> tiCourses = Arrays.asList(
            new MataKuliah(null, "IF101", "Algoritma dan Pemrograman", 3, ti),
            new MataKuliah(null, "IF102", "Struktur Data", 3, ti),
            new MataKuliah(null, "IF103", "Pemrograman Web", 3, ti),
            new MataKuliah(null, "IF104", "Sistem Operasi", 3, ti)
        );

        List<MataKuliah> siCourses = Arrays.asList(
            new MataKuliah(null, "SI201", "Sistem Informasi Manajemen", 3, si),
            new MataKuliah(null, "SI202", "Basis Data", 3, si),
            new MataKuliah(null, "SI203", "Analisis & Perancangan SI", 3, si),
            new MataKuliah(null, "SI204", "Audit Sistem Informasi", 3, si)
        );

        // Save only if not exists
        tiCourses.forEach(mk -> {
            mataKuliahRepository.findByKode(mk.getKode())
                .orElseGet(() -> mataKuliahRepository.save(mk));
        });

        siCourses.forEach(mk -> {
            mataKuliahRepository.findByKode(mk.getKode())
                .orElseGet(() -> mataKuliahRepository.save(mk));
        });


        // 4. Seed User & Dosen
        if (userRepository.findByUsername("dosen1").isEmpty()) {
            User user = userRepository.save(new User("dosen1", passwordEncoder.encode("pwdosen1"), "DOSEN"));
            Dosen dosen = Dosen.builder().nip("D001").nama("Dr. Asep Setiawan").user(user).prodi(ti).build();
            dosenRepository.save(dosen);
        }

        if (userRepository.findByUsername("dosen2").isEmpty()) {
            User user = userRepository.save(new User("dosen2", passwordEncoder.encode("pwdosen2"), "DOSEN"));
            Dosen dosen = Dosen.builder().nip("D002").nama("Dr. Budi Santosa").user(user).prodi(si).build();
            dosenRepository.save(dosen);
        }
        if (userRepository.findByUsername("dosen3").isEmpty()) {
            User user = userRepository.save(new User("dosen3", passwordEncoder.encode("pwdosen3"), "DOSEN"));
            Dosen dosen = Dosen.builder()
                    .nip("D003")
                    .nama("Dr. Chandra Wijaya")
                    .user(user)
                    .prodi(ti) // tetap ke prodi TI
                    .build();
            dosenRepository.save(dosen);
        }

        // 5. Seed User & Mahasiswa
        if (userRepository.findByUsername("mahasiswa1").isEmpty()) {
            User user = userRepository.save(new User("mahasiswa1", passwordEncoder.encode("pwmahasiswa1"), "MAHASISWA"));
            Mahasiswa mhs = Mahasiswa.builder().nim("M001").nama("Andi Wijaya").user(user).prodi(ti).build();
            mahasiswaRepository.save(mhs);
        }

        if (userRepository.findByUsername("mahasiswa2").isEmpty()) {
            User user = userRepository.save(new User("mahasiswa2", passwordEncoder.encode("pwmahasiswa2"), "MAHASISWA"));
            Mahasiswa mhs = Mahasiswa.builder().nim("M002").nama("Fajar Rahman").user(user).prodi(ti).build();
            mahasiswaRepository.save(mhs);
        }

        if (userRepository.findByUsername("mahasiswa3").isEmpty()) {
            User user = userRepository.save(new User("mahasiswa3", passwordEncoder.encode("pwmahasiswa3"), "MAHASISWA"));
            Mahasiswa mhs = Mahasiswa.builder().nim("M003").nama("Sinta Aulia").user(user).prodi(si).build();
            mahasiswaRepository.save(mhs);
        }

        // 6. Seed Admin
        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(new User("admin", passwordEncoder.encode("pwadmin"), "ADMIN"));
        }
    }
}
