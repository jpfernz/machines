package dev.worksite.machines.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.worksite.machines.config.MachinesTestConfig
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
        expectedPayload || expectedStatusCode
        '[{"id":1,"name":"Machine 1","projectId":1,"machineStatus":"ACTIVE"},{"id":2,"name":"Machine 2","projectId":1,"machineStatus":"INACTIVE"}]'         || 200
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
