package com.patterns.structural.composite;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Компоновщик
 */
public class CompositeUsage {
    /**
     * основа паттерна иерархия родственных объектов с общим интерфейсом
     * которые можно сгруппировать
     * и работать как с единым целым
     */
    public static void main(String[] args) {
        CompositeUsage executor = new CompositeUsage();
        executor.execute();
    }

    public void execute() {
        HtmlTag html = new HtmlTag();

        HeadHtmlTag head = new HeadHtmlTag();
        TitleHtmlTag title = new TitleHtmlTag();
        title.setText("Title");
        head.getChildTags().add(title);

        html.getChildTags().add(head);

        BodyHtmlTag body = new BodyHtmlTag();
        DivHtmlTag div = new DivHtmlTag();
        body.getChildTags().add(div);

        ParagraphHtmlTag paragraph = new ParagraphHtmlTag();
        paragraph.setText("Paragraph");
        div.getChildTags().add(paragraph);

        html.getChildTags().add(body);

        html.print();
    }
}

class HtmlTag extends ContainerHtmlTag {
    @Override
    String getTagName() {
        return "html";
    }
}

class HeadHtmlTag extends ContainerHtmlTag {
    @Override
    String getTagName() {
        return "head";
    }
}

class TitleHtmlTag extends AbstractHtmlTag {
    @Override
    String getTagName() {
        return "title";
    }
}

class BodyHtmlTag extends ContainerHtmlTag {
    @Override
    String getTagName() {
        return "body";
    }
}

class DivHtmlTag extends ContainerHtmlTag {
    @Override
    String getTagName() {
        return "div";
    }
}

class ParagraphHtmlTag extends AbstractHtmlTag {
    @Override
    public String getTagName() {
        return "p";
    }
}

abstract class ContainerHtmlTag extends AbstractHtmlTag {
    private List<AbstractHtmlTag> childTags = new ArrayList<>();

    public List<AbstractHtmlTag> getChildTags() {
        return childTags;
    }

    public void setChildTags(List<AbstractHtmlTag> childTags) {
        this.childTags = childTags;
    }

    public void print() {
        printTagName(true);
        for (AbstractHtmlTag childTag : childTags) {
            childTag.print();
        }
        printTagName(false);
    }

    @Override
    public void printTagName(boolean isOpen) {
        System.out.println(format(isOpen ? "<%s>" : "</%s>", getTagName()));
    }
}

abstract class AbstractHtmlTag {
    protected String id;
    protected Integer scrollLeft;
    protected Integer scrollTop;
    protected Integer clientLeft;
    protected Integer clientTop;
    protected String color;
    protected String text;
    //other properties...

    abstract String getTagName();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getScrollLeft() {
        return scrollLeft;
    }

    public void setScrollLeft(Integer scrollLeft) {
        this.scrollLeft = scrollLeft;
    }

    public Integer getScrollTop() {
        return scrollTop;
    }

    public void setScrollTop(Integer scrollTop) {
        this.scrollTop = scrollTop;
    }

    public Integer getClientLeft() {
        return clientLeft;
    }

    public void setClientLeft(Integer clientLeft) {
        this.clientLeft = clientLeft;
    }

    public Integer getClientTop() {
        return clientTop;
    }

    public void setClientTop(Integer clientTop) {
        this.clientTop = clientTop;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void print() {
        printTagName(true);
        if (isNotBlank(text)) {
            System.out.print(text);
        }
        printTagName(false);
        System.out.println();
    }

    protected void printTagName(boolean isOpen) {
        System.out.print(format(isOpen ? "<%s>" : "</%s>", getTagName()));
    }
}
