package dev.worksite.machines.controller;

import dev.worksite.machines.dto.contract.MachineDTO;
import dev.worksite.machines.models.Machine;
import dev.worksite.machines.service.MachinesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MachinesControllerTest {

    @Mock
    private MachinesService machinesService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MachinesController machinesController;

    @Test
    void getMachinesReturnsListOfMachines() {
        // Arrange
        // Mock the behavior of machinesService and modelMapper as needed
        List<Machine> machines = List.of(new Machine(), new Machine());
        List<MachineDTO> machineDTOs = List.of(new MachineDTO(), new MachineDTO());

        // Act
        when(machinesService.getAllMachines()).thenReturn(machines);
//        when(modelMapper.map(any(Machine.class), eq(MachineDTO.class)))
//                .thenReturn(machineDTOs.get(0), machineDTOs.get(1));

        when(modelMapper.map(any(Machine.class), eq(MachineDTO.class)))
                .thenReturn(machineDTOs.get(0), machineDTOs.get(1));
        List<MachineDTO> result = machinesController.getMachines();

        // Assert
        assertEquals(machineDTOs, result);
        verify(machinesService).getAllMachines();
        verify(modelMapper, times(2)).map(any(Machine.class), eq(MachineDTO.class));

        assertNotNull(result);
        // Add more assertions based on the expected behavior
    }

    @Test
    void getMachineByIdReturnsMachineWhenFound() {
        Long id = 1L;
        Machine machine = new Machine();
        MachineDTO machineDTO = new MachineDTO();

        when(machinesService.getMachineById(id)).thenReturn(Optional.of(machine));
        when(modelMapper.map(machine, MachineDTO.class)).thenReturn(machineDTO);

        ResponseEntity<MachineDTO> response = machinesController.getMachineById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(machineDTO, response.getBody());
        verify(machinesService).getMachineById(id);
        verify(modelMapper).map(machine, MachineDTO.class);
    }

    @Test
    void getMachineByIdReturnsInternalServerErrorWhenNotFound() {
        Long id = 1L;

        when(machinesService.getMachineById(id)).thenReturn(Optional.empty());

        ResponseEntity<MachineDTO> response = machinesController.getMachineById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(machinesService).getMachineById(id);
    }

    @Test
    void addMachineReturnsCreatedMachine() {
        MachineDTO newMachine = new MachineDTO();
        Machine machine = new Machine();
        MachineDTO createdMachineDTO = new MachineDTO();

        when(modelMapper.map(newMachine, Machine.class)).thenReturn(machine);
        when(machinesService.createMachine(machine)).thenReturn(machine);
        when(modelMapper.map(machine, MachineDTO.class)).thenReturn(createdMachineDTO);

        ResponseEntity<MachineDTO> response = machinesController.addMachine(newMachine);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdMachineDTO, response.getBody());
        verify(modelMapper).map(newMachine, Machine.class);
        verify(machinesService).createMachine(machine);
        verify(modelMapper).map(machine, MachineDTO.class);
    }

    @Test
    void deleteMachineReturnsNoContentWhenSuccessful() {
        Long id = 1L;

        doNothing().when(machinesService).deleteMachine(id);

        ResponseEntity<Void> response = machinesController.deleteMachine(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(machinesService).deleteMachine(id);
    }

    @Test
    void deleteMachineReturnsInternalServerErrorWhenExceptionOccurs() {
        Long id = 1L;

        doThrow(new RuntimeException()).when(machinesService).deleteMachine(id);

        ResponseEntity<Void> response = machinesController.deleteMachine(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(machinesService).deleteMachine(id);
    }
}