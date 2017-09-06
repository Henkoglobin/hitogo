package org.hitogo.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.XmlRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

import org.hitogo.core.button.HitogoButton;
import org.hitogo.core.HitogoController;
import org.hitogo.core.HitogoObject;
import org.hitogo.core.StringUtils;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class HitogoViewBuilder {

    String title;
    String text;

    Integer state;
    Integer containerId;
    Integer titleViewId;
    Integer textViewId;
    Integer layoutViewId;

    int hashCode;
    boolean showAnimation;
    boolean isDismissible;

    List<HitogoButton> callToActionButtons;
    HitogoButton closeButton;

    Context context;
    View rootView;
    HitogoController controller;
    HitogoAnimation hitogoAnimation;

    HitogoViewBuilder(@NonNull Context context, @Nullable View rootView, @NonNull HitogoController controller) {
        this.context = context;
        this.rootView = rootView;
        this.controller = controller;
        this.callToActionButtons = new ArrayList<>();
    }

    @NonNull
    public HitogoViewBuilder setTitle(@XmlRes Integer viewId, @NonNull String title) {
        this.titleViewId = viewId;
        this.title = title;
        return this;
    }

    @NonNull
    public HitogoViewBuilder setTitle(@NonNull String title) {
        return setTitle(controller.getDefaultTitleViewId(), title);
    }

    @NonNull
    public HitogoViewBuilder setText(@XmlRes Integer viewId, @NonNull String text) {
        this.textViewId = viewId;
        this.text = text;
        return this;
    }

    @NonNull
    public HitogoViewBuilder setText(@NonNull String text) {
        return setText(controller.getDefaultTextViewId(), text);
    }

    @NonNull
    public HitogoViewBuilder withAnimations() {
        return withAnimations(controller.getDefaultAnimation(), controller.getDefaultLayoutViewId());
    }

    @NonNull
    public HitogoViewBuilder withAnimations(@Nullable HitogoAnimation animation) {
        return withAnimations(animation, controller.getDefaultLayoutViewId());
    }

    @NonNull
    public HitogoViewBuilder withAnimations(@Nullable HitogoAnimation animation,
                                            @Nullable Integer innerLayoutViewId) {
        this.showAnimation = true;
        this.hitogoAnimation = animation;
        this.layoutViewId = innerLayoutViewId;
        return this;
    }

    @NonNull
    public HitogoViewBuilder withoutAnimations() {
        this.showAnimation = false;
        this.hitogoAnimation = null;
        return this;
    }

    @NonNull
    public HitogoViewBuilder asDismissible(@NonNull HitogoButton closeButton) {
        if (closeButton.isCloseButton()) {
            this.isDismissible = true;
            this.closeButton = closeButton;
        } else {
            Log.e(HitogoViewBuilder.class.getName(), "Cannot add call to action button as close buttons.");
            Log.e(HitogoViewBuilder.class.getName(), "Trying to add default dismissible button.");
            asDismissible();
        }
        return this;
    }

    @NonNull
    public HitogoViewBuilder asDismissible() {
        this.isDismissible = true;

        try {
            closeButton = HitogoButton.with(controller)
                    .asCloseButton()
                    .build();
        } catch (InvalidParameterException ex) {
            this.isDismissible = false;
            Log.e(HitogoViewBuilder.class.getName(), "Cannot make this hitogo dismissible.");
            Log.e(HitogoViewBuilder.class.getName(), "Reason: " + ex.getMessage());
        }

        return this;
    }

    @NonNull
    public HitogoViewBuilder addActionButton(@NonNull HitogoButton...buttons) {
        for(HitogoButton button : buttons) {
            if (!button.isCloseButton()) {
                callToActionButtons.add(button);
            } else {
                Log.e(HitogoViewBuilder.class.getName(), "Cannot add close buttons as call to action buttons.");
            }
        }
        return this;
    }

    @NonNull
    public HitogoViewBuilder asIgnoreLayout() {
        this.containerId = null;
        return this;
    }

    @NonNull
    public HitogoViewBuilder asOverlay() {
        return asOverlay(controller.getDefaultOverlayContainerId());
    }

    @NonNull
    public HitogoViewBuilder asOverlay(@Nullable Integer overlayId) {
        this.containerId = overlayId;

        if (rootView != null && this.containerId != null) {
            View container = rootView.findViewById(this.containerId);
            if (container == null) {
                Log.e(HitogoViewBuilder.class.getName(), "Trying to use fallback to let the hitogo " +
                        "ignore the given layout.");
                return asIgnoreLayout();
            }
        } else {
            Log.e(HitogoViewBuilder.class.getName(), "Trying to use fallback to let the hitogo " +
                    "ignore the given layout.");
            return asIgnoreLayout();
        }

        return this;
    }

    @NonNull
    public HitogoViewBuilder asSimple(@NonNull String infoText) {
        this.text = infoText;

        HitogoViewBuilder customBuilder = controller.getSimpleView(this);
        if (customBuilder != null) {
            return customBuilder;
        } else {
            return asLayoutChild()
                    .asDismissible()
                    .withState(controller.getDefaultState());
        }
    }

    @NonNull
    public HitogoViewBuilder controlledBy(@NonNull HitogoController controller) {
        this.controller = controller;
        return this;
    }

    @NonNull
    public HitogoViewBuilder withState(@Nullable Integer state) {
        this.state = state;
        return this;
    }

    @NonNull
    public HitogoViewBuilder asLayoutChild() {
        this.containerId = controller.getDefaultLayoutContainerId();

        if (rootView != null && this.containerId != null) {
            View container = rootView.findViewById(this.containerId);
            if (container == null) {
                Log.e(HitogoViewBuilder.class.getName(), "Trying to use fallback to display hitogo " +
                        "as overlay.");
                return asOverlay();
            }
        } else {
            Log.e(HitogoViewBuilder.class.getName(), "Trying to use fallback to let the hitogo " +
                    "ignore the given layout.");
            return asIgnoreLayout();
        }

        return this;
    }

    @NonNull
    public HitogoViewBuilder asLayoutChild(@XmlRes int containerId) {
        this.containerId = containerId;

        if (rootView != null) {
            View container = rootView.findViewById(this.containerId);
            if (container == null) {
                Log.e(HitogoViewBuilder.class.getName(), "Trying to use fallback to display hitogo " +
                        "inside the default container layout.");
                return asLayoutChild();
            }
        } else {
            Log.e(HitogoViewBuilder.class.getName(), "Trying to use fallback to let the hitogo " +
                    "ignore the given layout.");
            return asIgnoreLayout();
        }

        return this;
    }

    @NonNull
    public HitogoObject build() {
        if (this.text == null) {
            throw new InvalidParameterException("Text parameter cannot be null.");
        }

        if (state == null) {
            throw new InvalidParameterException("To display non-dialog hitogos you need to define " +
                    "a state which will use a specific layout.");
        }

        if (!isDismissible && callToActionButtons.isEmpty()) {
            Log.e(HitogoViewBuilder.class.getName(), "Are you sure that this hitogo should have no " +
                    "interaction points? If yes, make sure to close this one if it's not " +
                    "needed anymore!");
        }

        hashCode = this.text.hashCode();
        return createLayout();
    }

    @NonNull
    private HitogoObject createLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(controller.getLayout(state), null);

        ViewGroup holder = null;
        if (this.containerId != null && rootView != null) {
            View containerView = rootView.findViewById(this.containerId);
            if (containerView instanceof ViewGroup) {
                holder = (ViewGroup) containerView;
            } else {
                holder = null;
            }
        }

        if (view != null) {
            view = buildLayoutContent(view);
            view = buildCallToActionButtons(view);
            view = buildCloseButtons(view);
        } else {
            throw new InvalidParameterException("Hitogo view is null. Is the layout existing for " +
                    "the state: '" + state + "'?");
        }

        return new HitogoView(this, view, holder);
    }


    @NonNull
    private View buildLayoutContent(@NonNull View containerView) {
        View view = setViewString(containerView, titleViewId, title);
        view = setViewString(view, textViewId, text);
        return view;
    }

    @NonNull
    private View setViewString(@NonNull View containerView, @Nullable Integer viewId,
                               @Nullable String chars) {
        if (viewId != null) {
            TextView textView = containerView.findViewById(viewId);
            if (textView != null) {
                if (chars != null && StringUtils.isNotEmpty(chars)) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(chars);
                } else {
                    textView.setVisibility(View.GONE);
                }
            } else {
                throw new InvalidParameterException("Did you forget to add the " +
                        "title/text view to your layout?");
            }
        } else {
            throw new InvalidParameterException("Title or text view id is null.");
        }
        return containerView;
    }

    @NonNull
    private View buildCallToActionButtons(@NonNull View containerView) {
        for (final HitogoButton callToActionButton : callToActionButtons) {
            View button = containerView.findViewById(callToActionButton.getViewIds()[0]);

            if (button != null) {
                if (button instanceof TextView) {
                    ((TextView) button).setText(callToActionButton.getText() != null ?
                            callToActionButton.getText() : "");
                }

                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callToActionButton.getListener().onClick();
                        controller.hideHitogo();
                    }
                });
            } else {
                throw new InvalidParameterException("Did you forget to add the " +
                        "call-to-action button to your layout?");
            }
        }

        return containerView;
    }

    @NonNull
    private View buildCloseButtons(@NonNull View containerView) {
        final View removeIcon = containerView.findViewById(closeButton.getViewIds()[0]);
        final View removeClick = containerView.findViewById(closeButton.getViewIds()[1]);

        if (removeIcon != null && removeClick != null) {
            removeIcon.setVisibility(this.isDismissible ? View.VISIBLE : View.GONE);
            removeClick.setVisibility(this.isDismissible ? View.VISIBLE : View.GONE);
            removeClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeButton.getListener().onClick();
                    controller.hideHitogo();
                }
            });
        } else {
            throw new InvalidParameterException("Did you forget to add the close button to " +
                    "your layout?");
        }

        return containerView;
    }

    public void show(@NonNull Activity activity) {
        showNow(activity);
    }

    public void showNow(@NonNull Activity activity) {
        build().show(activity);
    }

    public void showDelayed(@NonNull final Activity activity, long millis) {
        if (millis == 0) {
            build().show(activity);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!activity.isFinishing()) {
                        build().show(activity);
                    }
                }
            }, millis);
        }
    }

    public void show(@NonNull Fragment fragment) {
        showNow(fragment);
    }

    public void showNow(@NonNull Fragment fragment) {
        build().show(fragment);
    }

    public void showDelayed(@NonNull final Fragment fragment, long millis) {
        if (millis == 0) {
            build().show(fragment);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (fragment.isAdded()) {
                        build().show(fragment);
                    }
                }
            }, millis);
        }
    }
}