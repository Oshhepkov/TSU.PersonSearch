package com.github.oshhepkov.app;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private ContentAdapter contentAdapter;
    private MainViewModel mainViewModel;
    private final ContentAdapter.ItemClick itemClick = new ContentAdapter.ItemClick() {

        @Override
        public void onUserClick(UserModel user) {
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            intent.putExtra("data", user);
            startActivity(intent);
        }

        @Override
        public void onTryAgain() {
            mainViewModel.makeQuery(searchView.getQuery().toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        contentAdapter = new ContentAdapter(itemClick);
        RecyclerView content = findViewById(R.id.search_user_content_list);
        content.setItemAnimator(new SlideInLeftAnimator());
        content.setLayoutManager(new LinearLayoutManager(this));
        content.setAdapter(contentAdapter);
        observeViewModel();
        if (savedInstanceState == null) {
            contentAdapter.show(CommonItem.FirstTimeItem.create());
        }
    }

    private void observeViewModel() {
        mainViewModel.getContent().observe(this, new Observer<List<CommonItem>>() {
            @Override
            public void onChanged(@Nullable List<CommonItem> commonItems) {
                if (commonItems != null) {
                    contentAdapter.show(commonItems);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainViewModel.makeQuery(query);
                hideKeyboard();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    mainViewModel.makeQuery("");
                }
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_apply) {
            hideKeyboard();
            mainViewModel.makeQuery(searchView.getQuery().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null)
            return;

        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
