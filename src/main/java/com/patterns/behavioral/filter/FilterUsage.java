package com.patterns.behavioral.filter;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.patterns.behavioral.filter.AbstractRequestClientTypeCountCriteria.Condition.EQUALS;
import static com.patterns.behavioral.filter.AbstractRequestClientTypeCountCriteria.Condition.MORE_OR_EQUALS;
import static com.patterns.behavioral.filter.ClientType.BUYER;
import static com.patterns.behavioral.filter.ClientType.SELLER;
import static com.patterns.behavioral.filter.ObjectType.*;
import static com.patterns.behavioral.filter.RequestType.*;
import static com.patterns.behavioral.filter.Sex.FEMALE;
import static java.util.Arrays.asList;

public class FilterUsage {
    public static void main(String[] args) {
        FilterUsage executor = new FilterUsage();
        executor.execute();
    }

    private void execute() {
        List<Request> requests = new ArrayList<>();
        requests.add(
                Request.builder()
                        .objectType(APARTMENT)
                        .requestType(MOVE_OWNER)
                        .clients(asList(
                                RequestClient.builder()
                                        .clientType(SELLER)
                                        .client(
                                                Client.builder()
                                                        .age(25)
                                                        .name("Петр")
                                                        .passportNumber("3345")
                                                        .passportSerial("345225")
                                                        .sex(Sex.MALE)
                                                        .surname("Иванов")
                                                        .patronymic("Игоревич")
                                                        .build()
                                        )
                                        .build(),
                                RequestClient.builder()
                                        .clientType(BUYER)
                                        .client(
                                                Client.builder()
                                                        .name("Татьяна")
                                                        .passportNumber("3326")
                                                        .passportSerial("675333")
                                                        .sex(FEMALE)
                                                        .surname("Гришанина")
                                                        .patronymic("Беслановна")
                                                        .build()
                                        )
                                        .build()

                        ))
                        .build()
        );

        RequestAndCriteria andCriteria = new RequestAndCriteria(
                new ArrayList<>(
                        asList(
                                new RequestCriteriaObjectTypeApartment(),
                                new RequestCriteriaRequestTypeMoveOwner(),
                                new RequestWithOneBuyer(),
                                new RequestWithMoreThanOneOrEqualsSellers()
                        )
                )
        );

        assert andCriteria.acceptCriteria(requests).size() == 1;

        andCriteria.criteriaList.add(new RequestCriteriaObjectTypeLand());

        assert andCriteria.acceptCriteria(requests).size() == 0;
    }
}


interface RequestCriteria {
    List<Request> acceptCriteria(List<Request> requests);
}

abstract class AbstractRequestCriteria implements RequestCriteria {
    @Override
    public List<Request> acceptCriteria(List<Request> requests) {
        return requests.stream()
                .filter(getPredicate())
                .collect(Collectors.toList());
    }

    abstract Predicate<Request> getPredicate();
}

abstract class AbstractRequestRequestTypeCriteria extends AbstractRequestCriteria {
    @Override
    Predicate<Request> getPredicate() {
        return request -> request.getRequestType() == getRequestType();
    }

    abstract RequestType getRequestType();
}

class RequestCriteriaRequestTypeExcrowAccount extends AbstractRequestRequestTypeCriteria {

    @Override
    RequestType getRequestType() {
        return ESCROW_ACCOUNT;
    }
}

class RequestCriteriaRequestTypeRemoveEncumbrance extends AbstractRequestRequestTypeCriteria {

    @Override
    RequestType getRequestType() {
        return REMOVE_ENCUMBRANCE;
    }
}

class RequestCriteriaRequestTypeMoveOwner extends AbstractRequestRequestTypeCriteria {

    @Override
    RequestType getRequestType() {
        return MOVE_OWNER;
    }
}

class RequestAndCriteria implements RequestCriteria {
    List<RequestCriteria> criteriaList;

    private RequestAndCriteria() {
    }

    public RequestAndCriteria(List<RequestCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public List<Request> acceptCriteria(List<Request> requests) {
        List<Request> res = new ArrayList<>(requests);
        for (RequestCriteria requestCriteria : criteriaList) {
            res = requestCriteria.acceptCriteria(res);
        }
        return res;
    }
}

class RequestOrCriteria implements RequestCriteria {
    List<RequestCriteria> criteriaList;

