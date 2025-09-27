package com.worksite.machines.service;

import com.worksite.machines.models.Machine;

import java.util.Optional;

public interface MachinesService {
    Iterable <Machine> getAllMachines();

    Optional<Machine> getMachineById(Long id);

    Machine createMachine(Machine newMachine);

    Machine updateMachine(Long id, Machine updatedMachine);

    void deleteMachine(Long id);
}

