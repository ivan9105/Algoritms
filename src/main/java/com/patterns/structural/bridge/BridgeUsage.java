package com.patterns.structural.bridge;

import java.util.HashMap;
import java.util.Map;

public class BridgeUsage {
    /**
     * разделение 2 или более видов классов на несколько иерархий
     * позволяя менять их независимо друг от друга
     * т.е. при добавлении нового вида отчета или другого вида источника данных
     * можно будет реализовавать 2 иерархии независимо друг от друга
     */
    public static void main(String[] args) {
        Template template = new Template();
        ExternalWSSource wsSource = new ExternalWSSource();
        ElasticSearchSource elasticSource = new ElasticSearchSource();

        HtmlReportEngine<ExternalWSSource> htmlWithXmlSource = new HtmlReportEngine<>(template, wsSource);
        htmlWithXmlSource.generateReport();

        HtmlReportEngine<ElasticSearchSource> htmlWithJsonSource = new HtmlReportEngine<>(template, elasticSource);
        htmlWithJsonSource.generateReport();
    }
}

class HtmlReportEngine<T extends Source> extends ReportEngine<T> {
    public HtmlReportEngine(Template template, T source) {
        super(template, source);
    }

    @Override
    byte[] generateReport() {
        System.out.println("Generate html report, get data from " + source.getType() + " source");
        //logic generate html
        return new byte[0];
    }
}

class PdfReportEngine<T extends Source> extends ReportEngine<T> {
    public PdfReportEngine(Template template, T source) {
        super(template, source);
    }

    @Override
    byte[] generateReport() {
        System.out.println("Generate html report, get data from " + source.getType() + " source");
        //logic generate pdf
        return new byte[0];
    }
}

class CsvReportEngine<T extends Source> extends ReportEngine<T> {
    public CsvReportEngine(Template template, T source) {
        super(template, source);
    }

    @Override
    byte[] generateReport() {
        System.out.println("Generate html report, get data from " + source.getType() + " source");
        //logic generate csv
        return new byte[0];
    }
}


abstract class ReportEngine<T extends Source> {
    protected Template template;
    protected T source;

    public ReportEngine(Template template, T source) {
        this.template = template;
        this.source = source;
    }

    abstract byte[] generateReport();

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}

abstract class Source {
    protected Script script;
    abstract Map<String, Object> getData();
    abstract String getType();

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }
}

class ElasticSearchSource extends Source {
    @Override
    Map<String, Object> getData() {
        //get data from elastic cluster
        return new HashMap<>();
    }

    @Override
    String getType() {
        return "json";
    }
}

class ExternalWSSource extends Source {
    @Override
    Map<String, Object> getData() {
        //get data from external ws
        return new HashMap<>();
    }

    @Override
    String getType() {
        return "xml";
    }
}

class SqlSource extends Source {
    @Override
    Map<String, Object> getData() {
        //get data from sql
        return new HashMap<>();
    }

    @Override
    String getType() {
        return "sql";
    }
}

class Script {
    private String query;
    private Map<String, Object> params;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}

class Template {
    private String template;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}