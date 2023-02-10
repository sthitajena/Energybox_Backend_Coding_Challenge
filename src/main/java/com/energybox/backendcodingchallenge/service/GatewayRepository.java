package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GatewayRepository extends Repository<Gateway, String> {

    @Query("MATCH (gateway:Gateway) WHERE gateway.name CONTAINS $name RETURN gateway")
    List<Gateway> findSearchResults(@Param("name") String name);

    @Query("MATCH (gateway:Gateway) WHERE gateway.name = $name RETURN gateway")
    Gateway findByName(@Param("name") String name);
}

