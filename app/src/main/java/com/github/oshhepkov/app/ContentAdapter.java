package com.github.oshhepkov.app;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.CommonViewHolder> {

    @NonNull
    private final ItemClick itemClick;
    @NonNull
    private List<CommonItem> mItems = new ArrayList<>();
    ContentAdapter(@NonNull ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ContentAdapter.CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.item_loading, parent, false);
                return new LoadViewHolder(view);
            case 1:
                view = inflater.inflate(R.layout.item_error, parent, false);
                return new ErrorViewHolder(view);
            case 2:
                view = inflater.inflate(R.layout.item_empty, parent, false);
                return new EmptyViewHolder(view);
            case 3:
                view = inflater.inflate(R.layout.item_user, parent, false);
                return new UserViewHolder(view);
            case 4:
                view = inflater.inflate(R.layout.item_first_time, parent, false);
                return new FirstTimeViewHolder(view);
            case 5:
                view = inflater.inflate(R.layout.item_loading_wrap, parent, false);
                return new LoadViewHolder(view);
            case 6:
                view = inflater.inflate(R.layout.item_error_wrap, parent, false);
                return new ErrorWrapViewHolder(view);
            case 7:
                view = inflater.inflate(R.layout.item_info_divider, parent, false);
                return new DividerViewHolder(view);
            case 8:
                view = inflater.inflate(R.layout.item_info_common, parent, false);
                return new InfoCommonViewHolder(view);
            case 9:
                view = inflater.inflate(R.layout.item_info_name, parent, false);
                return new InfoNameViewHolder(view);
            case 10:
                view = inflater.inflate(R.layout.item_info_post, parent, false);
                return new PostViewHolder(view);
            default:
                throw new IllegalArgumentException("Can't get type for item");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        holder.bind(mItems.get(position), itemClick);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).type;
    }

    public void show(List<CommonItem> data) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CommonDiffUtil(mItems, data));
        diffResult.dispatchUpdatesTo(this);
        mItems = data;
    }

    public abstract static class ItemClick {
        public void onUserClick(UserModel user) {
        }

        public abstract void onTryAgain();
    }

    abstract static class CommonViewHolder extends RecyclerView.ViewHolder {

        CommonViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bind(CommonItem item, ItemClick click);

    }

    public static class LoadViewHolder extends CommonViewHolder {

        LoadViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void bind(CommonItem item, ItemClick click) {
        }

    }

    public static class EmptyViewHolder extends CommonViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void bind(CommonItem item, ItemClick click) {
        }
    }

    public static class FirstTimeViewHolder extends CommonViewHolder {
        FirstTimeViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void bind(CommonItem item, ItemClick click) {
        }
    }

    public static class ErrorViewHolder extends CommonViewHolder {

        private final Button tryAgain;
        private final TextView message;

        ErrorViewHolder(View itemView) {
            super(itemView);
            tryAgain = itemView.findViewById(R.id.try_again);
            message = itemView.findViewById(R.id.message);
        }

        @Override
        void bind(CommonItem item, final ItemClick click) {
            CommonItem.ErrorItem errorItem = (CommonItem.ErrorItem) item;
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.onTryAgain();
                }
            });
            message.setText(errorItem.errorMessage);
        }

    }

    public static class ErrorWrapViewHolder extends CommonViewHolder {

        private final Button tryAgain;
        private final TextView message;

        ErrorWrapViewHolder(View itemView) {
            super(itemView);
            tryAgain = itemView.findViewById(R.id.try_again);
            message = itemView.findViewById(R.id.message);
        }

        @Override
        void bind(CommonItem item, final ItemClick click) {
            CommonItem.ErrorWrapItem errorItem = (CommonItem.ErrorWrapItem) item;
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.onTryAgain();
                }
            });
            message.setText(errorItem.errorMessage);
        }

    }



    public static class UserViewHolder extends CommonViewHolder {

        private final ImageView userImage;
        private final TextView userName;

        UserViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.avatar);
            userName = itemView.findViewById(R.id.fio);
        }

        @Override
        void bind(CommonItem item, final ItemClick click) {
            final CommonItem.UserItem userItem = (CommonItem.UserItem) item;
            userName.setText(userItem.userModel.getName());
            Picasso.with(itemView.getContext())
                    .load(userItem.getUrl())
                    .into(userImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.onUserClick(userItem.userModel);
                }
            });
        }

    }

    public static class DividerViewHolder extends CommonViewHolder {

        DividerViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void bind(CommonItem item, ItemClick click) {
        }

    }

    public static class PostViewHolder extends CommonViewHolder {

        private final TextView name;
        private final TextView postDep;
        private final ImageView isMain;

        PostViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            isMain = itemView.findViewById(R.id.is_main);
            postDep = itemView.findViewById(R.id.post_department_value);
        }

        @Override
        void bind(CommonItem item, ItemClick click) {
            CommonItem.PostItem postItem = (CommonItem.PostItem) item;
            name.setText(postItem.name);
            postDep.setText(postItem.postDepartment);
            if (postItem.isMain) {
                isMain.setVisibility(View.VISIBLE);
            } else {
                isMain.setVisibility(View.GONE);
            }
        }

    }

    public static class InfoNameViewHolder extends CommonViewHolder {

        private final TextView name;

        InfoNameViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.fio);
        }

        @Override
        void bind(CommonItem item, ItemClick click) {
            name.setText(((CommonItem.InfoNameItem) item).name);
        }

    }


    public static class InfoCommonViewHolder extends CommonViewHolder {

        private final TextView label;
        private final TextView value;

        InfoCommonViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            value = itemView.findViewById(R.id.value);
        }

        @Override
        void bind(CommonItem item, ItemClick click) {
            CommonItem.InfoCommonItem commonItem = (CommonItem.InfoCommonItem) item;
            label.setText(commonItem.label);
            value.setText(commonItem.value);
        }

    }


}



