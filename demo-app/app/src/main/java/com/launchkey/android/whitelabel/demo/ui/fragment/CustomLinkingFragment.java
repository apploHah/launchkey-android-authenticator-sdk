package com.launchkey.android.whitelabel.demo.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.util.Utils;
import com.launchkey.android.whitelabel.sdk.WhiteLabelManager;
import com.launchkey.android.whitelabel.sdk.error.BaseError;

/**
 * Created by armando on 7/8/16.
 */
public class CustomLinkingFragment extends BaseDemoFragment {

    private EditText mCode, mName;
    private CheckBox mProvideName;
    private ProgressDialog mLinkingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.demo_fragment_link, container, false);
        return postInflationSetup(root);
    }

    private View postInflationSetup(View root) {

        mCode = (EditText) root.findViewById(R.id.demo_link_edit_code);
        mName = (EditText) root.findViewById(R.id.demo_link_edit_name);

        mProvideName = (CheckBox) root.findViewById(R.id.demo_link_checkbox_devicename);
        mProvideName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mName.setEnabled(isChecked);
            }
        });

        Button link = (Button) root.findViewById(R.id.demo_link_button);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLink();
            }
        });

        mLinkingDialog = new ProgressDialog(getActivity(), R.style.Theme_WhiteLabel_Dialog);
        mLinkingDialog.setIndeterminate(true);

        return root;
    }

    private void onLink() {
        String linkingCode = mCode.getText().toString().trim();
        String customDeviceName = mName.getText().toString().trim();

        if (!linkingCode.matches(WhiteLabelManager.REGEX_LINKING_CODE)) {
            showAlert("Error", "Linking code has illegal characters. Allowed structure: "
                    + WhiteLabelManager.REGEX_LINKING_CODE);
            return;
        }

        WhiteLabelManager.UserRegistrationListener2 registrationListener =
                new WhiteLabelManager.UserRegistrationListener2() {
                    @Override
                    public void onStart() {
                        mLinkingDialog.show();
                        mLinkingDialog.setMessage("Preparing device...");
                    }

                    @Override
                    public void onCodeVerification() {
                        mLinkingDialog.setMessage("Verifying linking code...");
                    }

                    @Override
                    public void onComplete(boolean successful, BaseError error) {
                        mLinkingDialog.dismiss();

                        if (successful) {
                            Utils.finish(CustomLinkingFragment.this);
                        } else {
                            showAlert("Error", Utils.getMessageForBaseError(error));
                        }
                    }
                };

        //depending on the desired approach, it is possible to provide a custom device name
        // if no custom device name is provided, a default one will be generated
        // based on the model and manufacturer.

        if (mProvideName.isChecked()) {

            if (!customDeviceName.matches(WhiteLabelManager.REGEX_DEVICE_NAME)) {
                showAlert("Error", "Device name has illegal characters. Allowed structure: "
                        + WhiteLabelManager.REGEX_DEVICE_NAME);
                return;
            }

            getWhiteLabelManager().registerUser(getActivity(), linkingCode, customDeviceName, registrationListener);
        } else {
            getWhiteLabelManager().registerUser(getActivity(), linkingCode, registrationListener);
        }
    }

    private void showAlert(String title, String message) {

        DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utils.finish(CustomLinkingFragment.this);
            }
        };

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setOnDismissListener(dismissListener)
                .create()
                .show();
    }
}
