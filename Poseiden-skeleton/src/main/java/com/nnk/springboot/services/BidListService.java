package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;

import java.util.List;

public interface BidListService {
    List<BidList> findAll();

    BidList findById(Integer id);

    BidList save(BidList bidList);

    void deleteById(Integer id);
}
