package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the RuleNameService interface for managing operations
 * related to the RuleName entity.
 * Provides concrete methods for CRUD (Create, Read, Update, Delete) operations.
 * Uses the RuleNameRepository for database interactions.
 */
@Slf4j
@Service
public class RuleNameServiceImpl implements RuleNameService {

    private final RuleNameRepository ruleNameRepository;

    @Autowired
    public RuleNameServiceImpl(RuleNameRepository ruleNameRepository) {

        this.ruleNameRepository = ruleNameRepository;

        log.info("RuleNameServiceImpl initialized with repository: {}", ruleNameRepository.getClass().getName());
    }

    /**
     * Retrieves all RuleName entities from the database.
     *
     * @return List of all RuleName entities
     */
    @Override
    public List<RuleName> findAll() {

        log.debug("Finding all RuleName entities");

        List<RuleName> ruleNames = ruleNameRepository.findAll();

        log.debug("Found {} RuleName entities", ruleNames.size());
        return ruleNames;
    }

    /**
     * Retrieves a RuleName entity by its ID.
     *
     * @param id The ID of the RuleName to find
     * @return Optional containing the found RuleName
     * @throws IllegalArgumentException if no RuleName is found with the given ID
     */
    @Override
    public Optional<RuleName> findById(Integer id) {

        log.debug("Finding RuleName with ID: {}", id);

        Optional<RuleName> ruleName = ruleNameRepository.findById(id);

        if (ruleName.isEmpty()) {
            String errorMsg = "RuleName not found for id: " + id;
            log.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        log.debug("Found RuleName: {}", ruleName.get());
        return ruleName;
    }

    /**
     * Saves a RuleName entity to the database.
     *
     * @param ruleName The RuleName entity to save
     * @return The saved RuleName entity
     */
    @Override
    public RuleName save(RuleName ruleName) {

        log.debug("Saving RuleName: {}", ruleName);

        try {
            RuleName savedRule = ruleNameRepository.save(ruleName);
            log.info("Successfully saved RuleName with ID: {}", savedRule.getId());
            return savedRule;
        } catch (Exception e) {
            log.error("Error saving RuleName: {}", ruleName, e);
            throw new RuntimeException("Failed to save RuleName: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a RuleName entity by its ID.
     *
     * @param id The ID of the RuleName to delete
     */
    @Override
    public void deleteById(Integer id) {

        log.debug("Deleting RuleName with ID: {}", id);

        try {
            if (!ruleNameRepository.existsById(id)) {
                log.warn("Attempted to delete non-existent RuleName with ID: {}", id);
                throw new IllegalArgumentException("RuleName not found for id: " + id);
            }
            ruleNameRepository.deleteById(id);
            log.info("Successfully deleted RuleName with ID: {}", id);

        } catch (Exception e) {

            log.error("Error deleting RuleName with ID: {}", id, e);
            throw new RuntimeException("Failed to delete RuleName: " + e.getMessage(), e);
        }
    }
}
