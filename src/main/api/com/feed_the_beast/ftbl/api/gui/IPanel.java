package com.feed_the_beast.ftbl.api.gui;

import java.util.Collection;

/**
 * Created by LatvianModder on 04.09.2016.
 */
public interface IPanel extends IWidget
{
    int FLAG_ONLY_RENDER_WIDGETS_INSIDE = 1;
    int FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE = 2;
    int FLAG_UNICODE_FONT = 4;
    int FLAG_DEFAULTS = FLAG_ONLY_RENDER_WIDGETS_INSIDE | FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE;

    default void add(IWidget w)
    {
        w.setParentPanel(this);
        getWidgets().add(w);

        if(w instanceof IPanel)
        {
            ((IPanel) w).refreshWidgets();
        }
    }

    default void addAll(Iterable<? extends IWidget> l)
    {
        for(IWidget w : l)
        {
            add(w);
        }
    }

    Collection<IWidget> getWidgets();

    void refreshWidgets();

    default void updateWidgetPositions()
    {
    }

    boolean hasFlag(int flag);

    void setScrollX(int scroll);

    void setScrollY(int scroll);

    default void setScrollX(double scroll, int elementsWidth)
    {
        int width = getWidth();
        if(elementsWidth < width)
        {
            setScrollX(0);
        }
        else
        {
            setScrollX((int) (scroll * (elementsWidth - width)));
        }
    }

    default void setScrollY(double scroll, int elementsHeight)
    {
        int height = getHeight();
        if(elementsHeight < height)
        {
            setScrollY(0);
        }
        else
        {
            setScrollY((int) (scroll * (elementsHeight - height)));
        }
    }
}