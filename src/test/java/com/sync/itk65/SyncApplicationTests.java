package com.sync.itk65;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sync.itk65.entity.CuDan;
import com.sync.itk65.entity.NguoiDung;
import com.sync.itk65.repository.CanHoRepository;
import com.sync.itk65.repository.CuDanRepository;
import com.sync.itk65.repository.NguoiDungRepository;
import com.sync.itk65.service.CuDanService;

@ExtendWith(MockitoExtension.class)
class SyncApplicationTests {

    @InjectMocks
    private CuDanService cuDanService;

    @Mock
    private CuDanRepository cuDanRepository;

    @Mock
    private CanHoRepository canHoRepository;

    @Mock
    private NguoiDungRepository nguoiDungRepository;

    @Test
    void testLuuCuDan_TrungTenDangNhap_ThrowException() {
        CuDan newCuDan = new CuDan();
        newCuDan.setTenDangNhap("trungten");
        newCuDan.setCccd("123456789012");

        NguoiDung existingUser = new NguoiDung();
        existingUser.setId(10L);
        existingUser.setTenDangNhap("trungten");

        when(nguoiDungRepository.findByTenDangNhap("trungten")).thenReturn(existingUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cuDanService.luuCuDan(newCuDan);
        });

        assertEquals("Tên đăng nhập: 'trungten' đã được sử dụng!", exception.getMessage());
        verify(cuDanRepository, never()).save(any());
    }

    @Test
    void testLuuCuDan_TenDangNhapHopLe_ThanhCong() {
        CuDan newCuDan = new CuDan();
        newCuDan.setTenDangNhap("hople");
        newCuDan.setCccd("123456789012");

        when(nguoiDungRepository.findByTenDangNhap("hople")).thenReturn(null);
        when(cuDanRepository.findAll()).thenReturn(new ArrayList<>());

        cuDanService.luuCuDan(newCuDan);

        verify(cuDanRepository, times(1)).save(newCuDan);
    }
}

