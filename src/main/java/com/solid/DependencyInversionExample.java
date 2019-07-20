package com.solid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;

public class DependencyInversionExample {
    public static void main(String[] args) {
        DependencyInversionExample executor = new DependencyInversionExample();
        executor.execute();
    }

    private void execute() {
        NotifierService notifierService = new NotifierService();

        SmsProvider provider = new BulkSmsProvider();
        SmsNotifier notifier = new SmsNotifier(provider);
        notifierService.send(
                "CONGRATULATION_1",
                singletonList(
                        PersonContact.builder()
                                .email("fedorov242@gmail.com")
                                .phone("79254432754")
                                .build()
                ),
                new HashMap<String, Object>() {
                    {
                        put("client_name", "Федоров Сергей Евгеньевич");
                    }
                },
                notifier
        );
    }
}

class NotifierService {
    void send(String message, List<PersonContact> contacts, Notifier notifier) {
        notifier.send(message, contacts);
    }

    void send(String templateId, List<PersonContact> contacts, Map<String, Object> params, Notifier notifier) {
        notifier.send(templateId, contacts, params);
    }
}

abstract class Notifier {

    abstract UUID send(String message, List<PersonContact> contacts);

    abstract UUID send(String templateId, List<PersonContact> contacts, Map<String, Object> params);
}

@AllArgsConstructor
class EmailNotifier extends Notifier {
    EmailProvider provider;

    public void setProvider(EmailProvider provider) {
        this.provider = provider;
    }

    @Override
    public UUID send(String message, List<PersonContact> contacts) {
        System.out.println("Send email message to client");
        return provider.send(message, contacts);
    }

    @Override
    UUID send(String templateId, List<PersonContact> contacts, Map<String, Object> params) {
        System.out.println("Send email template message to client");
        return provider.send(templateId, contacts, params);
    }
}

@AllArgsConstructor
class SmsNotifier extends Notifier {
    SmsProvider provider;

    public void setProvider(SmsProvider provider) {
        this.provider = provider;
    }

    @Override
    public UUID send(String message, List<PersonContact> contacts) {
        System.out.println("Send sms message to client");
        return provider.send(message, contacts);
    }

    @Override
    UUID send(String templateId, List<PersonContact> contacts, Map<String, Object> params) {
        System.out.println("Send sms template message to client");
        return provider.send(templateId, contacts, params);
    }
}

interface Provider {
    UUID send(String message, List<PersonContact> contacts);

    UUID send(String templateId, List<PersonContact> contacts, Map<String, Object> params);
}

interface SmsProvider extends Provider {
}

interface EmailProvider extends Provider {
}

class JangoEmailProvider implements EmailProvider {

    @Override
    public UUID send(String message, List<PersonContact> contacts) {
        System.out.println("Send email message by Jango mail provider");
        return randomUUID();
    }

    @Override
    public UUID send(String templateId, List<PersonContact> contacts, Map<String, Object> params) {
        System.out.println("Send email template message by Jango mail provider");
        return randomUUID();
    }
}

class BulkSmsProvider implements SmsProvider {

    @Override
    public UUID send(String message, List<PersonContact> contacts) {
        System.out.println("Send sms message by BulkSms sms provider");
        return randomUUID();
    }

    @Override
    public UUID send(String templateId, List<PersonContact> contacts, Map<String, Object> params) {
        System.out.println("Send sms template message by BulkSms sms provider");
        return randomUUID();
    }
}

@Builder
@Data
class PersonContact {
    private String phone;
    private String email;
}