package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.RuleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on the RuleName entity.
 * Extends JpaRepository to provide standard database operations and
 * query methods for interacting with the "rulename" table in the database.
 */
@Repository
public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {
}
