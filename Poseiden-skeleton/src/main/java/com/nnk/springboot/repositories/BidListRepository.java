package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for performing CRUD operations on the BidList entity.
 * Extends JpaRepository to provide standard database operations and
 * query methods for interacting with the "bidlist" table in the database.
 */
public interface BidListRepository extends JpaRepository<BidList, Integer> {

}
