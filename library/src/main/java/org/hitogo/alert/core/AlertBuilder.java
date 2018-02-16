package org.hitogo.alert.core;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import org.hitogo.button.core.Button;
import org.hitogo.core.HitogoController;
import org.hitogo.core.HitogoHelper;

/**
 * Public api interface for the AlertBuilder. This interface includes all methods that can be
 * used by this builder.
 *
 * @param <B> Type of the AlertBuilder that is using this interface
 * @param <A> Type of the Alert object that will be generated by this builder
 * @see Alert
 * @since 1.0.0
 */
public interface AlertBuilder<B, A extends Alert> extends AlertBuilderBase<B, A> {

    /**
     * Adds a title to this alert which can be used inside the alert implementation. This method
     * implementation will use the provideDefaultTitleViewId(Integer) method offered by the
     * HitogoController. This method will define the default view id for this title.
     *
     * @param title Title for the alert.
     * @return Builder object which has called this method.
     * @see HitogoController
     * @since 1.0.0
     */
    @NonNull
    B setTitle(@NonNull String title);

    /**
     * Adds a title resource to this alert which can be used inside the alert implementation. This
     * method implementation will use the provideDefaultTitleViewId(Integer) method offered by the
     * HitogoController. This method will define the default view id for this title. The string
     * resource will be translated by the builder using the HitogoHelper.getString(int).
     *
     * @param titleRes String resource which is used for the title.
     * @return Builder object which has called this method.
     * @see HitogoHelper
     * @see HitogoController
     * @since 1.0.0
     */
    @NonNull
    B setTitle(@StringRes int titleRes);

    /**
     * Adds a title resource and it's related view id to this alert which can be used inside the
     * alert implementation. The string resource will be translated by the builder using the
     * HitogoHelper.getString(int).
     *
     * @param viewId   View id which is going to use the title (optional).
     * @param titleRes String resource which is used for the title.
     * @return Builder object which has called this method.
     * @see HitogoHelper
     * @since 1.0.0
     */
    @NonNull
    B setTitle(@IdRes @Nullable Integer viewId, @StringRes int titleRes);

    /**
     * Adds a title to this alert which can be used inside the alert implementation.
     *
     * @param viewId View id which is going to use the title (optional).
     * @param title  Title for the alert.
     * @return Builder object which has called this method.
     * @since 1.0.0
     */
    @NonNull
    B setTitle(@IdRes @Nullable Integer viewId, @NonNull String title);

    /**
     * Adds a text string resource to this alert which can be used inside the alert implementation.
     * Alerts can have more than one text element. If more than one text element is defined, each
     * text element will need it's own view (id). The string resource will be translated by the
     * builder using the HitogoHelper.getString(int).
     *
     * @param viewId  View id which is going to use the text element (optional).
     * @param textRes Text element for the alert object.
     * @return Builder object which has called this method.
     * @see HitogoHelper
     * @since 1.0.0
     */
    @NonNull
    B addText(@IdRes @Nullable Integer viewId, @StringRes int textRes);

    /**
     * Adds a text element to this alert which can be used inside the alert implementation. Alerts
     * can have more than one text element. If more than one text element is defined, each text
     * element will need it's own view (id).
     *
     * @param viewId View id which is going to use the text element (optional).
     * @param text   Text element for the alert object.
     * @return Builder object which has called this method.
     * @since 1.0.0
     */
    @NonNull
    B addText(@IdRes @Nullable Integer viewId, @NonNull String text);

    /**
     * Adds a button to the alert. Buttons are an abstract container for all needed information
     * that one button could have (title, listener, view id, ...).
     *
     * @param buttons One or more button/s for the alert.
     * @return Builder object which has called this method.
     * @see Button
     * @since 1.0.0
     */
    @NonNull
    B addButton(@NonNull Button... buttons);

    /**
     * Sets a custom layout resource id for the alert. Usually this method should only be when
     * using an unique layout for this certain alert. Otherwise the more general method
     * provide(...)Layout offered by the HitogoController should be used to define the common cases
     * using alert states.
     *
     * @param layoutRes Layout res id for the alert.
     * @return Builder object which has called this method.
     * @see HitogoController
     * @since 1.0.0
     */
    @NonNull
    B setLayout(@LayoutRes int layoutRes);

    /**
     * Adds a drawable to the alert which can be used inside the alert implementation. This
     * method implementation will use the provideDefaultDrawableViewId(Integer) method offered by
     * the HitogoController. This method will define the default view id for this drawable. Alerts
     * can have more than one drawable. If more than one drawable is defined, the method
     * addDrawable(Integer, int) should rather be used to include a view id that can differ between
     * the drawables. The drawable resource will be translated by the builder using the
     * HitogoAccessor.getDrawable(int).
     *
     * @param drawableRes an int which represents the image
     * @return Builder object which has called this method.
     * @see Button
     * @since 1.0.0
     */
    @NonNull
    B addDrawable(@DrawableRes int drawableRes);

    /**
     * Adds a drawable to the alert which can be used inside the alert implementation. This
     * method implementation will use the provideDefaultDrawableViewId(Integer) method offered by
     * the HitogoController. This method will define the default view id for this drawable. Alerts
     * can have more than one drawable. If more than one drawable is defined, the method
     * addDrawable(Integer, Drawable) should rather be used to include a view id that can differ
     * between the drawables.
     *
     * @param drawable a Drawable
     * @return Builder object which has called this method.
     * @see Button
     * @since 1.0.0
     */
    @NonNull
    B addDrawable(@NonNull Drawable drawable);

    /**
     * Adds a drawable to the alert which can be used inside the alert implementation. Alerts can
     * have more than one drawable. The drawable resource will be translated by the builder using the
     * HitogoAccessor.getDrawable(int).
     *
     * @param drawableRes an int which represents the drawable
     * @return Builder object which has called this method.
     * @see Button
     * @since 1.0.0
     */
    @NonNull
    B addDrawable(@IdRes @Nullable Integer viewId, @DrawableRes int drawableRes);

    /**
     * Adds a drawable to the alert which can be used inside the alert implementation. Alerts can
     * have more than one drawable.
     *
     * @param drawable a Drawable
     * @return Builder object which has called this method.
     * @see Button
     * @since 1.0.0
     */
    @NonNull
    B addDrawable(@IdRes @Nullable Integer viewId, @NonNull Drawable drawable);
}
