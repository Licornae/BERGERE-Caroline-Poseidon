package com.nnk.springboot.services;


import com.nnk.springboot.domain.RuleName;

import java.util.List;
import java.util.Optional;

public interface RuleNameService {

    List<RuleName> findAll();

    Optional<RuleName> findById(Integer id);

    RuleName save(RuleName ruleName);

    void deleteById(Integer id);
}
