package me.marcolvr.client.graphics.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class LvrActionListener implements ActionListener, MouseListener {

    private List<Consumer<ActionEvent>> componentActions;
    private HashMap<LvrMouseEventType, List<Consumer<MouseEvent>>> mouseEvents;

    public void addComponentActionEvent(Consumer<ActionEvent> action){
        componentActions.add(action);
    }

    public void addMouseClickEvent(Consumer<MouseEvent> action){
        mouseEvents.get(LvrMouseEventType.MOUSE_CLICK).add(action);
    }

    public void addMouseHoldEvent(Consumer<MouseEvent> action){
        mouseEvents.get(LvrMouseEventType.MOUSE_HOLD).add(action);
    }

    public void addMouseReleaseEvent(Consumer<MouseEvent> action){
        mouseEvents.get(LvrMouseEventType.MOUSE_RELEASE).add(action);
    }

    public void addMouseEnterEvent(Consumer<MouseEvent> action){
        mouseEvents.get(LvrMouseEventType.MOUSE_ENTER).add(action);
    }

    public void addMouseExitEvent(Consumer<MouseEvent> action){
        mouseEvents.get(LvrMouseEventType.MOUSE_EXIT).add(action);
    }

    public void clearComponentActions(){
        componentActions.clear();
    }

    public void clearMouseActions(LvrMouseEventType type){
        if(type==null){
            mouseEvents.values().forEach(List::clear);
            return;
        }
        mouseEvents.get(type).clear();
    }

    public LvrActionListener(){
        componentActions=new ArrayList<>();
        mouseEvents =new HashMap<>();
        for(LvrMouseEventType type : LvrMouseEventType.values()){
            mouseEvents.put(type, new ArrayList<>());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        componentActions.forEach(act -> act.accept(e));

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseEvents.get(LvrMouseEventType.MOUSE_CLICK).forEach(action ->{
            action.accept(e);
        });
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseEvents.get(LvrMouseEventType.MOUSE_HOLD).forEach(action ->{
            action.accept(e);
        });
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseEvents.get(LvrMouseEventType.MOUSE_RELEASE).forEach(action ->{
            action.accept(e);
        });
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseEvents.get(LvrMouseEventType.MOUSE_ENTER).forEach(action ->{
            action.accept(e);
        });
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseEvents.get(LvrMouseEventType.MOUSE_EXIT).forEach(action ->{
            action.accept(e);
        });
    }
}
