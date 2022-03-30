// Generated by view binder compiler. Do not edit!
package com.example.qr_go.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.qr_go.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class UserListContentBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ShapeableImageView qrSearchOptionIcon;

  @NonNull
  public final MaterialButton userDelButton;

  @NonNull
  public final TextView userScoreView;

  @NonNull
  public final TextView usernameView;

  private UserListContentBinding(@NonNull LinearLayout rootView,
      @NonNull ShapeableImageView qrSearchOptionIcon, @NonNull MaterialButton userDelButton,
      @NonNull TextView userScoreView, @NonNull TextView usernameView) {
    this.rootView = rootView;
    this.qrSearchOptionIcon = qrSearchOptionIcon;
    this.userDelButton = userDelButton;
    this.userScoreView = userScoreView;
    this.usernameView = usernameView;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static UserListContentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static UserListContentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.user_list_content, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static UserListContentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.qr_search_option_icon;
      ShapeableImageView qrSearchOptionIcon = ViewBindings.findChildViewById(rootView, id);
      if (qrSearchOptionIcon == null) {
        break missingId;
      }

      id = R.id.user_del_button;
      MaterialButton userDelButton = ViewBindings.findChildViewById(rootView, id);
      if (userDelButton == null) {
        break missingId;
      }

      id = R.id.user_score_view;
      TextView userScoreView = ViewBindings.findChildViewById(rootView, id);
      if (userScoreView == null) {
        break missingId;
      }

      id = R.id.username_view;
      TextView usernameView = ViewBindings.findChildViewById(rootView, id);
      if (usernameView == null) {
        break missingId;
      }

      return new UserListContentBinding((LinearLayout) rootView, qrSearchOptionIcon, userDelButton,
          userScoreView, usernameView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
