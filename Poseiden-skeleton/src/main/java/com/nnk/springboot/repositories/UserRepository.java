package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on the User entity.
 * Extends JpaRepository to provide standard database operations and
 * JpaSpecificationExecutor to support query specification pattern for flexible queries.
 * Used for interacting with the "users" table in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

}
