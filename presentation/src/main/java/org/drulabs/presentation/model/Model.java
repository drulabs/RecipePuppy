package org.drulabs.presentation.model;

public class Model<T> {

    private boolean loading;
    private Throwable error;
    private T data;

    Model(boolean loading, Throwable error, T data) {
        this.loading = loading;
        this.error = error;
        this.data = data;
    }

    public boolean isLoading() {
        return loading;
    }

    public Throwable getError() {
        return error;
    }

    public T getData() {
        return data;
    }

    // Generators
    static <E> Model<E> success(E e) {
        return new Model<>(false, null, e);
    }

    static <E> Model<E> error(Throwable throwable) {
        return new Model<>(false, throwable, null);
    }

    static <E> Model<E> loading(boolean loadingStatus) {
        return new Model<>(loadingStatus, null, null);
    }
}
