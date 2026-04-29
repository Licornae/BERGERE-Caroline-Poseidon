package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation class for managing operations related to the CurvePoint entity.
 * This service provides methods to retrieve data from the CurvePointRepository and perform
 * business logic as necessary. It implements the CurvePointService interface.
 */
@Slf4j
@Service
public class CurvePointServiceImpl implements CurvePointService {

    private final CurvePointRepository curvePointRepository;

    @Autowired
    public CurvePointServiceImpl(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    /**
     * Retrieves all CurvePoint entities from the repository.
     *
     * @return a list of all CurvePoint entities
     */
    @Override
    public List<CurvePoint> findAll() {

        log.debug("Finding all CurvePoint entities");

        List<CurvePoint> curvePoints = curvePointRepository.findAll() ;

        log.debug("Found {} CurvePoint entities", curvePoints.size());

        return curvePoints;
    }

    /**
     * Retrieves a CurvePoint entity by its unique identifier.
     * If the entity is not found, an EntityNotFoundException is thrown.
     *
     * @param id the unique identifier of the CurvePoint entity to retrieve
     * @return an Optional containing the CurvePoint entity if found, or empty if not found
     * @throws EntityNotFoundException if the CurvePoint entity with the specified ID does not exist
     */
    @Override
    public Optional<CurvePoint> findById(int id) {
        log.debug("Finding CurvePoint entity by id: {}", id);
        Optional<CurvePoint> curvePoint = curvePointRepository.findById(id);
        if (curvePoint.isEmpty()) {
            String errorMsg = "CurvePoint not found for id: " + id;
            log.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }
        log.debug("Found CurvePoint entity: {}", curvePoint.get());
        return curvePoint;
    }

    /**
     * Saves the provided CurvePoint entity to the repository.
     * If the entity is successfully saved, the persisted entity is returned.
     * In case of any exception during the save operation, a RuntimeException is thrown.
     *
     * @param curvePoint the CurvePoint entity to save
     * @return the saved CurvePoint entity with updated fields, such as the generated ID
     * @throws RuntimeException if an error occurs while saving the entity
     */
    @Override
    public CurvePoint save(CurvePoint curvePoint) {

        log.debug("Saving CurvePoint entity: {}", curvePoint);

        try {
            CurvePoint savedCurvePoint = curvePointRepository.save(curvePoint);
            log.info("Successfully saved CurvePoint entity with id: {}", savedCurvePoint.getId());
            return savedCurvePoint;
        } catch (Exception e) {
            log.error("Failed to save CurvePoint entity: {}", curvePoint, e);
            throw new RuntimeException("Failed to save CurvePoint entity", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        log.debug("Deleting CurvePoint entity with id: {}", id);

        try {
            if (!curvePointRepository.existsById(id)) {
                log.warn("Attempted to delete non-existent CurvePoint entity with id: {}", id);
                throw new IllegalArgumentException("CurvePoint entity not found for id: " + id);
            }
            curvePointRepository.deleteById(id);
            log.info("Successfully deleted CurvePoint entity with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete CurvePoint entity with id: {}", id, e);
            throw new RuntimeException("Failed to delete CurvePoint entity", e);
        }
    }


}
