package com.satuhati.satuhatis.repository;

import com.satuhati.satuhatis.model.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DosenRepository extends JpaRepository<Dosen, Long> {

    Optional<Dosen> findByNip(String nip);

    Optional<Dosen> findByUser_Id(Long userId);

    Optional<Dosen> findByUser_Username(String username);

    boolean existsByNip(String nip);
}
