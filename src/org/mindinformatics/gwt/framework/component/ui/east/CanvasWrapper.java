package org.mindinformatics.gwt.framework.component.ui.east;

import org.mindinformatics.gwt.domeo.client.IDomeo;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Window;

public class CanvasWrapper {

    IDomeo _domeo;
    
    private Canvas canvas;
    private Context2d context;
    
    public CanvasWrapper(IDomeo domeo) {
        _domeo = domeo;
        
        canvas = Canvas.createIfSupported();
        canvas.setWidth("100%");
        canvas.setHeight("100%");
        
        canvas.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event)
            {
//                Window.alert(canvas.getOffsetHeight() + " hit " + event.getX() + " - " + event.getY());
//                Window.alert("y% " + );
                _domeo.jumpToLocation((100*(float)event.getY())/(float)canvas.getOffsetHeight());
            }
        });
      
        
        context = canvas.getContext2d();
        context.setLineWidth(200);
        
//      context.setStrokeStyle("red");
//      context.beginPath();
//        context.moveTo(150, 0);
//        context.lineTo(150, 150);
//        context.stroke();
//        context.closePath();
        
//        context.setStrokeStyle("yellow");
//        context.beginPath();
//        context.moveTo(150, 0);
//        context.lineTo(150, 20);
//        context.stroke();
//        context.closePath();
        
//        context.setStrokeStyle("green");
//        context.beginPath();
//        context.moveTo(150, 40);
//        context.lineTo(150, 41);
//        context.stroke();
//        context.closePath();
        
//        context.setStrokeStyle("gray");
//        context.setLineWidth(100);
//        context.beginPath();
//        context.moveTo(240, 75);
//        context.lineTo(240, 76);
//        context.stroke();
//        context.closePath();
        
        //createAnnotationLine(30);
        //createHighligthLine(40);
        //createEqualMarkers();
    }
    
    public void createAnnotationLine(float percentageY) {
        
        float y = (percentageY*150.0f)/100.0f;
        
        //context.setStrokeStyle("#B0C4DE");
        context.setLineWidth(10);
        context.setFillStyle("#B0C4DE");
        context.fillRect(0, y, 300, 1);
        
//        context.beginPath();
//        context.moveTo(150, y);
//        context.lineTo(150, y+0.5);
//        context.stroke();
//        context.closePath();
    }
    
    public void createHighligthLine(float percentageY) {
        
        float y = (percentageY*150.0f)/100.0f;
        
        //context.setStrokeStyle("yellow");
        context.setLineWidth(10);
        context.setFillStyle("yellow");
        context.fillRect(0, y, 300, 1);
        
        /*context.beginPath();
        context.moveTo(150, y);
        context.lineTo(150, y+0.5);
        context.stroke();
        context.closePath();*/
    }
    
    public void createSectionLine(float percentageY) {
        
        float y = (percentageY*150.0f)/100.0f;
        
        context.setStrokeStyle("green");
        context.setLineWidth(200);
        context.beginPath();
        context.moveTo(240, y);
        context.lineTo(240, y+0.5);
        context.stroke();
        context.closePath();
    }
    
    public void createEqualMarkers() {
        createSectionLine(25);
        createSectionLine(50);
        createSectionLine(75);
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
}
