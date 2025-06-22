package com.satuhati.satuhatis.repository;

import com.satuhati.satuhatis.model.MataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MataKuliahRepository extends JpaRepository<MataKuliah, Long> {
    Optional<MataKuliah> findByKode(String kode);
    boolean existsByKode(String kode);
    Optional<MataKuliah> findByNama(String nama);
    List<MataKuliah> findByProdi_Id(Long prodiId);
}
