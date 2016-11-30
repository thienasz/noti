package com.th3.appms.repository;

import com.th3.appms.domain.Imei;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Imei entity.
 */
@SuppressWarnings("unused")
public interface ImeiRepository extends JpaRepository<Imei,Long> {

    @Query("select distinct imei from Imei imei left join fetch imei.types")
    List<Imei> findAllWithEagerRelationships();

    @Query("select imei from Imei imei left join fetch imei.types where imei.id =:id")
    Imei findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select imei from Imei imei where imei.imei =:imei")
    Imei findOneWithImei(@Param("imei") String imei);

}
