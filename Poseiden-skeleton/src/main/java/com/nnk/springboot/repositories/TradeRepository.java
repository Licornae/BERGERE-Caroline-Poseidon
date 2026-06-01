package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for performing CRUD operations on the Trade entity.
 * Extends JpaRepository to provide standard database operations
 * and query methods for interacting with the "trade" table in the database.
 */
public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