    private RequestOrCriteria() {
    }

    public RequestOrCriteria(List<RequestCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    @Override
    public List<Request> acceptCriteria(List<Request> requests) {
        Set<Request> res = new HashSet<>();
        for (RequestCriteria requestCriteria : criteriaList) {
            res.addAll(requestCriteria.acceptCriteria(requests));
        }
        return new ArrayList<>(res);
    }
}

abstract class AbstractRequestObjectTypeCriteria extends AbstractRequestCriteria {
    @Override
    Predicate<Request> getPredicate() {
        return request -> request.getObjectType() == getObjectType();
    }

    abstract ObjectType getObjectType();
}

class RequestCriteriaObjectTypeLivingHouse extends AbstractRequestObjectTypeCriteria {
    @Override
    ObjectType getObjectType() {
        return LIVING_HOUSE;
    }
}

class RequestCriteriaObjectTypeApartment extends AbstractRequestObjectTypeCriteria {
    @Override
    ObjectType getObjectType() {
        return APARTMENT;
    }
}

class RequestCriteriaObjectTypeLand extends AbstractRequestObjectTypeCriteria {
    @Override
    ObjectType getObjectType() {
        return LAND;
    }
}

class RequestCriteriaObjectTypeUnderConstruction extends AbstractRequestObjectTypeCriteria {
    @Override
    ObjectType getObjectType() {
        return UNDER_CONSTRUCTION;
    }
}

class RequestCriteriaObjectTypeRealEstate extends AbstractRequestObjectTypeCriteria {
    @Override
    ObjectType getObjectType() {
        return REAL_ESTATE;
    }
}

abstract class AbstractRequestClientTypeCountCriteria extends AbstractRequestCriteria {

    @Override
    Predicate<Request> getPredicate() {
        return request -> {
            if (request.getClients() == null) return false;

            long size = request.getClients().stream().filter(
                    client -> client.getClientType() == getClientType()
            ).count();

            Condition condition = getCondition();
            if (condition == null) {
                throw new IllegalStateException("Condition can not be null");
            }

            switch (condition) {
                case EQUALS:
                    return size == getSize();
                case LESS_THAN:
                    return size < getSize();
                case MORE_THAN:
                    return size > getSize();
                case LESS_OR_EQUALS:
                    return size <= getSize();
                case MORE_OR_EQUALS:
                    return size >= getSize();
            }
            return false;
        };
    }

    abstract ClientType getClientType();

    abstract int getSize();

    abstract Condition getCondition();

    enum Condition {
        MORE_OR_EQUALS,
        LESS_OR_EQUALS,
        MORE_THAN,
        LESS_THAN,
        EQUALS
    }
}

class RequestWithOneBuyer extends AbstractRequestClientTypeCountCriteria {

    @Override
    ClientType getClientType() {
        return BUYER;
    }

    @Override
    int getSize() {
        return 1;
    }

    @Override
    Condition getCondition() {
        return EQUALS;
    }
}

class RequestWithMoreThanOneOrEqualsSellers extends AbstractRequestClientTypeCountCriteria {

    @Override
    ClientType getClientType() {
        return SELLER;
    }

    @Override
    int getSize() {
        return 1;
    }

    @Override
    Condition getCondition() {
        return MORE_OR_EQUALS;
    }
}

@Data
@Builder
class Request {
    RequestType requestType;
    ObjectType objectType;
    List<RequestClient> clients;
}

@Data
@Builder
class RequestClient {
    ClientType clientType;
    Client client;
}

@Data
@Builder
class Client {
    String name;
    String surname;
    String patronymic;
    Integer age;
    Sex sex;
    String passportNumber;
    String passportSerial;
}

enum RequestType {
    MOVE_OWNER,
    REMOVE_ENCUMBRANCE,
    ESCROW_ACCOUNT
}

enum ObjectType {
    APARTMENT,
    LIVING_HOUSE,
    LAND,
    UNDER_CONSTRUCTION,
    REAL_ESTATE
}

enum ClientType {
    SELLER,
    BUYER
}

enum Sex {
    MALE,
    FEMALE
}