package net.devdome.paperplayer.mvp;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T extends Mvp.View> implements Mvp.Presenter<T> {

    protected T view;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        compositeSubscription.clear();
        this.view = null;
    }

    public T getView() {
        return this.view;
    }

    protected void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    private boolean isViewAttached() {
        return view != null;
    }

    protected void addSubscription(Subscription subscription) {
        this.compositeSubscription.add(subscription);
    }
    
    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before requesting data to the Presenter");
        }
    }
}
