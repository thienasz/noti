package com.th3.appms.repository;

import com.th3.appms.domain.Type;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Type entity.
 */
@SuppressWarnings("unused")
public interface TypeRepository extends JpaRepository<Type,Long> {

}
