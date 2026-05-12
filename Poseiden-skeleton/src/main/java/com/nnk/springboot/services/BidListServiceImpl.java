package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation class for managing operations related to the BidList entity.
 * This service provides methods to retrieve data from the BidListRepository and perform
 * business logic as necessary. It implements the BidListService interface.
 */
@Slf4j
@Service
public class BidListServiceImpl implements BidListService {

    private final BidListRepository bidListRepository;

    @Autowired
    public BidListServiceImpl(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    /**
     * Retrieves all BidList entities from the repository.
     *
     * @return a list of all BidList entities
     */
    @Override
    public List<BidList> findAll() {
        log.debug("Findind all BidLists entities");
        List<BidList> bidLists = bidListRepository.findAll();
        log.debug("Found {} BidLists entities", bidLists.size());
        return bidLists;
    }


    /**
     * Retrieves a BidList entity by its unique identifier.
     *
     * @param id the unique identifier of the BidList entity to be retrieved
     * @return the BidList entity associated with the given id
     * @throws IllegalArgumentException if no BidList entity is found for the given id
     */
    @Override
    public BidList findById(Integer id) {
        log.debug("Finding BidList entity with id: {}", id);

        return bidListRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMsg = "BidList not found for id: " + id;
                    log.error(errorMsg);
                    return new IllegalArgumentException(errorMsg);
                });
    }

    /**
     * Persists a BidList entity to the repository.
     *
     * @param bidList the BidList entity to be saved
     * @return the saved BidList entity with its updated state, including any generated identifier
     * @throws RuntimeException if the BidList entity could not be saved due to a persistent error
     */
    @Override
    public BidList save(BidList bidList) {
        log.debug("Saving BidList entity: {}", bidList);

        try {
            BidList savedBidList = bidListRepository.save(bidList);
            log.info("Successfully saved BidList entity with id: {}", savedBidList.getBidListId());
            return savedBidList;
        } catch (Exception e) {
            log.error("Failed to save BidList entity: {}", bidList, e);
            throw new RuntimeException("Failed to save BidList entity", e);
        }
    }

    /**
     * Deletes a BidList entity with the specified unique identifier.
     * If the entity does not exist, an {@code IllegalArgumentException} is thrown.
     * Logs the operation's details and handles any unexpected errors.
     *
     * @param id the unique identifier of the BidList entity to be deleted
     * @throws IllegalArgumentException if no BidList entity is found for the provided id
     * @throws RuntimeException if an unexpected error occurs during the deletion process
     */
    @Override
    public void deleteById(Integer id) {

        log.debug("Deleting BidList entity with id: {}", id);

        try{
            if (!bidListRepository.existsById(id)) {
                log.warn("Attempted to delete non-existent BidList entity with id: {}", id);
                throw new IllegalArgumentException("BidList entity not found for id: " + id);
            }
            bidListRepository.deleteById(id);
            log.info("Successfully deleted BidList entity with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete BidList entity with id: {}", id, e);
            throw new RuntimeException("Failed to delete BidList entity with id: " + id, e);
        }

    }
}
