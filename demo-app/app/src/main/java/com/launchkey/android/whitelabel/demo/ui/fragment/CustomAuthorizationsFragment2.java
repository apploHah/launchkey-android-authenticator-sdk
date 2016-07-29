package com.launchkey.android.whitelabel.demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.ui.adapter.DemoAuthorizationsAdapter;
import com.launchkey.android.whitelabel.demo.util.Utils;
import com.launchkey.android.whitelabel.sdk.auth.Application;
import com.launchkey.android.whitelabel.sdk.auth.AuthorizationsCenter;
import com.launchkey.android.whitelabel.sdk.auth.AuthorizationsItemFilterRule;
import com.launchkey.android.whitelabel.sdk.error.BaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by armando on 7/8/16.
 */
public class CustomAuthorizationsFragment2 extends BaseDemoFragment
        implements AdapterView.OnItemClickListener, AuthorizationsCenter.AuthorizationsListener2 {

    private List<Application> mAuthorizations = new ArrayList<>();
    private ListView mList;
    private DemoAuthorizationsAdapter mAdapter;
    private AuthorizationsCenter mAuthsCenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.demo_fragment_authorizations, container, false);
        return postInflationSetup(root);
    }

    private View postInflationSetup(View root) {
        mAdapter = new DemoAuthorizationsAdapter(getActivity(), mAuthorizations, this);

        mList = (ListView) root.findViewById(R.id.demo_fragment_authorizations_list);
        mList.setAdapter(mAdapter);

        final Button clearAll = (Button) root.findViewById(R.id.demo_fragment_authorizations_button);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthsCenter.logOutAllApplications(true, CustomAuthorizationsFragment2.this);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuthsCenter = AuthorizationsCenter.getInstance(getActivity());

        //apply custom rule to filter out the unnecessary entries
        mAuthsCenter.setAuthorizationsItemFilterRules(new CustomAuthorizationsRule());
    }

    @Override
    public void onResume() {
        super.onResume();
        mAuthsCenter.addListener(this);
        mAuthsCenter.refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAuthsCenter.removeListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Application a = mAdapter.getItem(position);
        mAuthsCenter.logOutApplication(a, false, this);
    }

    @Override
    public void onAuthorizationsListUpdate(boolean success, List<Application> list,
                                           boolean localDelivery, BaseError error) {
        if (success) {
            mAuthorizations.clear();
            mAuthorizations.addAll(list);
            mAdapter.notifyDataSetChanged();
        } else {
            Utils.simpleSnackbarForBaseError(mList, error);
        }
    }

    @Override
    public void onApplicationLogOut(boolean success, boolean forced,
                                    Application application, BaseError error) {
        if (success) {
            Utils.simpleSnackbar(mList, "Authorization logged out", Snackbar.LENGTH_LONG);
            mAuthsCenter.refresh();
        } else {
            Utils.simpleSnackbarForBaseError(mList, error);
        }
    }

    @Override
    public void onAllApplicationsLogOut(boolean success, boolean includingTransactions,
                                        BaseError error) {
        if (success) {
            Utils.simpleSnackbar(mList, "Authorizations cleared", Snackbar.LENGTH_LONG);
            mAuthsCenter.refresh();
        } else {
            Utils.simpleSnackbarForBaseError(mList, error);
        }
    }

    private class CustomAuthorizationsRule implements AuthorizationsItemFilterRule {

        @Override
        public boolean showApplication(Application application) {
            return !application.getAction().equals("Revoke");
        }
    }
}
