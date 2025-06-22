package com.satuhati.satuhatis.repository;

import com.satuhati.satuhatis.model.Prodi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProdiRepository extends JpaRepository<Prodi, Long> {
    List<Prodi> findByFakultas_Id(Long fakultasId);
    Optional<Prodi> findByNama(String nama); // ⬅️ ini juga dibutuhkan
}