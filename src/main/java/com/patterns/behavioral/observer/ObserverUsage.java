package com.patterns.behavioral.observer;

import com.google.common.collect.ImmutableSet;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;

public class ObserverUsage {
    /**
     * поведенческий паттерн проектирования,
     * который создаёт механизм подписки,
     * позволяющий одним объектам следить и реагировать на события,
     * происходящие в других объектах.
     */
    public static void main(String[] args) {
        ObserverUsage executor = new ObserverUsage();
        executor.execute();
    }

    private void execute() {
        EntityManager em = new EntityManager();
        em.addListener(new JobEntityListener());

        JobEntity job = em.find(JobEntity.class, 1L);
        job.setNumber("662345");
        em.merge(job);
    }
}

class EntityManager {
    private List<EntityListener> listeners = new ArrayList<>();

    public <T> T find(Class<T> entityClass, Object primaryKey) {
        //process jpa framework's difficult logic
        return (T) JobEntity.builder()
                .id(1L)
                .number("354002")
                .build();
    }

    public <T> T merge(T entity) {
        preUpdate(entity);
        doMerge(entity);
        postUpdate(entity);
        return entity;
    }

    private <T> Set<String> getDirtyField(T entity) {
        System.out.println("Reload entity from persistence storage, define dirty fields process");
        return ImmutableSet.of("number");
    }

    private <T> void preUpdate(T entity) {
        Optional<EntityListener> listenerOptional = listeners.stream().filter(entityListener ->
                entityListener.getEntityClass().equals(entity.getClass()) &&
                        entityListener instanceof PreUpdateEntityListener
        ).findFirst();

        if (listenerOptional.isPresent()) {
            ((PreUpdateEntityListener) listenerOptional.get()).preUpdate(entity, getDirtyField(entity));
        }
    }

    private <T> void postUpdate(T entity) {
        Optional<EntityListener> listenerOptional = listeners.stream().filter(entityListener ->
                entityListener.getEntityClass().equals(entity.getClass()) &&
                        entityListener instanceof PostUpdateEntityListener
        ).findFirst();

        if (listenerOptional.isPresent()) {
            ((PostUpdateEntityListener) listenerOptional.get()).postUpdate(entity);
        }
    }

    private <T> T doMerge(T entity) {
        System.out.println("Save entity in persistence storage and reload");
        return entity;
    }

    void addListener(EntityListener listener) {
        listeners.add(listener);
    }
}

class JobEntityListener implements PreUpdateEntityListener<JobEntity>, PostUpdateEntityListener<JobEntity> {

    @Override
    public void preUpdate(JobEntity entity, Set<String> dirtyFields) {
        System.out.println(format("Call pre update, dirty fields '%s'", dirtyFields));
    }

    @Override
    public void postUpdate(JobEntity updated) {
        System.out.println("Call post update");
    }

    @Override
    public Class getEntityClass() {
        return JobEntity.class;
    }
}

interface PreUpdateEntityListener<T> extends EntityListener {
    void preUpdate(T entity, Set<String> dirtyFields);
}

interface PostUpdateEntityListener<T> extends EntityListener {
    void postUpdate(T updated);
}

interface EntityListener {
    Class getEntityClass();
}

@Builder
@Data
class JobEntity {
    Long id;
    String number;
}