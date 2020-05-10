package com.patterns.behavioral.chain_responsibillity;

public class ChainResponsibilityUsage {
    /**
     * Пример Package org.springframework.web.filter
     *
     * поведенческий паттерн проектирования, который позволяет передавать запросы
     * последовательно по цепочке обработчиков. Каждый последующий обработчик решает,
     * может ли он обработать запрос сам и стоит ли передавать запрос дальше по цепи.
     */
    public static void main(String[] args) {
        ChainResponsibilityUsage executor = new ChainResponsibilityUsage();
        executor.execute();
    }

    /**
     * Цепочка фильтров
     */
    private void execute() {
        new OAuth2Filter(
                new AuthorizationFilter(
                        new CorsFilter(
                                new RememberMeFilter(null)
                        )
                )
        ).filter(new HttpRequest());
    }
}

abstract class Filter {
    protected Filter next;

    public Filter(Filter next) {
        this.next = next;
    }

    void filter(HttpRequest request) {
        doFilter(request);

        if (next != null) {
            next.filter(request);
        }
    }

    abstract void doFilter(HttpRequest request);
}

class OAuth2Filter extends Filter {

    public OAuth2Filter(Filter next) {
        super(next);
    }

    @Override
    void doFilter(HttpRequest request) {
        System.out.println("Validate and parse oauth2 token...");
    }
}

class AuthorizationFilter extends Filter {

    public AuthorizationFilter(Filter next) {
        super(next);
    }

    @Override
    void doFilter(HttpRequest request) {
        System.out.println("Authorize process...");
    }
}

class CorsFilter extends Filter {

    public CorsFilter(Filter next) {
        super(next);
    }

    @Override
    void doFilter(HttpRequest request) {
        System.out.println("Check permissions for origin...");
    }
}

class RememberMeFilter extends Filter {

    public RememberMeFilter(Filter next) {
        super(next);
    }

    @Override
    void doFilter(HttpRequest request) {
        System.out.println("Populate thread local context auth info...");
    }
}

class HttpRequest {
}
