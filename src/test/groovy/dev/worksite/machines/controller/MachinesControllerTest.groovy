package dev.worksite.machines.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.worksite.machines.config.MachinesTestConfig
import dev.worksite.machines.dto.contract.MachineDTO
import dev.worksite.machines.models.Machine
import dev.worksite.machines.repository.MachinesRepository
import dev.worksite.machines.types.MachineStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

@ContextConfiguration(classes = MachinesTestConfig)
class MachinesControllerTest extends Specification {

    @Autowired
    private MachinesController machinesController

    @Autowired
    private MachinesRepository machinesRepository

    private MockMvc mockMvc
    private ObjectMapper objectMapper

    def setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(machinesController).build()
        objectMapper = new ObjectMapper()
    }

    def "Retrieve list of machines"() {
        given: "DB retrieves all machines"
        machinesRepository.findAll() >> getAllMachines()

        when: "A request is made to retrieve all machines"
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/machines"))

        then: "It should return the expected HTTP status code"
        result.andReturn().getResponse().status == expectedStatusCode

        and: "Return the list of projects"
        result.andReturn().getResponse().contentAsString == expectedPayload

        where:
        expectedPayload                                                                                                                             || expectedStatusCode
        '[{"id":1,"name":"Machine 1","projectId":1,"machineStatus":"ACTIVE"},{"id":2,"name":"Machine 2","projectId":1,"machineStatus":"INACTIVE"}]' || 200
    }

    def "Add a new machine"() {
        given: "A new machine to be added"
        MachineDTO newMachine = new MachineDTO()
        newMachine.setName("Excavator")
        newMachine.setProjectId(1L)
        newMachine.setMachineStatus(MachineDTO.MachineStatusEnum.ACTIVE)

        and: "Configure the repository to return the added machine"
        machinesRepository.save(_ as Machine) >> { Machine machine ->
            machine.setId(3L)
            machine.setName("Excavator")
            machine.setProjectId(1L)
            machine.setMachineStatus(MachineStatus.ACTIVE)
            return machine
        }

        when: "A request is made to add a new machine"
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/machines")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newMachine)))

        then: "It should return the expected HTTP status code"
        result.andReturn().getResponse().status == 201

        and: "Return the added machine"
        String responseContent = result.andReturn().getResponse().contentAsString
        MachineDTO addedMachine = objectMapper.readValue(responseContent, MachineDTO.class)

        and: "It should match the added machine"
        with(addedMachine) {
            id == 3L
            name == "Excavator"
            projectId == 1L
            machineStatus == MachineDTO.MachineStatusEnum.ACTIVE
        }

    }

    private static List<Machine> getAllMachines() {
        List<Machine> machineList = new ArrayList<>()
        Machine machine1 = new Machine()
        machine1.setId(1L)
        machine1.setName("Machine 1")
        machine1.setProjectId(1L)
        machine1.setMachineStatus(MachineStatus.ACTIVE)
        machineList.add(machine1)

        Machine machine2 = new Machine()
        machine2.setId(2L)
        machine2.setName("Machine 2")
        machine2.setProjectId(1L)
        machine2.setMachineStatus(MachineStatus.INACTIVE)
        machineList.add(machine2)

        return machineList
    }

}
