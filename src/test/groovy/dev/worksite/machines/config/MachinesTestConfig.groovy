package dev.worksite.machines.config

import dev.worksite.machines.controller.MachinesController
import dev.worksite.machines.repository.MachinesRepository
import dev.worksite.machines.service.MachinesService
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import spock.mock.DetachedMockFactory

@Component
class MachinesTestConfig {

    DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper()
    }

    @Bean
    MachinesController machinesController(MachinesService machinesService, ModelMapper modelMapper) {
        return new MachinesController(machinesService, modelMapper)
    }

    @Bean
    MachinesService machinesService(MachinesRepository machinesRepository) {
        return detachedMockFactory.Stub(MachinesService)
    }

    @Bean
    MachinesRepository machinesRepository() {
        return detachedMockFactory.Stub(MachinesRepository)
    }

}
