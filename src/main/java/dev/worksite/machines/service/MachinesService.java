package dev.worksite.machines.service;

import dev.worksite.machines.models.Machine;

import java.util.Optional;

public interface MachinesService {
    Iterable <Machine> getAllMachines();

    Optional <Machine> getMachineById(Long id);

    Machine createMachine(Machine newMachine);

    Machine updateMachine(Long id);

    void deleteMachine(Long id);
}
