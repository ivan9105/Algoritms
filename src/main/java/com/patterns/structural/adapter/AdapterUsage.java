package com.patterns.structural.adapter;

public class AdapterUsage {
    /**
     * это структурный паттерн проектирования,
     * который позволяет объектам с несовместимыми интерфейсами работать вместе.
     */
    public static void main(String[] args) {
        AdapterUsage executor = new AdapterUsage();
        executor.execute();
    }

    public void execute() {
        XmlSource xmlSource = new XmlSource();
        Adapter adapter = new Adapter(xmlSource);

        ExternalClient client = new ExternalClient();
        client.send(adapter.getData());
    }
}

class JsonData {
}

class XmlData {
}

//новый класс
class JsonSource {
    public JsonData getData() {
        System.out.println("Get json data");
        return new JsonData();
    }
}

//старый класс
class XmlSource {
    public XmlData getData() {
        System.out.println("Get xml data");
        return new XmlData();
    }
}

//сам адаптер
class Adapter extends JsonSource {
    private XmlSource xmlSource;

    public Adapter(XmlSource xmlSource) {
        this.xmlSource = xmlSource;
    }

    @Override
    public JsonData getData() {
        XmlData xmlData = xmlSource.getData();
        System.out.println("Процесс конвертации xml в json");
        return new JsonData();
    }
}

class ExternalClient {
    public void send(JsonData data) {
        //логика по отправке сообщения
        System.out.println("Send....");
    }
}