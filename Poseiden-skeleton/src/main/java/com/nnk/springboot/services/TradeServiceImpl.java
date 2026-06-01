package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the TradeService interface for managing operations related to the Trade entity.
 * Provides methods for executing CRUD (Create, Read, Update, Delete) operations.
 * This class interacts with the TradeRepository to perform database operations.
 * Includes logging for monitoring method execution and error handling.
 */
@Slf4j
@Service
public class TradeServiceImpl implements TradeService{

    private final TradeRepository tradeRepository;

    @Autowired
    public TradeServiceImpl(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    /**
     * Retrieves all Trade entities from the repository.
     *
     * @return a list of all Trade entities found in the repository.
     */
    @Override
    public List<Trade> findAll() {
        log.debug("Finding all Trade entities");
        List<Trade> trades = tradeRepository.findAll();
        log.debug("Found {} Trade entities", trades.size());
        return trades;
    }

    /**
     * Retrieves a Trade entity by its unique identifier.
     *
     * @param id the unique identifier of the Trade entity to be retrieved
     * @return the Trade entity associated with the specified identifier
     * @throws EntityNotFoundException if no Trade entity is found with the given identifier
     */
    @Override
    public Trade findById(Integer id) {

        log.debug("Finding Trade entity with id: {}", id);

        return tradeRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMsg = "Trade not found for id: " + id;
                    log.error(errorMsg);
                    return new EntityNotFoundException(errorMsg);
                });
    }

    /**
     * Persists the given Trade entity to the repository.
     *
     * @param trade the Trade entity to be saved
     * @return the saved Trade entity with updated state, including any generated identifier
     * @throws RuntimeException if there is an error during the save operation
     */
    @Override
    public Trade save(Trade trade) {

        log.debug("Saving Trade entity: {}", trade);

        try {
            Trade savedTrade = tradeRepository.save(trade);
            log.info("Successfully saved Trade entity with id: {}", savedTrade.getTradeId());
            return savedTrade;
        } catch (Exception e) {
            log.error("Error saving Trade entity: {}", trade, e);
            throw new RuntimeException("Failed to save Trade entity", e);
        }
    }

    /**
     * Deletes the Trade entity associated with the specified identifier from the repository.
     *
     * @param id the unique identifier of the Trade entity to be deleted
     * @throws IllegalArgumentException if no Trade entity is found with the specified identifier
     * @throws RuntimeException if there is an error during the delete operation
     */
    @Override
    public void deleteById(Integer id) {

        log.debug("Deleting Trade entity with id: {}", id);

        try {
            if (!tradeRepository.existsById(id)) {
                log.warn("Attempted to delete non-existent Trade entity with id: {}", id);
                throw new IllegalArgumentException("Trade entity not found for id: " + id);
            }
            tradeRepository.deleteById(id);
            log.info("Successfully deleted Trade entity with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete Trade entity with id: {}", id, e);
            throw new RuntimeException("Failed to delete Trade entity with id: " + id, e);
        }
    }
}
