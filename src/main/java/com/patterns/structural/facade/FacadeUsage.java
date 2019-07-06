package com.patterns.structural.facade;

public class FacadeUsage {
    public static void main(String[] args) {
        FacadeUsage executor = new FacadeUsage();
        executor.execute();
    }

    public void execute() {
        new ReportManagerFacade().createReport();
    }
}

/**
 * Класс Фасад
 */
class ReportManagerFacade {
    /**
     * с виду простой метод создания отчета который скрывает в себе взаимодействие между неск внешними системами и источником данных
     */
    public byte[] createReport() {
        new PersonClient().getPersons();
        new ExternalDocumentExchangeWS().findAllDocuments();
        new GenerateContractClient().generateContract();
        return new ReportRepository().saveReport();
    }
}

class PersonClient {
    public void getPersons() {
        System.out.println("Get persons");
    }
}

class ExternalDocumentExchangeWS {
    public void findAllDocuments() {
        System.out.println("Find all documents in external WS");
    }
}

class GenerateContractClient {
    public void generateContract() {
        System.out.println("Generate contract");
    }
}

class ReportRepository {
    public byte[] saveReport() {
        System.out.println("Save report");
        return new byte[0];
    }
}