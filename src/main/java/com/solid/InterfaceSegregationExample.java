package com.solid;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.solid.AuditService.AuditType.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

public class InterfaceSegregationExample {
    public static void main(String[] args) {
        InterfaceSegregationExample executor = new InterfaceSegregationExample();
        executor.execute();
    }

    /**
     * разделение интерфейсов
     *
     * Как и в случае с принципом единственной ответственности,
     * цель принципа разделения интерфейса заключается в минимизации побочных эффектов
     * и повторов за счёт разделения ПО на независимые части
     *
     * делает систему более гибкой
     * упрощает и ускоряет доработку
     * упрощает модульное тестирование
     * не нужна вся связка компонентов
     */
    private void execute() {
        EntityManager em = new EntityManager();
        em.setAuditService(new AuditService());

        AddressMapper mapper = new AddressMapper();

        JobEntity job = new JobEntity(randomUUID(), now(), now(), new Address());
        mapper.toDto(job.getAddress());
        em.persist(job);
    }
}

class AddressMapper {
    AddressDTO toDto(Address address) {
        //convert to dto
        return new AddressDTO();
    }

    Address toModel(AddressDTO dto) {
        //convert to model
        return new Address();
    }
}

@Data
class EntityManager {
    private AuditService auditService;

    void persist(Entity entity) {
        //persist logic
        auditService.revision(entity, CREATED);
    }

    void merge(Entity entity) {
        //merge logic
        auditService.revision(entity, MODIFIED);
    }

    void delete(Entity entity) {
        //delete logic
        auditService.revision(entity, DELETED);
    }

}

class AuditService {
    void revision(Entity entity, AuditType type) {
        //save audit logic
        switch (type) {
            case CREATED:
                System.out.println(format("Persist entity with id '%s'", entity.getId()));
                break;
            case DELETED:
                System.out.println(format("Delete entity with id '%s'", entity.getId()));
                break;
            case MODIFIED:
                System.out.println(format("Update entity with id '%s'", entity.getId()));
                break;
        }
    }

    enum AuditType {
        CREATED,
        MODIFIED,
        DELETED
    }
}

@Data
@Builder
class JobEntity implements HasUUID, Auditable, HasAddress {
    private UUID id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Address address;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public Address getAddress() {
        return address;
    }
}

interface HasUUID extends Entity<UUID> {
    UUID getId();
}

interface HasLongId extends Entity<Long> {
    Long getId();
}

interface Entity<T> {
    T getId();
}

interface Auditable {
    LocalDateTime getCreatedDate();

    void setCreatedDate(LocalDateTime createdDate);

    LocalDateTime getModifiedDate();

    void setModifiedDate(LocalDateTime modifiedDate);
}

interface HasAddress {
    Address getAddress();
}

class Address {
    //address fields
}

class AddressDTO {
    //dto object for address
}