package dev.worksite.machines.repository;

import dev.worksite.machines.models.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachinesRepository extends JpaRepository<Machine, Long> {
}
