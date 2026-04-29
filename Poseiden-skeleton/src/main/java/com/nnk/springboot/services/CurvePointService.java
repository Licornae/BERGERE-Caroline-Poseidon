package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;

import java.util.List;
import java.util.Optional;

public interface CurvePointService {
    List<CurvePoint> findAll();

    Optional<CurvePoint> findById(int id);

    CurvePoint save(CurvePoint curvePoint);

    void deleteById(Integer id);
}
