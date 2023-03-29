package me.marcolvr.client.graphics.component;


import me.marcolvr.client.graphics.listener.LvrActionListener;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class LvrTextField extends JTextField implements LvrComponent{

    private LvrActionListener listener;

    public LvrTextField() {
        listener=new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrTextField(String text) {
        super(text);
        listener=new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrTextField(int columns) {
        super(columns);
        listener=new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrTextField(String text, int columns) {
        super(text, columns);
        listener=new LvrActionListener();
        addActionListener(listener);
        addMouseListener(listener);

    }

    public LvrTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
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
