package com.satuhati.satuhatis.repository;

import com.satuhati.satuhatis.model.Kelas;
import com.satuhati.satuhatis.model.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KelasRepository extends JpaRepository<Kelas, Long> {

    List<Kelas> findByDosen(Dosen dosen);

    List<Kelas> findByDosenIsNull();

    List<Kelas> findByDosen_Id(Long dosenId); // ⬅️ Tambahin ini

    List<Kelas> findByMatakuliah_ProdiList_Id(Long prodiId);
    List<Kelas> findByMatakuliah_Id(Long matakuliahId);

    boolean existsByNamaKelas(String namaKelas);
}
