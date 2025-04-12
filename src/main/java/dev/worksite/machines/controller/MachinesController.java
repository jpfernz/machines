package dev.worksite.machines.controller;

import dev.worksite.machines.dto.contract.MachineDTO;
import dev.worksite.machines.exceptions.DataException;
import dev.worksite.machines.models.Machine;
import dev.worksite.machines.service.MachinesService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/machines")
public class MachinesController {

    private final MachinesService machinesService;

    private final ModelMapper modelMapper;

    public MachinesController(MachinesService machinesService, ModelMapper modelMapper) {
        this.machinesService = machinesService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<MachineDTO> getMachines() {
        try {
            List<MachineDTO> machinesList = new ArrayList<>();
            machinesService.getAllMachines().forEach(machine -> machinesList.add(modelMapper.map(machine, MachineDTO.class)));
            return machinesList;
        } catch (Exception e) {
            throw new DataException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineDTO> getMachineById(@PathVariable Long id) {
        try {
            MachineDTO machineResponse = modelMapper.map(
                    machinesService.getMachineById(id).orElseThrow(),
                    MachineDTO.class);

            return ResponseEntity.status(HttpStatus.OK).body(machineResponse);
        } catch (Exception e) {
            log.error("Error occurred while fetching machine", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<MachineDTO> addMachine(@RequestBody MachineDTO newMachine) {
        try {
            MachineDTO machineResponse = modelMapper.map(
                    machinesService.createMachine(modelMapper.map(newMachine, Machine.class)),
                    MachineDTO.class);

            return ResponseEntity.status(HttpStatus.CREATED).body(machineResponse);
        } catch (Exception e) {
            log.error("Error occurred while adding machine", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<MachineDTO> updateMachine(@PathVariable Long id) {
        try {
            MachineDTO machineResponse = modelMapper.map(
                    machinesService.updateMachine(id),
                    MachineDTO.class);

            return ResponseEntity.status(HttpStatus.OK).body(machineResponse);
        } catch (Exception e) {
            log.error("Error occurred while updating machine", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachine(@PathVariable Long id) {
        try {
            machinesService.deleteMachine(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error("Error occurred while deleting machine", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
