package com.nnk.springboot.services;


import com.nnk.springboot.domain.RuleName;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing operations related to the RuleName entity.
 * Provides methods for CRUD (Create, Read, Update, Delete) operations.
 */
public interface RuleNameService {

    List<RuleName> findAll();

    Optional<RuleName> findById(Integer id);

    RuleName save(RuleName ruleName);

    void deleteById(Integer id);
}
