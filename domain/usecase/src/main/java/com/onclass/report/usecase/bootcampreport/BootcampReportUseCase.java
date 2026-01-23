package com.onclass.report.usecase.bootcampreport;

import com.onclass.report.enums.ExceptionMessages;
import com.onclass.report.exceptions.NotFoundException;
import com.onclass.report.model.bootcampreport.BootcampCreated;
import com.onclass.report.model.bootcampreport.BootcampReport;
import com.onclass.report.model.bootcampreport.gateways.BootcampReportRepositoryPort;
import com.onclass.report.port.consumer.CapabilityConsumerPort;
import com.onclass.report.port.consumer.PersonConsumerPort;
import com.onclass.report.port.consumer.TechnologyConsumerPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.onclass.report.usecase.utils.BootcampReportUtils.buildBootcampReport;
import static com.onclass.report.usecase.utils.BootcampReportUtils.buildCapabilityWithTechnologies;

@RequiredArgsConstructor
public class BootcampReportUseCase {
    private final BootcampReportRepositoryPort bootcampReportRepositoryPort;
    private final CapabilityConsumerPort capabilityConsumerPort;
    private final TechnologyConsumerPort technologyConsumerPort;
    private final PersonConsumerPort personConsumerPort;

    public Mono<Void> handleBootcampCreation(BootcampCreated bootcampCreated) {
        return capabilityConsumerPort.getCapabilitiesByBootcampId(bootcampCreated.getBootcampId())
                .flatMap(capability -> technologyConsumerPort.getTechnologiesByCapabilityId(capability.getId())
                        .collectList()
                        .map(techs -> buildCapabilityWithTechnologies(capability, techs))
                )
                .collectList()
                .map(capabilities -> buildBootcampReport(bootcampCreated, capabilities))
                .flatMap(bootcampReportRepositoryPort::save)
                .then();
    }

    public Mono<Void> addPersonToBootcampReport(Long bootcampId, Long personId) {
        return personConsumerPort.getPersonById(personId)
                .flatMap(student -> bootcampReportRepositoryPort.addStudentToReport(bootcampId, student));
    }

    public Mono<BootcampReport> getMostSuccessfulBootcamp() {
        return bootcampReportRepositoryPort.findMostEnrolledBootcamp()
                .switchIfEmpty(Mono.error(new NotFoundException(ExceptionMessages.BOOTCAMP_NOT_FOUND.getMessage())));
    }
}
