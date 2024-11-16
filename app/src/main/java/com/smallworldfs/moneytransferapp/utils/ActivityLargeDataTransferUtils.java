package com.smallworldfs.moneytransferapp.utils;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import rx.Completable;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class ActivityLargeDataTransferUtils {

    public static void saveFile(@NonNull final Context context,
                                @NonNull final String fileName,
                                @NonNull final String jsonData,
                                @NonNull final Completable.CompletableSubscriber subscriber) {
        Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(Completable.CompletableSubscriber emitter) {
                try {
                    final ObjectOutputStream objectOutStream = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
                    objectOutStream.writeObject(jsonData);
                    objectOutStream.close();
                    emitter.onCompleted();
                } catch (IOException e) {
                    Log.e("STACK", "----------------------",e);
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Nullable
    public static void readAndDeleteFile(@NonNull final Context context, @NonNull final String fileName,
                                         @NonNull final Subscriber<String> subscriber) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> emitter) {
                try {
                    final ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(fileName));
                    final String jsonData = (String) objectInputStream.readObject();
                    objectInputStream.close();
                    context.deleteFile(fileName);
                    emitter.onNext(jsonData);
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
