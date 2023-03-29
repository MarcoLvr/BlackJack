package me.marcolvr.client.graphics.component;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public interface LvrComponent {

    void onComponentEvent(Consumer<ActionEvent> action);
    void onMouseClick(Consumer<MouseEvent> action);
    void onMouseHold(Consumer<MouseEvent> action);
    void onMouseRelease(Consumer<MouseEvent> action);
    void onMouseEnter(Consumer<MouseEvent> action);
    void onMouseExit(Consumer<MouseEvent> action);
}
