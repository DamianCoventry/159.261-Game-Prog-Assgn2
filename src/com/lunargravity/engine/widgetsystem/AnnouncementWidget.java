package com.lunargravity.engine.widgetsystem;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class AnnouncementWidget extends WidgetObserver {
    private static final String MAJOR_TITLE_IMAGE = "majorTitleImage";
    private static final String MAJOR_TITLE_BLEND_IMAGE = "majorTitleBlendImage";
    private static final String MINOR_TITLE_IMAGE = "minorTitleImage";
    private static final String MINOR_TITLE_BLEND_IMAGE = "minorTitleBlendImage";
    private static final String SUB_TITLE_IMAGE = "subTitleImage";
    private static final String SUB_TITLE_BLEND_IMAGE = "subTitleBlendImage";

    private IAnnouncementObserver _observer;

    public AnnouncementWidget(WidgetManager widgetManager, IAnnouncementObserver observer) {
        super(widgetManager);
        _observer = observer;
    }

    // TODO: requires 1 or more LabelWidgets to be centered vertically and horizontally
    //   requires 1 ore more LabelWidgets to be anchored to the bottom right of the viewport
    //   requires 1 background ImageWidget
    //   requires 1 or more fonts

    @Override
    protected void initialiseChildren(WidgetCreateInfo wci) throws IOException {
        WidgetCreateInfo child = wci.getChild(MAJOR_TITLE_IMAGE, "ImageWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ImageWidget(_widgetManager)));
        }
        child = wci.getChild(MAJOR_TITLE_BLEND_IMAGE, "ImageWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ImageWidget(_widgetManager)));
        }
        child = wci.getChild(MINOR_TITLE_IMAGE, "ImageWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ImageWidget(_widgetManager)));
        }
        child = wci.getChild(MINOR_TITLE_BLEND_IMAGE, "ImageWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ImageWidget(_widgetManager)));
        }
        child = wci.getChild(SUB_TITLE_IMAGE, "ImageWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ImageWidget(_widgetManager)));
        }
        child = wci.getChild(SUB_TITLE_BLEND_IMAGE, "ImageWidget");
        if (child != null) {
            getWidget().addChild(new Widget(_widget, child, new ImageWidget(_widgetManager)));
        }
    }

    @Override
    public void freeResources() {
        super.freeResources();
        // anything to do?
    }

    @Override
    public void mouseButtonEvent(int button, int action, int mods) throws IOException, InterruptedException {
        boolean hadMouseCapture = _widget == _widgetManager.getMouseCapture();
        super.mouseButtonEvent(button, action, mods);
        if (action == GLFW_RELEASE && hadMouseCapture && _widget == _widgetManager.getHoveringOver()) {
            _observer.announcementClicked(_widget.getId());
        }
    }
}
