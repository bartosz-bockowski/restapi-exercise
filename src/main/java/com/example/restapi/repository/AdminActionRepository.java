package com.example.restapi.repository;

import com.example.restapi.domain.AdminAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminActionRepository extends JpaRepository<AdminAction, Long> {
}
