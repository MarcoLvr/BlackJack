package me.marcolvr.client.graphics.component;


import me.marcolvr.client.graphics.listener.LvrActionListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;

public class LvrLabel extends JLabel implements LvrComponent{

    private LvrActionListener listener;

    public LvrLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        listener=new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        listener=new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrLabel(String text) {
        super(text);
        listener=new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        listener=new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrLabel(Icon image) {
        super(image);
        listener=new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrLabel() {
        listener=new LvrActionListener();
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
    }}
