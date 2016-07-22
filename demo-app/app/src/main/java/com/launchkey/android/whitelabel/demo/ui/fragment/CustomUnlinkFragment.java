package com.launchkey.android.whitelabel.demo.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.util.Utils;
import com.launchkey.android.whitelabel.sdk.WhiteLabelManager;
import com.launchkey.android.whitelabel.sdk.error.BaseError;
import com.launchkey.android.whitelabel.sdk.error.CommunicationError;

/**
 * Created by armando on 7/20/16.
 */
public class CustomUnlinkFragment extends BaseDemoFragment {

    private ProgressDialog mUnlinkingDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUnlinkingDialog = new ProgressDialog(getActivity(), R.style.Theme_WhiteLabel_Dialog);
        mUnlinkingDialog.setIndeterminate(true);
        mUnlinkingDialog.setCancelable(false);
        mUnlinkingDialog.setMessage("Custom Unlinking...");

        unlink();
    }

    private void unlink() {

        Utils.show(mUnlinkingDialog);

        getWhiteLabelManager().unlink(new WhiteLabelManager.UnlinkListener() {

            @Override
            public void onUnlink(boolean success, BaseError error) {

                Utils.dismiss(mUnlinkingDialog);

                if (success) {
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        getActivity().finish();
                    }
                } else {
                    String httpCode = error instanceof CommunicationError
                            ? String.format("(%s)", error.getCode()) : "";

                    String message = error == null
                            ? "Null error"
                            : String.format("Error %s obj=%s message=%s",
                            httpCode, error.getClass().getSimpleName(), error.getMessage());
                    Utils.alert(getActivity(), "Problem Unlinking", message);
                }
            }
        });
    }
}
