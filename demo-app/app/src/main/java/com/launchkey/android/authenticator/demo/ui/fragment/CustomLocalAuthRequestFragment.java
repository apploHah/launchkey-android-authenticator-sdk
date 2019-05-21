package com.launchkey.android.authenticator.demo.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.launchkey.android.authenticator.demo.R;
import com.launchkey.android.authenticator.demo.ui.activity.AuthRequestActivity;
import com.launchkey.android.authenticator.demo.util.Utils;
import com.launchkey.android.authenticator.sdk.auth.LocalAuthManager;
import com.launchkey.android.authenticator.sdk.auth.LocalAuthRequest;
import com.launchkey.android.authenticator.sdk.auth.Policy;
import com.launchkey.android.authenticator.sdk.auth.PolicyFactory;
import com.launchkey.android.authenticator.sdk.auth.event.GetLocalAuthEventCallback;
import com.launchkey.android.authenticator.sdk.auth.event.LocalAuthResponseEventCallback;
import com.launchkey.android.authenticator.sdk.error.BaseError;
import com.launchkey.android.authenticator.sdk.error.LocalAuthRequestPendingError;

/**
 * Created by armando on 1/4/18.
 */

public class CustomLocalAuthRequestFragment extends BaseDemoFragment {

    private View mRoot;
    private Switch mByCount, mByType;
    private EditText mByCountField;
    private Switch mByTypeKno, mByTypeInh, mByTypePos;
    private EditText mTitle, mExpiration;
    private Button mGenerate;
    private LocalAuthManager mLocalAuth;
    private GetLocalAuthEventCallback mOnLocalAuth;
    private LocalAuthResponseEventCallback mOnLocalResponse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRoot = inflater.inflate(R.layout.demo_fragment_larconfig, container, false);

        mByCount = (Switch) mRoot.findViewById(R.id.lar_switch_count);
        mByType = (Switch) mRoot.findViewById(R.id.lar_switch_type);
        mByTypeKno = (Switch) mRoot.findViewById(R.id.lar_switch_type_kno);
        mByTypeInh = (Switch) mRoot.findViewById(R.id.lar_switch_type_inh);
        mByTypePos = (Switch) mRoot.findViewById(R.id.lar_switch_type_pos);
        mByCountField = (EditText) mRoot.findViewById(R.id.lar_edit_count);
        mTitle = (EditText) mRoot.findViewById(R.id.lar_edit_title);
        mGenerate = (Button) mRoot.findViewById(R.id.lar_button);

        mExpiration = (EditText) mRoot.findViewById(R.id.lar_edit_expiration);
        mExpiration.setHint(getString(R.string.demo_activity_list_feature_requests_local_expiration_seconds_hint, LocalAuthManager.EXPIRE_IN_SECONDS_DEFAULT, LocalAuthManager.EXPIRE_IN_SECONDS_MAX));

        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    final boolean byCount = buttonView.getId() == R.id.lar_switch_count;

                    mByCount.setChecked(byCount);
                    mByCountField.setEnabled(byCount);

                    mByType.setChecked(!byCount);
                    mByTypeKno.setEnabled(!byCount);
                    mByTypeInh.setEnabled(!byCount);
                    mByTypePos.setEnabled(!byCount);

                }
            }
        };

        mByCount.setOnCheckedChangeListener(listener);
        mByType.setOnCheckedChangeListener(listener);

        mGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generateLar();
            }
        });

        mOnLocalAuth = new GetLocalAuthEventCallback() {

            @Override
            public void onEventResult(boolean successful, BaseError error,
                                      LocalAuthRequest localAuthRequest) {

                if (getActivity() == null) {
                    return;
                }

                if (!successful) {

                    String errorMessage = Utils.getMessageForBaseError(error);

                    DialogInterface.OnClickListener okClick = null;

                    // Provide hooks to handling a pending request if there's one
                    if (error instanceof LocalAuthRequestPendingError) {

                        okClick = new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                handleLocalAuth();
                            }
                        };

                        // Append to error message to handle pending request
                        errorMessage = errorMessage
                                .concat("\n")
                                .concat(getString(R.string.demo_fragment_lar_error_pending_message));
                    }

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.demo_fragment_lar_error_title)
                            .setMessage(errorMessage)
                            .setPositiveButton(R.string.demo_generic_ok, okClick)
                            .create();

                    alertDialog.show();

                    TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
                    if (messageView != null) {
                        messageView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    }
                }
            }
        };

        mOnLocalResponse = new LocalAuthResponseEventCallback() {
            @Override
            public void onEventResult(boolean successful, BaseError error, Boolean approved) {

                if (getActivity() == null) {
                    return;
                }

                final String message;

                if (successful) {

                    message = getString(R.string.demo_activity_list_feature_requests_local_result_message,
                            approved ?
                            getString(R.string.demo_activity_list_feature_requests_local_result_approved) :
                            getString(R.string.demo_activity_list_feature_requests_local_result_denied));
                } else {

                    message = Utils.getMessageForBaseError(error);
                }

                Snackbar.make(mRoot, message, Snackbar.LENGTH_LONG).show();
            }
        };

        mLocalAuth = LocalAuthManager.getInstance();
        mLocalAuth.registerForEvents(mOnLocalAuth, mOnLocalResponse);
    }

    private void generateLar() {

        PolicyFactory factory = new PolicyFactory();
        Policy policy;

        if (mByCount.isChecked()) {

            int count;

            try {
                count = Integer.parseInt(mByCountField.getText().toString().trim());
            } catch (Exception e) {
                count = 0;
            }

            policy = factory.getPolicy(count);
        } else if (mByType.isChecked()) {

            policy = factory.getPolicy(mByTypeKno.isChecked(), mByTypeInh.isChecked(), mByTypePos.isChecked());
        } else {

            policy = factory.getPolicy();
        }

        String title = mTitle.getText().toString().trim();
        boolean letSdkUseDefaultTitle = title.isEmpty();

        if (letSdkUseDefaultTitle) {
            title = null;
        }

        mLocalAuth.setTitle(title);

        int expirationSeconds;

        try {
            expirationSeconds = Integer.parseInt(mExpiration.getText().toString().trim());
        } catch (Exception e) {
            expirationSeconds = 10;
        }

        mLocalAuth.setExpireIn(expirationSeconds);

        boolean generated = mLocalAuth.authenticateUser(policy);

        // React to successful requests, GetLocalAuthEventCallback will handle error
        if (generated) {

            handleLocalAuth();
        }
    }

    private void handleLocalAuth() {

        Intent authRequestActivity = new Intent(getActivity(), AuthRequestActivity.class);
        startActivity(authRequestActivity);
    }
}
