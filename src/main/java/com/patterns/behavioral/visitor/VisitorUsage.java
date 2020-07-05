package com.patterns.behavioral.visitor;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.patterns.behavioral.visitor.XmlPlaceCollectorVisitor.*;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class VisitorUsage {
    /**
     * поведенческий паттерн проектирования,
     * который позволяет добавлять в программу новые операции,
     * не изменяя классы объектов, над которыми эти операции могут выполняться.
     */
    public static void main(String[] args) {
        VisitorUsage executor = new VisitorUsage();
        executor.execute();
    }

    private void execute() {
        XmlPlaceCollectorVisitor visitor = new XmlPlaceCollectorVisitor();

        XmlElement root = XmlElement.builder()
                .name("root")
                .child(asList(
                        XmlElement.builder()
                                .name(PLACE_ELEMENT_NAME)
                                .attributes(asList(
                                        XmlAttribute.builder().
                                                name(PLACE_NAME_ATTR_NAME)
                                                .value("Россия")
                                                .build(),
                                        XmlAttribute.builder()
                                                .name(VERTECES_ATTR_NAME)
                                                .value("144,2;4,5;665,323;1234,5353")
                                                .build()
                                ))
                                .child(singletonList(
                                        XmlElement.builder()
                                                .name(PLACE_ELEMENT_NAME)
                                                .attributes(asList(
                                                        XmlAttribute.builder().
                                                                name(PLACE_NAME_ATTR_NAME)
                                                                .value("Самара")
                                                                .build(),
                                                        XmlAttribute.builder()
                                                                .name(VERTECES_ATTR_NAME)
                                                                .value("5636,2;4,5;6654,3223;17234,53531")
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .build(),
                        XmlElement.builder()
                                .name(PLACE_ELEMENT_NAME)
                                .attributes(asList(
                                        XmlAttribute.builder().
                                                name(PLACE_NAME_ATTR_NAME)
                                                .value("Украина")
                                                .build(),
                                        XmlAttribute.builder()
                                                .name(VERTECES_ATTR_NAME)
                                                .value("6363,55;43,5;675,383;1234,1253")
                                                .build()
                                ))
                                .child(singletonList(
                                        XmlElement.builder()
                                                .name(PLACE_ELEMENT_NAME)
                                                .attributes(asList(
                                                        XmlAttribute.builder().
                                                                name(PLACE_NAME_ATTR_NAME)
                                                                .value("Одесса")
                                                                .build(),
                                                        XmlAttribute.builder()
                                                                .name(VERTECES_ATTR_NAME)
                                                                .value("5636,2;4,5;6654,3223;17234,53531")
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .build()
                ))
                .build();

        root.accept(visitor);

        List<Place> result = visitor.getResult();

    }
}

class XmlPlaceCollectorVisitor {
    static final String PLACE_ELEMENT_NAME = "place";
    static final String VERTECES_ATTR_NAME = "vertices";
    static final String PLACE_NAME_ATTR_NAME = "name";

    List<Place> res = new ArrayList<>();

    void visit(XmlElement element, Place parent) {
        if (!PLACE_ELEMENT_NAME.equals(element.getName())) {
            if (element.getChild() != null && element.getChild().size() > 0) {
                element.getChild().forEach(childElement -> visit(childElement, parent));
            }
            return;
        }

        Place place = convertToPlace(element, parent);
        if (element.getChild() != null && element.getChild().size() > 0) {
            element.getChild().forEach(childElement -> visit(childElement, place));
        }

        if (parent != null) {
            return;
        }

        res.add(place);
    }

    List<Place> getResult() {
        return res;
    }

    private Place convertToPlace(XmlElement element, Place parent) {
        Place res = Place.builder()
                .parent(parent)
                .child(new ArrayList<>())
                .vertices(getVertices(element))
                .name(getAttributeValue(element, PLACE_NAME_ATTR_NAME))
                .build();

        if (parent != null) {
            parent.getChild().add(res);
        }

        return res;
    }

    private List<Vertex> getVertices(XmlElement element) {
        return Stream.of(getAttributeValue(element, VERTECES_ATTR_NAME).split(";"))
                .map(vertexStr -> {
                    String[] arr = vertexStr.split(",");
                    return new Vertex(parseInt(arr[0]), parseInt(arr[1]));
                })
                .collect(Collectors.toList());
    }

    private String getAttributeValue(XmlElement element, String attrName) {
        return element.getAttributes().stream()
                .filter(xmlAttribute -> attrName.equals(xmlAttribute.getName()))
                .findAny().orElseThrow(() ->
                        new IllegalStateException(format("Place without %s attribute", attrName))
                ).getValue();
    }
}

@Builder
@Data
class XmlElement {
    private String name;
    private String value;
    private List<XmlAttribute> attributes;
    private XmlElement parent;
    private List<XmlElement> child;

    public void accept(XmlPlaceCollectorVisitor visitor) {
        visitor.visit(this, null);
    }
}

@Builder
@Data
class XmlAttribute {
    private String name;
    private String value;
}

@Builder
@Data
class Place {
    private String name;
    private Place parent;
    private List<Place> child;
    private List<Vertex> vertices;
}

@Builder
@Data
class Vertex {
    private int x;
    private int y;

    @Override
    public String toString() {
        return format("%s,%s", x, y);
    }
}