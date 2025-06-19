package com.satuhati.satuhatis.dto;

public class KelasDTO {
    private Long id;
    private String namaKelas;
    private int kapasitas;
    private DosenDTO dosen; // bisa null kalau belum ada dosennya
    private MatakuliahDTO matakuliah;
}