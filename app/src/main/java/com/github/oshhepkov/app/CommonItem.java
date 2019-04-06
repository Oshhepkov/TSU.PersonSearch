package com.github.oshhepkov.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class CommonItem {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat needFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    final int type;

    CommonItem(int type) {
        this.type = type;
    }

    private static String toDate(String date) {
        if (date == null || date.isEmpty()) {
            return "Неизвестно";
        }

        try {
            Date d = format.parse(date);
            return needFormat.format(d);
        } catch (ParseException e) {
            return "Неизвестно";
        }
    }

    public static List<CommonItem> createFromUserInfo(ConcreteUserModel user, String name) {
        List<CommonItem> result = new ArrayList<>();
        result.add(new InfoNameItem(name));
        result.add(new DividerItem());
        result.add(new InfoCommonItem("Дата рождения:", toDate(user.getBirthDate())));
        if (user.getDegree() != null) {
            result.add(new InfoCommonItem("Учёная степень:", user.getDegree()));
        }
        if (user.getRank() != null) {
            result.add(new InfoCommonItem("Учёное звание:", user.getRank()));
        }
        result.add(new DividerItem());
        Collections.sort(user.getPosts(), new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return t1.getIsMain().compareTo(post.getIsMain());
            }
        });
        for (Post post : user.getPosts()) {
            result.add(new PostItem(post.getName(), post.getPostDepartmentName(), post.getIsMain()));
        }
        return result;
    }

    public boolean isTheSame(CommonItem otherItem) {
        return type == otherItem.type;
    }

    public boolean isContentTheSame(CommonItem otherItem) {
        return type == otherItem.type;
    }

    public static class LoadItem extends CommonItem {
        LoadItem() {
            super(0);
        }

        public static List<CommonItem> create() {
            List<CommonItem> result = new ArrayList<>(1);
            result.add(new LoadItem());
            return result;
        }
    }

    public static class LoadWrapItem extends CommonItem {
        LoadWrapItem() {
            super(5);
        }

        public static List<CommonItem> create() {
            List<CommonItem> result = new ArrayList<>(1);
            result.add(new LoadWrapItem());
            return result;
        }
    }

    public static class ErrorItem extends CommonItem {
        public final String errorMessage;

        ErrorItem(String errorMessage) {
            super(1);
            this.errorMessage = errorMessage;
        }

        public static List<CommonItem> create(Throwable throwable) {
            List<CommonItem> result = new ArrayList<>(1);
            result.add(new ErrorItem(throwable.getMessage()));
            return result;
        }

        @Override
        public boolean isContentTheSame(CommonItem otherItem) {
            return otherItem instanceof ErrorItem && ((ErrorItem) otherItem).errorMessage.equals(errorMessage);
        }
    }

    public static class ErrorWrapItem extends CommonItem {
        public final String errorMessage;

        ErrorWrapItem(String errorMessage) {
            super(6);
            this.errorMessage = errorMessage;
        }

        public static List<CommonItem> create(String message) {
            List<CommonItem> result = new ArrayList<>(1);
            result.add(new ErrorWrapItem(message));
            return result;
        }

        @Override
        public boolean isContentTheSame(CommonItem otherItem) {
            return otherItem instanceof ErrorWrapItem && ((ErrorWrapItem) otherItem).errorMessage.equals(errorMessage);
        }
    }

    static class EmptyItem extends CommonItem {
        EmptyItem() {
            super(2);
        }
    }

    public static class FirstTimeItem extends CommonItem {
        FirstTimeItem() {
            super(4);
        }

        public static List<CommonItem> create() {
            List<CommonItem> result = new ArrayList<>(1);
            result.add(new FirstTimeItem());
            return result;
        }
    }

    public static class UserItem extends CommonItem {
        public final UserModel userModel;

        UserItem(UserModel userModel) {
            super(3);
            this.userModel = userModel;
        }

        public static List<CommonItem> create(List<UserModel> list) {
            List<CommonItem> result = new ArrayList<>(1);
            if (list == null || list.isEmpty()) {
                result.add(new EmptyItem());
            } else {
                for (UserModel model : list) {
                    result.add(new UserItem(model));
                }
            }
            return result;
        }

        @Override
        public boolean isContentTheSame(CommonItem otherItem) {
            return otherItem instanceof UserItem && ((UserItem) otherItem).userModel.getId().equals(userModel.getId());
        }

        public String getUrl() {
            return userModel.getUrl();
        }
    }

    public static class DividerItem extends CommonItem {
        DividerItem() {
            super(7);
        }
    }

    public static class InfoCommonItem extends CommonItem {
        public final String label;
        public final String value;

        InfoCommonItem(String label, String value) {
            super(8);
            this.label = label;
            this.value = value;
        }
    }

    public static class InfoNameItem extends CommonItem {
        public final String name;

        InfoNameItem(String name) {
            super(9);
            this.name = name;
        }
    }

    public static class PostItem extends CommonItem {
        public final String name;
        public final String postDepartment;
        public final Boolean isMain;

        PostItem(String name, String postDepartment, Boolean isMain) {
            super(10);
            this.name = name;
            this.postDepartment = postDepartment;
            this.isMain = isMain;
        }
    }
}
