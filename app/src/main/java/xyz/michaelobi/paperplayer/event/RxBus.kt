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

package xyz.michaelobi.paperplayer.event

import rx.Observable
import rx.functions.Action1
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import rx.subscriptions.CompositeSubscription


/**
 * PaperPlayer Michael Obi 12 01 2017 12:06 AM
 */

class RxBus {

    private val bus = SerializedSubject(PublishSubject.create<Any>())

    private val subscriptionMap = HashMap<Any, CompositeSubscription>()

    fun post(event: Any) {
        if (this.bus.hasObservers()) {
            this.bus.onNext(event)
        }
    }

    private fun <T> observe(eventClass: Class<T>): Observable<T> {
        return this.bus
                .filter { o -> o != null }
                .filter({ eventClass.isInstance(it) })
                .cast(eventClass)
    }

    fun <T> subscribe(eventClass: Class<T>, subscriptionAnchor: Any, action: Action1<T>) {
        val subscription = observe(eventClass).subscribe(action)
        getCompositeSubscription(subscriptionAnchor).add(subscription)
    }

    private fun getCompositeSubscription(subscriptionAnchor: Any): CompositeSubscription {
        var compositeSubscription = subscriptionMap[subscriptionAnchor]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeSubscription()
            subscriptionMap[subscriptionAnchor] = compositeSubscription
        }
        return compositeSubscription
    }

    fun cleanup(subscriptionAnchor: Any) {
        val compositeSubscription = subscriptionMap.remove(subscriptionAnchor)
        compositeSubscription?.clear()
    }
}
