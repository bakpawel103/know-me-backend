package com.example.knowme.repository;

import java.util.Optional;

import com.example.knowme.model.ERole;
import com.example.knowme.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
