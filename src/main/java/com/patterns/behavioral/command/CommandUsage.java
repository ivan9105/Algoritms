package com.patterns.behavioral.command;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.lang.String.format;

public class CommandUsage {
    public static void main(String[] args) {
        CommandUsage executor = new CommandUsage();
        executor.execute();
    }

    /**
     * Поведенческий паттерн
     * запросы превращаются в объекты
     * аналогия класс кнопка - класс action
     * action передаются как аргументы
     * или например
     * понятия command, receiver, invoker, client
     * command - action
     * receiver - объект который аффектит выполнение команды
     * invoker - кнопка
     */
    private void execute() {
        Editor editor = new Editor();

        CutAction cutAction = new CutAction(editor);
        CopyAction copyAction = new CopyAction(editor);
        PasteAction pasteAction = new PasteAction(editor);

        Button cutButton = editor.addButton(cutAction.id(), cutAction);
        editor.addButton(copyAction.id(), copyAction);
        editor.addButton(pasteAction.id(), pasteAction);

        Shortcut pasteShortcut = editor.addShortcut("Ctrl + L", pasteAction);

        cutButton.doPress();
        pasteShortcut.doShortcut();

        editor.undo();
        editor.undo();
    }
}

class CutAction extends Action {

    public CutAction(Editor editor) {
        super(editor);
    }

    @Override
    boolean execute() {
        System.out.println("Cut");
        return true;
    }

    @Override
    String id() {
        return "Cut";
    }
}

class PasteAction extends Action {

    public PasteAction(Editor editor) {
        super(editor);
    }

    @Override
    boolean execute() {
        System.out.println("Paste");
        return true;
    }

    @Override
    String id() {
        return "Paste";
    }
}

class CopyAction extends Action {
    public CopyAction(Editor editor) {
        super(editor);
    }

    @Override
    boolean execute() {
        System.out.println("Copy");
        return true;
    }

    @Override
    String id() {
        return "Copy";
    }
}

class Editor {
    String text = "Random text";
    String clipboard = "text";

    History history = new History();
    List<Button> buttons = new ArrayList<>();
    List<Shortcut> shortcuts = new ArrayList<>();

    String getSelection() {
        System.out.println("Get selection...");
        return text;
    }

    void deleteSelection() {
        System.out.println("Delete selection...");
    }

    void replaceSelection(String textForReplacing) {
        System.out.println(format("Replace '%s' on '%s'", clipboard, textForReplacing));
    }

    void undo() {
        Action last = history.last();
        if (last != null) {
            last.undo();
        }
    }

    void executeCommand(Action action) {
        if (action.execute()) {
            history.push(action);
        }
    }

    Button addButton(String text, Action action) {
        Button button = Button.builder()
                .text(text)
                .action(action)
                .editor(this)
                .build();
        buttons.add(button);
        return button;
    }

    Shortcut addShortcut(String combination, Action action) {
        Shortcut shortcut = Shortcut.builder()
                .combination(combination)
                .action(action)
                .editor(this)
                .build();
        shortcuts.add(shortcut);
        return shortcut;
    }
}

@Builder
@Data
class Button {
    private String text;
    private Action action;
    private Editor editor;

    void doPress() {
        editor.executeCommand(action);
    }
}

@Builder
@Data
class Shortcut {
    private String combination;
    private Action action;
    private Editor editor;

    void doShortcut() {
        editor.executeCommand(action);
    }
}

abstract class Action {
    protected Editor editor;
    protected String backup;

    public Action(Editor editor) {
        this.editor = editor;
    }

    void backup() {
        this.backup = editor.text;
    }

    void undo() {
        this.editor.text = this.backup;
    }

    abstract boolean execute();

    abstract String id();
}

class History {
    Stack<Action> actions = new Stack<>();

    void push(Action action) {
        System.out.println(format("Push action %s", action.id()));
        actions.push(action);
    }

    Action last() {
        Action last = actions.pop();
        if (last == null) {
            return null;
        }
        System.out.println(format("Undo action %s", last.id()));
        return last;
    }
}