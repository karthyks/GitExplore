package com.github.karthyks.gitexplore.login;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.karthyks.gitexplore.R;
import com.github.karthyks.gitexplore.frameworks.CustomFragment;

public class LoginFragment extends CustomFragment<LoginActivity> implements View.OnClickListener {

    public static LoginFragment getFragment() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_login_gh).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_gh:
                getHostingActivity().renderGithubWeb();
                break;
            default:
        }
    }
}
