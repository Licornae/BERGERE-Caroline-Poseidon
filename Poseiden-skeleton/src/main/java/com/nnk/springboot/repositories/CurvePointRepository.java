package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for performing CRUD operations on the CurvePoint entity.
 * Extends JpaRepository to provide standard database operations and
 * query methods for interacting with the "curvepoint" table in the database.
 */
public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {

}
