/*
 * MIT License
 *
 * Copyright (c) 2017 MIchael Obi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.michaelobi.paperplayer.mvp;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T extends Mvp.View> implements Mvp.Presenter<T> {

    private T view;

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

    @NonNull
    public final T getView() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
        return view;
    }

    private boolean isViewAttached() {
        return view != null;
    }

    protected void addSubscription(Subscription subscription) {
        this.compositeSubscription.add(subscription);
    }

    public static class MvpViewNotAttachedException extends IllegalStateException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before requesting data to the Presenter");
        }
    }
}
