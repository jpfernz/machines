package com.worksite.machines.controller;

import com.worksite.machines.dto.contract.MachineDTO;
import com.worksite.machines.exceptions.DataException;
import com.worksite.machines.models.Machine;
import com.worksite.machines.service.MachinesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/machines")
@Tag(name = "Machines", description = "Machine API")
public class MachinesController {

    private final MachinesService machinesService;

    private final ModelMapper modelMapper;

    public MachinesController(MachinesService machinesService, ModelMapper modelMapper) {
        this.machinesService = machinesService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all machines", description = "Returns a list of all machines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of machines"),
            @ApiResponse(responseCode = "500", description = "Internal server error while fetching machines")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public List<MachineDTO> getMachines() {
        try {
            List<MachineDTO> machinesList = new ArrayList<>();
            machinesService.getAllMachines().forEach(machine -> machinesList.add(modelMapper.map(machine, MachineDTO.class)));
            return machinesList;
        } catch (Exception e) {
            throw new DataException("Error occurred while fetching list of machines",e);
        }
    }

    @Operation(summary = "Get machine by ID", description = "Returns a machine by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved machine"),
            @ApiResponse(responseCode = "404", description = "Machine not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error while fetching machine")
    })
    @PreAuthorize("hasAnyRole('ROLE_USER')")
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

    @Operation(summary = "Add a new machine", description = "Adds a new machine to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added new machine"),
            @ApiResponse(responseCode = "500", description = "Internal server error while adding machine")
    })
    @PreAuthorize("hasAnyRole('ROLE_USER')")
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

    @Operation(summary = "Update a machine", description = "Updates an existing machine in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated machine"),
            @ApiResponse(responseCode = "404", description = "Machine not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error while updating machine")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<MachineDTO> updateMachine(@PathVariable Long id, @RequestBody MachineDTO updatedMachine) {

        try {
            MachineDTO machineResponse = modelMapper.map(
                    machinesService.updateMachine(id, modelMapper.map(updatedMachine, Machine.class)),
                    MachineDTO.class);

            return ResponseEntity.status(HttpStatus.OK).body(machineResponse);
        } catch (Exception e) {
            log.error("Error occurred while updating machine", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Delete a machine", description = "Deletes an existing machine from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted machine"),
            @ApiResponse(responseCode = "404", description = "Machine not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error while deleting machine")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
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
