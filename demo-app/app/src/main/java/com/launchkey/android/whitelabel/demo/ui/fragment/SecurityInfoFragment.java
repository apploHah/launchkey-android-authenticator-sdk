package com.launchkey.android.whitelabel.demo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.launchkey.android.whitelabel.demo.R;
import com.launchkey.android.whitelabel.demo.util.Utils;
import com.launchkey.android.whitelabel.sdk.error.BaseError;
import com.launchkey.android.whitelabel.sdk.security.SecurityFactor;
import com.launchkey.android.whitelabel.sdk.security.SecurityService;

import java.util.List;

/**
 * Created by armando on 7/20/16.
 */
public class SecurityInfoFragment extends BaseDemoFragment implements SecurityService.SecurityStatusListener {

    private TextView mText;
    private SecurityService mSecService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.demo_fragment_security_info, container, false);
        return postInflationSetup(root);
    }

    private View postInflationSetup(View root) {
        mText = (TextView) root.findViewById(R.id.demo_fragment_securityinfo_text);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSecService = SecurityService.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        mSecService.getStatus(this);
    }

    @Override
    public void onSecurityStatusUpdate(boolean success, List<SecurityFactor> list, BaseError error) {
        if (success) {
            showSecurityInformation(list);
        } else {
            Utils.simpleSnackbar(mText, "Error: " + error.getMessage());
        }
    }

    private void showSecurityInformation(List<SecurityFactor> list) {

        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (SecurityFactor f : list) {

            sb.append("\n");

            if (f != null) {
                sb.append(String.format("Factor:%s, Category:%s, Active:%b",
                        getFactorName(f), getFactorCategory(f), f.isActive()));
            } else {
                sb.append("Factor null");
            }
        }

        String message = sb.toString();

        if (message.trim().isEmpty()) {
            message = "None set.";
        }

        mText.setText(message);
    }

    private String getFactorName(SecurityFactor f) {
        switch (f.getFactor()) {
            case SecurityService.FACTOR_PIN:
                return "PIN Code";
            case SecurityService.FACTOR_CIRCLE:
                return "Circle Code";
            case SecurityService.FACTOR_BLUETOOTH:
                return "Bluetooth Devices";
            case SecurityService.FACTOR_GEOFENCING:
                return "Geofencing";
            case SecurityService.FACTOR_WEARABLES:
                return "Wearable Devices";
            case SecurityService.FACTOR_FINGERPRINT:
                return "Fingerprint";
            default:
                return "None";
        }
    }

    private String getFactorCategory(SecurityFactor f) {
        switch (f.getCategory()) {
            case SecurityService.CATEGORY_KNOWLEDGE:
                return "Knowledge";
            case SecurityService.CATEGORY_INHERENCE:
                return "Inherence";
            case SecurityService.CATEGORY_POSSESSION:
                return "Possession";
            default:
                return "None";
        }
    }
}
