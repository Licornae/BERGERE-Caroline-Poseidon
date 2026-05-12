package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;

import java.util.List;

/**
 * Service interface for managing operations related to the CurvePoint entity.
 * Provides methods for CRUD (Create, Read, Update, Delete) operations.
 */
public interface CurvePointService {
    List<CurvePoint> findAll();

    CurvePoint findById(int id);

    CurvePoint save(CurvePoint curvePoint);

    void deleteById(Integer id);
}
