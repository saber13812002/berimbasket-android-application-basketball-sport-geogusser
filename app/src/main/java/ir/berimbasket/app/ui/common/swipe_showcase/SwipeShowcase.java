package ir.berimbasket.app.ui.common.swipe_showcase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Locale;

import ir.berimbasket.app.R;

public class SwipeShowcase {

    public interface Listener {
        void onDismissed();
    }

    private FrameLayout container;
    private TutView tutView;
    private Listener listener;

    private SwipeShowcase(@NonNull Activity activity) {
        this.container = new FrameLayout(activity);
        this.tutView = new TutView(activity);
        Window window = activity.getWindow();
        if (window != null) {
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            if (decorView != null) {
                ViewGroup content = decorView.findViewById(android.R.id.content);
                if (content != null) {
                    content.addView(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    this.container.addView(tutView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }
        }
        this.container.setVisibility(View.GONE);
        this.container.setAlpha(0f);
    }

    @NonNull
    public static SwipeShowcase from(@NonNull Activity activity) {
        return new SwipeShowcase(activity);
    }

    public SwipeShowcase setListener(SwipeShowcase.Listener listener) {
        this.listener = listener;
        return this;
    }

    public SwipeShowcase setContentView(@LayoutRes int content) {
        View child = LayoutInflater.from(tutView.getContext()).inflate(content, container, false);
        container.addView(child, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    private void dismiss() {
        ViewCompat.animate(container)
                .alpha(0f)
                .setDuration(container.getResources().getInteger(android.R.integer.config_mediumAnimTime))
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        super.onAnimationEnd(view);
                        ViewParent parent = view.getParent();
                        if (parent instanceof ViewGroup) {
                            ((ViewGroup) parent).removeView(view);
                        }
                        if (listener != null) {
                            listener.onDismissed();
                        }
                    }
                }).start();

    }

    public SwipeShowcase show() {
        container.setVisibility(View.VISIBLE);
        ViewCompat.animate(container)
                .alpha(1f)
                .setDuration(container.getResources().getInteger(android.R.integer.config_longAnimTime))
                .start();
        container.setOnClickListener(v -> dismiss());
        return this;
    }

    @Nullable
    private View findViewById(@IdRes int viewId) {
        Context context = tutView.getContext();
        View view = null;
        if (context instanceof Activity) {
            view = ((Activity) context).findViewById(viewId);
        }
        return view;

    }

    public SwipeShowcase.ViewActions on(@IdRes int viewId) {
        return new SwipeShowcase.ViewActions(this, findViewById(viewId));
    }

    public SwipeShowcase.ViewActions on(View view) {
        return new SwipeShowcase.ViewActions(this, view);
    }

    private static class ViewActionsSettings {
        private boolean animated = true;
        private Integer delay = 0;
        private Integer duration = 300;
    }

    public static class ViewActions {
        private final SwipeShowcase tutoShowcase;
        private final View view;
        private final SwipeShowcase.ViewActionsSettings settings;

        ViewActions(final SwipeShowcase tutoShowcase, View view) {
            this.tutoShowcase = tutoShowcase;
            this.view = view;
            this.settings = new SwipeShowcase.ViewActionsSettings();
        }

        public SwipeShowcase.ViewActions on(@IdRes int viewId) {
            return tutoShowcase.on(viewId);
        }

        public SwipeShowcase.ViewActions on(View view) {
            return tutoShowcase.on(view);
        }

        public SwipeShowcase show() {
            return tutoShowcase.show();
        }

        private void displaySwipeable(final boolean left) {
            final Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);

            final ImageView hand = new ImageView(view.getContext());
            if (left) {
                hand.setImageResource(R.drawable.swipe_left);
            } else {
                hand.setImageResource(R.drawable.swipe_right);
            }
            hand.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            hand.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    boolean isLayoutRTL = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL;

                    int x = (int) (rect.centerX() - hand.getWidth() / 2f);
                    int y = (int) (rect.centerY() - hand.getHeight() / 2f);

                    hand.setTranslationY(y);
                    if (!left && isLayoutRTL) {
                        hand.setTranslationX(-x);
                    } else {
                        hand.setTranslationX(x);
                    }

                    if (settings.animated) {
                        float tX;
                        if (left) {
                            tX = rect.left;
                        } else {
                            if (isLayoutRTL) {
                                tX = rect.left;
                            } else {
                                tX = rect.left + rect.width() * 0.7f;
                            }
                        }
                        ViewCompat.animate(hand).translationX(tX)
                                .setStartDelay(settings.delay != null ? settings.delay : 500)
                                .setDuration(settings.duration != null ? settings.duration : 600)
                                .setInterpolator(new DecelerateInterpolator());
                    }

                    hand.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });

            tutoShowcase.container.addView(hand);
            tutoShowcase.container.invalidate();
        }

        public SwipeShowcase.ActionViewActionsEditor displaySwipableLeft() {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    displaySwipeable(true);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new SwipeShowcase.ActionViewActionsEditor(this);
        }

        public SwipeShowcase.ActionViewActionsEditor displaySwipableRight() {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    displaySwipeable(false);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new SwipeShowcase.ActionViewActionsEditor(this);
        }
    }

    public static class ViewActionsEditor {
        final SwipeShowcase.ViewActions viewActions;

        ViewActionsEditor(SwipeShowcase.ViewActions viewActions) {
            this.viewActions = viewActions;
        }

        public SwipeShowcase.ViewActions on(@IdRes int viewId) {
            return viewActions.on(viewId);
        }

        public SwipeShowcase.ViewActions on(View view) {
            return viewActions.on(view);
        }

        public SwipeShowcase show() {
            return viewActions.show();
        }

    }

    public static class ActionViewActionsEditor extends SwipeShowcase.ViewActionsEditor {
        ActionViewActionsEditor(SwipeShowcase.ViewActions viewActions) {
            super(viewActions);
        }
    }

}
