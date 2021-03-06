package org.hitogo.alert.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.hitogo.alert.core.AlertImpl;
import org.hitogo.button.core.Button;
import org.hitogo.core.HitogoAnimation;
import org.hitogo.core.HitogoController;

import java.security.InvalidParameterException;

/**
 * Implementation for the view alert. This alert requires to have at least one text element, a
 * state or layout resource and an anchor view-id/-tag.
 *
 * @since 1.0.0
 */
public class ViewAlertImpl extends AlertImpl<ViewAlertParams> implements ViewAlert {

    private ViewGroup viewGroup;
    private HitogoAnimation animation;

    @Override
    protected void onCheck(@NonNull ViewAlertParams params) {
        super.onCheck(params);

        if (params.getTextMap().size() == 0) {
            throw new InvalidParameterException("You need to add a text to this alert.");
        }

        if (params.getState() == null && params.getLayoutRes() == null) {
            throw new InvalidParameterException("To display non-dialog alerts you need to define " +
                    "a state which will use a specific layout. This layout needs to be defined " +
                    "inside your HitogoController. Optionally you can define a custom layout via " +
                    "setLayout.");
        }
    }

    @Override
    protected void onCreate(@NonNull HitogoController controller, @NonNull ViewAlertParams params) {
        super.onCreate(controller, params);

        this.animation = params.getAnimation() != null ?
                params.getAnimation() : controller.provideDefaultAlertAnimation(getAlertType());

        if (params.getContainerId() != null) {
            View containerView = determineViewGroup();

            if (containerView != null && !(containerView instanceof ViewGroup) &&
                    (getController().provideIsDebugState())) {
                throw new InvalidParameterException("Your container view needs to be a ViewGroup. " +
                        "Use for example the LinearLayout to solve this issue.");
            }

            if (containerView != null && containerView instanceof ViewGroup) {
                viewGroup = (ViewGroup) containerView;
            } else {
                Log.e(ViewAlertBuilderImpl.class.getName(), "Cannot find any container view. " +
                        "Using activity content view (layer) fallback.");
                viewGroup = null;
            }
        }
    }

    protected View determineViewGroup() {
        int layoutContainerId = getParams().getContainerId();
        View containerView = getRootView().findViewById(layoutContainerId);

        Integer overlayContainerId = getController().provideDefaultViewAlertOverlayContainerId();
        if (containerView == null && overlayContainerId != null) {
            Log.e(ViewAlertBuilderImpl.class.getName(), "Cannot find container view. " +
                    "Using default overlay container view as fallback.");
            containerView = getRootView().findViewById(overlayContainerId);
        }

        if (!isExecutedByActivity() && containerView == null) {
            Log.e(ViewAlertBuilderImpl.class.getName(), "Cannot find fragment " +
                    "container/overlay view. Using default activity container view as fallback.");
            containerView = getActivityRootView().findViewById(layoutContainerId);
        }

        if (!isExecutedByActivity() && containerView == null && overlayContainerId != null) {
            Log.e(ViewAlertBuilderImpl.class.getName(), "Cannot find activity " +
                    "container view. Using default activity overlay view as fallback.");
            containerView = getActivityRootView().findViewById(overlayContainerId);
        }

        return containerView;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, @NonNull Context context,
                                @NonNull ViewAlertParams params) {

        View view = null;
        if (params.getLayoutRes() != null && params.getLayoutRes() != 0) {
            view = inflater.inflate(params.getLayoutRes(), null);
        } else if (params.getState() != null && getController().provideViewLayout(params.getState()) != null) {
            view = inflater.inflate(getController().provideViewLayout(params.getState()), null);
        }

        if (view != null) {
            buildLayoutInteractions(view);
            buildLayoutContent(view);

            for (Button button : params.getButtons()) {
                determineButtonCreation(button, view, false);
            }

            if (params.getCloseButton() != null) {
                determineButtonCreation(params.getCloseButton(), view, true);
            }

            return view;
        } else if (getController().provideIsDebugState()) {
            throw new InvalidParameterException("Alert view is null. Is the layout existing for " +
                    "the state: '" + params.getState() + "'?");
        }
        return null;
    }

    protected void determineButtonCreation(Button button, View dialogView, boolean forceClose) {
        if (button.getParams().hasButtonView()) {
            buildButton(button, dialogView, forceClose);
        } else if (getController().provideIsDebugState()) {
            throw new IllegalStateException("ViewAlert can only process buttons that have a view " +
                    "use asViewButton or asCloseButton.");
        }
    }

