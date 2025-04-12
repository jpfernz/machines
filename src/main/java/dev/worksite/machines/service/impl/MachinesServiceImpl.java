package dev.worksite.machines.service.impl;

import dev.worksite.machines.models.Machine;
import dev.worksite.machines.repository.MachinesRepository;
import dev.worksite.machines.service.MachinesService;
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
    public Machine updateMachine(Long id) {
        return repository.findById(id)
                .map(machine -> {
                    machine.setId(id);
                    return repository.save(machine);
                })
                .orElseThrow(() -> new RuntimeException("Machine not found"));
    }

    @Override
    public void deleteMachine(Long id) {
        repository.deleteById(id);
    }
}
