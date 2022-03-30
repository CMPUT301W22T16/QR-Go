// Generated by view binder compiler. Do not edit!
package com.example.qr_go.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.qr_go.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class UserQrListContentBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView qrIdView;

  @NonNull
  public final TextView qrScoreView;

  @NonNull
  public final ImageView trashButton;

  private UserQrListContentBinding(@NonNull LinearLayout rootView, @NonNull TextView qrIdView,
      @NonNull TextView qrScoreView, @NonNull ImageView trashButton) {
    this.rootView = rootView;
    this.qrIdView = qrIdView;
    this.qrScoreView = qrScoreView;
    this.trashButton = trashButton;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static UserQrListContentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static UserQrListContentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.user_qr_list_content, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static UserQrListContentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.qr_id_view;
      TextView qrIdView = ViewBindings.findChildViewById(rootView, id);
      if (qrIdView == null) {
        break missingId;
      }

      id = R.id.qr_score_view;
      TextView qrScoreView = ViewBindings.findChildViewById(rootView, id);
      if (qrScoreView == null) {
        break missingId;
      }

      id = R.id.trash_button;
      ImageView trashButton = ViewBindings.findChildViewById(rootView, id);
      if (trashButton == null) {
        break missingId;
      }

      return new UserQrListContentBinding((LinearLayout) rootView, qrIdView, qrScoreView,
          trashButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
