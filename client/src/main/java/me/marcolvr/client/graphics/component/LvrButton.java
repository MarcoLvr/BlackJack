package me.marcolvr.client.graphics.component;


import me.marcolvr.client.graphics.listener.LvrActionListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class LvrButton extends JButton implements LvrComponent{



    private LvrActionListener listener;

    public LvrButton() {
        listener = new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);
    }

    public LvrButton(Icon icon) {
        super(icon);
        listener = new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrButton(String text) {
        super(text);
        listener = new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrButton(Action a) {
        super(a);
        listener = new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrButton(String text, Icon icon) {
        super(text, icon);
        listener = new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public void setBoundsRelative(JComponent component, int x, int y, int width, int height){
        setBounds(component.getX()+x, component.getY()+y, component.getWidth()+width, component.getHeight()+height);
    }

    @Override
    public void onComponentEvent(Consumer<ActionEvent> action){
        listener.addComponentActionEvent(action);
    }

    @Override
    public void onMouseClick(Consumer<MouseEvent> action){
        listener.addMouseClickEvent(action);
    }

    @Override
    public void onMouseHold(Consumer<MouseEvent> action){
        listener.addMouseHoldEvent(action);
    }

    @Override
    public void onMouseRelease(Consumer<MouseEvent> action){
        listener.addMouseReleaseEvent(action);
    }

    @Override
    public void onMouseEnter(Consumer<MouseEvent> action){
        listener.addMouseEnterEvent(action);
    }

    @Override
    public void onMouseExit(Consumer<MouseEvent> action){
        listener.addMouseExitEvent(action);
    }

}
