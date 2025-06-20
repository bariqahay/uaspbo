package com.satuhati.satuhatis.repository;

import com.satuhati.satuhatis.model.Prodi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdiRepository extends JpaRepository<Prodi, Long> {
    List<Prodi> findByFakultas_Id(Long fakultasId); // ini bro!
}
