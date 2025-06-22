package com.satuhati.satuhatis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.satuhati.satuhatis.model.Dosen;
import com.satuhati.satuhatis.model.DosenMatakuliah;
import com.satuhati.satuhatis.model.MataKuliah;

import java.util.List;

public interface DosenMatakuliahRepository extends JpaRepository<DosenMatakuliah, Long> {
    List<DosenMatakuliah> findByMatakuliah_Id(Long matakuliahId);
    boolean existsByDosenAndMatakuliah(Dosen dosen, MataKuliah matakuliah);

}
