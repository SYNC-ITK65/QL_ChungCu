package com.sync.itk65.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", 400);
        return mav;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Lỗi dữ liệu: Dữ liệu bị trùng lặp (ví dụ: trùng mã căn hộ, mã tài sản, căn cước công dân) hoặc vi phạm ràng buộc dữ liệu liên quan.");
        mav.addObject("status", 409);
        return mav;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ModelAndView handleValidationException(Exception ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Dữ liệu đầu vào không hợp lệ hoặc sai định dạng.");
        mav.addObject("status", 400);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Hệ thống gặp sự cố ngoài ý muốn: " + ex.getMessage());
        mav.addObject("status", 500);
        return mav;
    }
}
