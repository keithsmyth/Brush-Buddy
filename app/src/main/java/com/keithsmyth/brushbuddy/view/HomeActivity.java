package com.keithsmyth.brushbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.keithsmyth.brushbuddy.App;
import com.keithsmyth.brushbuddy.R;
import com.keithsmyth.brushbuddy.model.User;
import com.keithsmyth.brushbuddy.model.UserProvider;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public class HomeActivity extends AppCompatActivity {

    private View root;

    private UserProvider userProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        root = findViewById(R.id.activity_home);

        // inject
        userProvider = App.inject(this).userProvider();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.add_buddy_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DrawingActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userProvider.current()
                .observeOn(mainThread())
                .subscribe(new CurrentUserObserver());
    }

    private class CurrentUserObserver implements SingleObserver<User> {
        @Override
        public void onSubscribe(Disposable d) {
            // no op
        }

        @Override
        public void onSuccess(User user) {
            setTitle(user.name);
        }

        @Override
        public void onError(Throwable e) {
            userProvider.logout();
            Snackbar.make(root, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
}
