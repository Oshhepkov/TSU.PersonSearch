package com.github.oshhepkov.app;

public class ConcreteUserStatus {
    private final Status status;
    private String errorMessage;
    private ConcreteUserModel user;

    public ConcreteUserStatus(Status status, String errorMessage, ConcreteUserModel user) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.user = user;
    }

    public static ConcreteUserStatus loading() {
        return new ConcreteUserStatus(Status.LOADING, null, null);
    }

    public static ConcreteUserStatus success(ConcreteUserModel model) {
        return new ConcreteUserStatus(Status.SUCCESS, null, model);
    }

    public static ConcreteUserStatus error(String message) {
        return new ConcreteUserStatus(Status.ERROR, message, null);
    }

    public Status getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ConcreteUserModel getUser() {
        return user;
    }

    public enum Status {
        LOADING,
        ERROR,
        SUCCESS
    }
}
