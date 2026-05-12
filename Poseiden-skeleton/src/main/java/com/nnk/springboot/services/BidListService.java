package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;

import java.util.List;

/**
 * Service interface for managing operations related to the BidList entity.
 * Provides methods for CRUD (Create, Read, Update, Delete) operations.
 */
public interface BidListService {
    List<BidList> findAll();

    BidList findById(Integer id);

    BidList save(BidList bidList);

    void deleteById(Integer id);
}
