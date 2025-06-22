package com.satuhati.satuhatis.repository;

import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.model.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KelasRepository extends JpaRepository<Kelas, Long> {

    // Untuk ambil semua kelas yang diajar dosen tertentu
    List<Kelas> findByDosen(Dosen dosen);

    // Untuk daftar kelas yang belum ada dosennya
    List<Kelas> findByDosenIsNull();

    // Ambil kelas berdasarkan ID dosen
    List<Kelas> findByDosen_Id(Long dosenId);

    // Ambil kelas berdasarkan ID prodi
    List<Kelas> findByMatakuliah_Prodi_Id(Long id);

    // Ambil kelas berdasarkan ID matakuliah
    List<Kelas> findByMatakuliah_Id(Long matakuliahId);

    // Cari kelas berdasarkan nama (untuk validasi)
    Optional<Kelas> findByNamaKelas(String namaKelas);

    // Cek kalau nama kelas sudah ada
    boolean existsByNamaKelas(String namaKelas);

    // 🔥 QUERY DINAMIS UNTUK FILTER (Fakultas, Prodi, Matkul)
    @Query("""
        SELECT k FROM Kelas k
        WHERE (:fakultasId IS NULL OR k.matakuliah.prodi.fakultas.id = :fakultasId)
          AND (:prodiId IS NULL OR k.matakuliah.prodi.id = :prodiId)
          AND (:matakuliahId IS NULL OR k.matakuliah.id = :matakuliahId)
    """)
    List<Kelas> findFilteredKelas(
        @Param("fakultasId") Long fakultasId,
        @Param("prodiId") Long prodiId,
        @Param("matakuliahId") Long matakuliahId
    );
}
