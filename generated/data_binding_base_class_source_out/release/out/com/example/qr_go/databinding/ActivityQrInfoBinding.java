// Generated by view binder compiler. Do not edit!
package com.example.qr_go.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.qr_go.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityQrInfoBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView qrIcon;

  @NonNull
  public final TextView qrLocation;

  @NonNull
  public final TextView qrName;

  private ActivityQrInfoBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView qrIcon,
      @NonNull TextView qrLocation, @NonNull TextView qrName) {
    this.rootView = rootView;
    this.qrIcon = qrIcon;
    this.qrLocation = qrLocation;
    this.qrName = qrName;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityQrInfoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityQrInfoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_qr_info, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityQrInfoBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.qrIcon;
      ImageView qrIcon = ViewBindings.findChildViewById(rootView, id);
      if (qrIcon == null) {
        break missingId;
      }

      id = R.id.qrLocation;
      TextView qrLocation = ViewBindings.findChildViewById(rootView, id);
      if (qrLocation == null) {
        break missingId;
      }

      id = R.id.qrName;
      TextView qrName = ViewBindings.findChildViewById(rootView, id);
      if (qrName == null) {
        break missingId;
      }

      return new ActivityQrInfoBinding((ConstraintLayout) rootView, qrIcon, qrLocation, qrName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
