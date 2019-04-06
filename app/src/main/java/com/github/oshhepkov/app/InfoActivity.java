package com.github.oshhepkov.app;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class InfoActivity extends AppCompatActivity {

    private UserModel user;
    private InfoViewModel infoViewModel;
    private final ContentAdapter.ItemClick itemClick = new ContentAdapter.ItemClick() {
        @Override
        public void onTryAgain() {
            infoViewModel.setAccountId(user.getId());
        }
    };
    private ContentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        infoViewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
        setSupportActionBar(toolbar);
        user = (UserModel) getIntent().getSerializableExtra("data");
        adapter = new ContentAdapter(itemClick);
        RecyclerView content = findViewById(R.id.content);
        content.setItemAnimator(new SlideInLeftAnimator());
        content.setLayoutManager(new LinearLayoutManager(this));
        content.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(user.getName());
        ImageView avatar = findViewById(R.id.avatar);
        Picasso.with(this)
                .load(user.getUrl())
                .into(avatar);
        infoViewModel.setAccountId(user.getId());

        infoViewModel.getContent().observe(this, new Observer<ConcreteUserStatus>() {
            @Override
            public void onChanged(@Nullable ConcreteUserStatus concreteUserStatus) {
                if (concreteUserStatus != null) {
                    switch (concreteUserStatus.getStatus()) {
                        case LOADING:
                            adapter.show(CommonItem.LoadWrapItem.create());
                            break;
                        case ERROR:
                            adapter.show(CommonItem.ErrorWrapItem.create(concreteUserStatus.getErrorMessage()));
                            break;
                        case SUCCESS:
                            adapter.show(CommonItem.createFromUserInfo(concreteUserStatus.getUser(), user.getName()));
                            break;
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
