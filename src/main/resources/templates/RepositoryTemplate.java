package com.example.jdlparser.repositories;

import com.example.jdlparser.entities.{{ENTITY_NAME}};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface {{ENTITY_NAME}}Repository extends JpaRepository<{{ENTITY_NAME}}, Long> {
}
