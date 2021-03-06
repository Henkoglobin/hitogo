package org.hitogo.alert;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.hitogo.alert.core.VisibilityListener;
import org.hitogo.alert.dialog.DialogAlert;
import org.hitogo.alert.dialog.DialogAlertBuilder;
import org.hitogo.button.core.Button;
import org.hitogo.core.HitogoController;
import org.mockito.Matchers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A class which can be used to create Mocks for the DialogAlertBuilder.
 *
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class DialogAlertBuilderMock {

    /**
     * Creates a new Mock object for the DialogAlertBuilder.
     *
     * @return a new DialogAlertBuilder
     * @since 1.0.0
     */
    @NonNull
    public static DialogAlertBuilder getMock() {
        DialogAlert dialogAlertMock = mock(DialogAlert.class);
        return getMock(dialogAlertMock);
    }

    /**
     * Creates a new Mock object for the DialogAlertBuilder. The given DialogAlert can be used to
     * define the return value for the method {@code build()}.
     *
     * @param alert a DialogAlert
     * @return a new DialogAlertBuilder
     * @since 1.0.0
     */
    @NonNull
    public static DialogAlertBuilder getMock(@NonNull DialogAlert alert) {
        DialogAlertBuilder dialogAlertBuilder = mock(DialogAlertBuilder.class);

        when(dialogAlertBuilder.addButton(anyInt())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.addButton(anyString())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.addButton(Matchers.<Button>anyVararg())).thenReturn(dialogAlertBuilder);

        when(dialogAlertBuilder.asDismissible()).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.asDismissible(any(Button.class))).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.asDismissible(anyBoolean())).thenReturn(dialogAlertBuilder);

        when(dialogAlertBuilder.setTitle(anyString())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.setTitle(anyInt())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.setTitle(anyInt(), anyInt())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.setTitle(anyInt(), anyString())).thenReturn(dialogAlertBuilder);

        when(dialogAlertBuilder.setState(anyInt())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.setState(any(Enum.class))).thenReturn(dialogAlertBuilder);

        when(dialogAlertBuilder.addText(anyInt())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.addText(anyString())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.addText(anyInt(), anyInt())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.addText(anyInt(), anyString())).thenReturn(dialogAlertBuilder);

        when(dialogAlertBuilder.addDrawable(anyInt())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.addDrawable(any(Drawable.class))).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.addDrawable(anyInt(), anyInt())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.addDrawable(anyInt(), any(Drawable.class))).thenReturn(dialogAlertBuilder);

        when(dialogAlertBuilder.setStyle(anyInt())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.setBundle(any(Bundle.class))).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.setController(any(HitogoController.class))).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.setLayout(anyInt())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.setTag(anyString())).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.addVisibilityListener(any(VisibilityListener.class))).thenReturn(dialogAlertBuilder);
        when(dialogAlertBuilder.setPriority(anyInt())).thenReturn(dialogAlertBuilder);

        when(dialogAlertBuilder.build()).thenReturn(alert);

        return dialogAlertBuilder;
    }
}
