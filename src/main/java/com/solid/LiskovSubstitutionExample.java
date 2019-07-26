package com.solid;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Поведение наследников должно быть эквивалентно поведению базовых классов
 */
public class LiskovSubstitutionExample {
    public static void main(String[] args) {
        LiskovSubstitutionExample executor = new LiskovSubstitutionExample();
        executor.execute();
    }

    private void execute() {
        new JobService(new CustomPostgresRepository()).findAll();
    }
}

@AllArgsConstructor
class JobService {
    private JobRepository jobRepository;

    public List<Job> findAll() {
        return jobRepository.findAll();
    }
}

/**
 * поведение в любом случае не измениться так как на выходе мы будем иметь список jobs
 */
class CustomPostgresRepository extends PostgresJobRepository {
    @Override
    public List<Job> findAll() {
        //may be additional aggregate data from external service or additional log
        return super.findAll();
    }
}

class PostgresJobRepository implements JobRepository {

    @Override
    public List<Job> findAll() {
        //connect to postgres, load data, convert data to abstraction
        return ImmutableList.of(new Job());
    }
}

class MongoJobRepository implements JobRepository {

    @Override
    public List<Job> findAll() {
        //connect to mongo db, load data, convert data to abstraction
        return ImmutableList.of(new Job());
    }
}

interface JobRepository {
    List<Job> findAll();
}

class Job {
}
