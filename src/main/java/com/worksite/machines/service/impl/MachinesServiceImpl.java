package com.worksite.machines.service.impl;

import com.worksite.machines.models.Machine;
import com.worksite.machines.repository.MachinesRepository;
import com.worksite.machines.service.MachinesService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MachinesServiceImpl implements MachinesService {

    private final MachinesRepository repository;

    public MachinesServiceImpl(MachinesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable <Machine> getAllMachines() {
        return repository.findAll();
    }

    @Override
    public Optional<Machine> getMachineById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Machine createMachine(Machine newMachine) {
        return repository.save(newMachine);
    }

    @Override
    public Machine updateMachine(Long id, Machine updatedMachine) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Machine with id " + id + " does not exist.");
        }
        updatedMachine.setId(id);
        return repository.save(updatedMachine);
    }

    @Override
    public void deleteMachine(Long id) {
        repository.deleteById(id);
    }
}