    protected void buildLayoutInteractions(@NonNull View containerView) {
        if (getParams().dismissByLayoutClick()) {
            containerView.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View view) {
                    close();
                }
            });
            containerView.setClickable(true);
        }
    }

    protected void buildLayoutContent(@NonNull View containerView) {
        if (getParams().getTitleViewId() != null) {
            setViewString(containerView, getParams().getTitleViewId(), getParams().getTitle());
        }

        SparseArray<String> textMap = getParams().getTextMap();
        for (int i = 0; i < textMap.size(); i++) {
            Integer viewId = textMap.keyAt(i);
            String text = textMap.valueAt(i);
            setViewString(containerView, viewId, text);
        }

        SparseArray<Drawable> drawableMap = getParams().getDrawableMap();
        for (int i = 0; i < drawableMap.size(); i++) {
            Integer viewId = drawableMap.keyAt(i);
            Drawable drawable = drawableMap.valueAt(i);
            setDrawable(containerView, viewId, drawable);
        }
    }

    protected void setViewString(@NonNull View containerView, @Nullable Integer viewId,
                                 @Nullable String chars) {
        if (viewId != null) {
            TextView textView = containerView.findViewById(viewId);
            if (textView != null) {
                if (getHelper().isNotEmpty(chars)) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(getAccessor().getHtmlText(chars));
                } else {
                    textView.setVisibility(View.GONE);
                }
            } else if (getController().provideIsDebugState()) {
                throw new InvalidParameterException("Did you forget to add the view to your layout?");
            }
        } else if (getController().provideIsDebugState()) {
            throw new InvalidParameterException("View id is null.");
        }
    }

    protected void setDrawable(@NonNull View containerView, @Nullable Integer viewId,
                               @Nullable Drawable drawable) {
        if (viewId != null) {
            View view = containerView.findViewById(viewId);
            if (view != null) {
                if (drawable != null) {
                    view.setVisibility(View.VISIBLE);

                    if (view instanceof ImageView) {
                        ((ImageView) view).setImageDrawable(drawable);
                    } else {
                        view.setBackground(drawable);
                    }
                } else {
                    view.setVisibility(View.GONE);
                }
            } else if (getController().provideIsDebugState()) {
                throw new InvalidParameterException("Did you forget to add the view to your layout?");
            }
        } else if (getController().provideIsDebugState()) {
            throw new InvalidParameterException("View id is null.");
        }
    }

    @SuppressWarnings("unchecked")
    protected void buildButton(final Button button, View view, final boolean forceClose) {
        final View buttonLayout = view.findViewById(button.getParams().getIconId());
        View clickLayout = view.findViewById(button.getParams().getClickId());

        if (clickLayout == null) {
            clickLayout = buttonLayout;
        }

        if (buttonLayout != null) {
            SparseArray<String> textMap = button.getParams().getTextMap();
            for (int i = 0; i < textMap.size(); i++) {
                Integer viewId = textMap.keyAt(i);
                String text = textMap.valueAt(i);
                setButtonString(buttonLayout, viewId, text);
            }

            SparseArray<Drawable> drawableMap = button.getParams().getDrawableMap();
            for (int i = 0; i < drawableMap.size(); i++) {
                Integer viewId = drawableMap.keyAt(i);
                Drawable drawable = drawableMap.valueAt(i);
                setDrawable(buttonLayout, viewId, drawable);
            }

            buttonLayout.setVisibility(View.VISIBLE);
            clickLayout.setVisibility(View.VISIBLE);
            clickLayout.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    button.getParams().getListener().onClick(ViewAlertImpl.this, button.getParams().getButtonParameter());

                    if ((button.getParams().isClosingAfterClick() || forceClose) && isAttached()) {
                        close();
                    }
                }
            });
        } else if (getController().provideIsDebugState()) {
            throw new InvalidParameterException("Did you forget to add the button to your layout?");
        }
    }

    protected void setButtonString(@NonNull View buttonLayout, @Nullable Integer viewId,
                                   @Nullable String chars) {
        TextView textView = null;
        if (viewId != null && viewId != -1) {
            textView = buttonLayout.findViewById(viewId);
        } else if (buttonLayout instanceof TextView) {
            textView = (TextView) buttonLayout;
        }

        if (textView != null) {
            if (getHelper().isNotEmpty(chars)) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(getAccessor().getHtmlText(chars));
            } else {
                textView.setVisibility(View.GONE);
            }
        } else if (getController().provideIsDebugState()) {
            throw new InvalidParameterException("Either your button layout is not a text view or " +
                    "the subview for the text element could not be found. Make sure your button" +
                    "layout is including the TextView.");
        }
    }

    @Override
    protected void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (viewGroup != null) {
            viewGroup.addView(getView());
        } else {
            ViewGroup.LayoutParams layoutParams = getView().getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            if (!((Activity) context).isFinishing()) {
                ((Activity) context).addContentView(getView(), layoutParams);
            }
        }
    }

    @Override
    protected void onShowAnimation(@NonNull Context context) {
        super.onShowAnimation(context);

        if (animation != null) {
            getHelper().measureView((Activity) context, getView(), viewGroup);
            animation.showAnimation(getParams(), getView(), this);
        }
    }

    @Override
    protected void onCloseAnimation(@NonNull Context context) {
        super.onCloseAnimation(context);

        if (animation != null) {
            animation.hideAnimation(getParams(), getView(), this);
        }
    }

    @Override
    protected void onCloseDefault(@NonNull Context context) {
        super.onCloseDefault(context);
        ViewManager manager = (ViewManager) getView().getParent();
        manager.removeView(getView());
    }

    @Override
    public long getAnimationDuration() {
        return animation.getAnimationDuration();
    }
}
