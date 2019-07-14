package com.patterns.behavioral.snapshot;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Collections.singletonList;

public class MementoUsage {
    public static void main(String[] args) {
        MementoUsage executor = new MementoUsage();
        executor.execute();
    }

    /**
     * Поведенческий паттерн снимок
     */
    private void execute() {
        Job job = Job.builder()
                .account(new Account())
                .customers(singletonList(new Customer()))
                .driver(new Driver())
                .promocode(new Promocode())
                .status(JobStatus.NEW)
                .build();

        JobSnapshot snapshot = new JobSnapshot(job);

        System.out.println("Before restore");
        job.setStatus(JobStatus.CANCELLED);
        System.out.println(job.toString());

        job = snapshot.restore();

        System.out.println("After restore");
        System.out.println(job.toString());
    }
}

@Data
class JobSnapshot implements Memento<Job> {
    private LocalDateTime date;
    private Job jobData;

    public JobSnapshot(Job job) {
        jobData = (Job) job.clone();
        date = LocalDateTime.now();
        System.out.println(format("Backup job '%s', date '%s'", jobData, date.format(ISO_DATE_TIME)));
    }

    @Override
    public Job restore() {
        System.out.println(format("Restore job '%s', date '%s'", jobData, date.format(ISO_DATE_TIME)));
        return jobData;
    }
}

interface Memento<T> {
    T restore();
}

@Builder
@Data
class Job implements Cloneable {
    private List<Customer> customers;
    private Driver driver;
    private Account account;
    private Service service;
    private Promocode promocode;
    private JobStatus status;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ignore) {
        }
        return null;
    }
}

class Customer {
}

class Driver {
}

class Account {
}

class Service {
}

class Promocode {
}

enum JobStatus {
    DRAFT,
    NEW,
    IN_PROGRESS,
    CANCELLED,
    ERROR,
    DONE
}