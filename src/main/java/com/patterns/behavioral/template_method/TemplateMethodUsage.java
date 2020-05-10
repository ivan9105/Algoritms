package com.patterns.behavioral.template_method;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;

import static com.patterns.behavioral.template_method.TransactionUtils.runInTransaction;

public class TemplateMethodUsage {
    /**
     * это поведенческий паттерн проектирования,
     * который определяет скелет алгоритма,
     * перекладывая ответственность за некоторые его шаги на подклассы.
     * Паттерн позволяет подклассам переопределять шаги алгоритма, не меняя его общей структуры.
     */
    public static void main(String[] args) {
        TemplateMethodUsage executor = new TemplateMethodUsage();
        executor.execute();
    }

    private void execute() {
        runInTransaction((em) -> {
            Entity entity = new Entity();
            em.persist(entity);
            em.reload(1L, entity.getClass());
            System.out.println("Нужно вспомнить java 8 functional interfaces");
        });
    }
}

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class TransactionUtils {
    static void runInTransaction(Consumer<EntityManager> consumer) {
        Transaction tx = new Transaction();
        try {
            tx.begin();

            EntityManager em = tx.getEntityManager();
            consumer.accept(em);

            tx.commit();
        } finally {
            tx.end();
        }
    }
}

class Transaction {
    void begin() {
        System.out.println("Begin transaction...");
    }

    void commit() {
        System.out.println("Commit transaction...");
    }

    void end() {
        System.out.println("End transaction...");
    }

    EntityManager getEntityManager() {
        return new EntityManager();
    }
}

class EntityManager {
    void persist(Entity entity) {
        System.out.println("Persist entity...");
    }

    void reload(Long id, Class<? extends Entity> cls) {
        System.out.println("Reload...");
    }

    //etc
}

class Entity {
}

