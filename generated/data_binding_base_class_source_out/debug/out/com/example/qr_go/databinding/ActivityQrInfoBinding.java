// Generated by view binder compiler. Do not edit!
package com.example.qr_go.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.qr_go.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityQrInfoBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final BottomNavigationView bottomNavView;

  @NonNull
  public final Button buttonOtherPlayers;

  @NonNull
  public final Button buttonSend;

  @NonNull
  public final EditText inputComment;

  @NonNull
  public final ListView myQRList;

  @NonNull
  public final ImageView profilePhoto;

  @NonNull
  public final TextView qrLocation;

  @NonNull
  public final TextView qrName;

  @NonNull
  public final TextView qrScore;

  private ActivityQrInfoBinding(@NonNull ConstraintLayout rootView,
      @NonNull BottomNavigationView bottomNavView, @NonNull Button buttonOtherPlayers,
      @NonNull Button buttonSend, @NonNull EditText inputComment, @NonNull ListView myQRList,
      @NonNull ImageView profilePhoto, @NonNull TextView qrLocation, @NonNull TextView qrName,
      @NonNull TextView qrScore) {
    this.rootView = rootView;
    this.bottomNavView = bottomNavView;
    this.buttonOtherPlayers = buttonOtherPlayers;
    this.buttonSend = buttonSend;
    this.inputComment = inputComment;
    this.myQRList = myQRList;
    this.profilePhoto = profilePhoto;
    this.qrLocation = qrLocation;
    this.qrName = qrName;
    this.qrScore = qrScore;
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
      id = R.id.bottom_nav_view;
      BottomNavigationView bottomNavView = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavView == null) {
        break missingId;
      }

      id = R.id.buttonOtherPlayers;
      Button buttonOtherPlayers = ViewBindings.findChildViewById(rootView, id);
      if (buttonOtherPlayers == null) {
        break missingId;
      }

      id = R.id.buttonSend;
      Button buttonSend = ViewBindings.findChildViewById(rootView, id);
      if (buttonSend == null) {
        break missingId;
      }

      id = R.id.inputComment;
      EditText inputComment = ViewBindings.findChildViewById(rootView, id);
      if (inputComment == null) {
        break missingId;
      }

      id = R.id.myQRList;
      ListView myQRList = ViewBindings.findChildViewById(rootView, id);
      if (myQRList == null) {
        break missingId;
      }

      id = R.id.profile_photo;
      ImageView profilePhoto = ViewBindings.findChildViewById(rootView, id);
      if (profilePhoto == null) {
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

      id = R.id.qrScore;
      TextView qrScore = ViewBindings.findChildViewById(rootView, id);
      if (qrScore == null) {
        break missingId;
      }

      return new ActivityQrInfoBinding((ConstraintLayout) rootView, bottomNavView,
          buttonOtherPlayers, buttonSend, inputComment, myQRList, profilePhoto, qrLocation, qrName,
          qrScore);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
