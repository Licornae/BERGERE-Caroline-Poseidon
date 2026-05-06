package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;

import java.util.List;

public interface CurvePointService {
    List<CurvePoint> findAll();

    CurvePoint findById(int id);

    CurvePoint save(CurvePoint curvePoint);

    void deleteById(Integer id);
}
