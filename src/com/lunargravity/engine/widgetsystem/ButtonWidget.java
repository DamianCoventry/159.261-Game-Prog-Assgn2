package com.lunargravity.engine.widgetsystem;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class ButtonWidget extends WidgetObserver {
    private final IButtonObserver _observer;

    public ButtonWidget(WidgetManager widgetManager, IButtonObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        super.initialiseChildren(wci);
        // TODO: need to examine the wci structure and pass the correct info to each of these ctor calls
    }

    @Override
    public void freeResources() {
        super.freeResources();
        // TODO
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) {
        boolean hadMouseCapture = _widget == _widgetManager.getMouseCapture();
        super.mouseButtonEvent(button, action, mods);
        if (action == GLFW_RELEASE && hadMouseCapture && _widget == _widgetManager.getHoveringOver()) {
            _observer.buttonClicked(_widget.getId());
        }
    }
}
