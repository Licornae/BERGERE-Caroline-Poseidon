package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;

import java.util.List;

/**
 * Service interface for managing operations related to the Trade entity.
 * Provides methods for CRUD (Create, Read, Update, Delete) operations.
 */
public interface TradeService {

    List<Trade> findAll();

    Trade findById(Integer id);

    Trade save(Trade trade);

    void deleteById(Integer id);
}
