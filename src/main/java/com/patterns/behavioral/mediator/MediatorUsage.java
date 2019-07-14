package com.patterns.behavioral.mediator;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class MediatorUsage {
    public static void main(String[] args) {
        MediatorUsage executor = new MediatorUsage();
        executor.execute();
    }

    private void execute() {
        Conversation conversation = new Conversation();
        Participant firstParticipant = conversation.register("Вася");
        Participant secondParticipant = conversation.register("Артем");
        Participant thirdParticipant = conversation.register("Федор");

        firstParticipant.send("Привет", secondParticipant.name);
        thirdParticipant.send("Привет Всем",  null);
        secondParticipant.send("Как твои дела?", firstParticipant.name);
    }
}

/**
 * Пример посредника
 * который инкапсулирует логику взаимодействия между участниками
 * без их взаимодействия друг с другом
 */
class Conversation {
    Map<String, Participant> participants = new HashMap<>();

    public Participant register(String name) {
        System.out.println(format("Participant '%s' register to chat", name));
        Participant participant = new Participant(name, this);
        participants.put(name, participant);
        return participant;
    }

    public void send(String message, String from, String to) {
        if (to != null) {
            participants.get(to).receive(message, from);
        } else {
            participants.values()
                    .stream()
                    .filter(participant -> !participant.name.equals(from))
                    .forEach(participant -> participant.receive(message, from));
        }
    }
}

@Data
class Participant {
    String name;
    Conversation conversation;

    public Participant(String name, Conversation conversation) {
        this.name = name;
        this.conversation = conversation;
    }

    public void send(String message, String to) {
        System.out.println(format("Participant '%s' sent message '%s' to '%s'", name, message, to));
        conversation.send(message, name, to);
    }

    public void receive(String message, String from) {
        System.out.println(format("Participant '%s' receive message '%s' from '%s'", name, message, from));
    }
}