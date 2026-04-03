package com.sync.itk65.service;

import com.sync.itk65.entity.PhanAnh;
import com.sync.itk65.repository.PhanAnhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhanAnhService {

    @Autowired
    private PhanAnhRepository phanAnhRepository;

    public List<PhanAnh> findAll() {
        return phanAnhRepository.findAll();
    }

    public PhanAnh findById(Long id) {
        return phanAnhRepository.findById(id).orElse(null);
    }

    public PhanAnh save(PhanAnh phanAnh) {
        return phanAnhRepository.save(phanAnh);
    }

    public void deleteById(Long id) {
        phanAnhRepository.deleteById(id);
    }
}