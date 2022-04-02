// Generated by view binder compiler. Do not edit!
package com.example.qr_go.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.qr_go.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public final class ActivityNewGameQrActivityBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final BottomNavigationView bottomNavView;

  @NonNull
  public final Guideline guideline2;

  @NonNull
  public final KonfettiView konfettiView;

  @NonNull
  public final CheckBox locationCheckbox;

  @NonNull
  public final Button saveGameQr;

  @NonNull
  public final TextView score;

  @NonNull
  public final TextView scoreText;

  @NonNull
  public final Button takeQrPhotoButton;

  @NonNull
  public final ImageView takeQrPhotoImageview;

  private ActivityNewGameQrActivityBinding(@NonNull ConstraintLayout rootView,
      @NonNull BottomNavigationView bottomNavView, @NonNull Guideline guideline2,
      @NonNull KonfettiView konfettiView, @NonNull CheckBox locationCheckbox,
      @NonNull Button saveGameQr, @NonNull TextView score, @NonNull TextView scoreText,
      @NonNull Button takeQrPhotoButton, @NonNull ImageView takeQrPhotoImageview) {
    this.rootView = rootView;
    this.bottomNavView = bottomNavView;
    this.guideline2 = guideline2;
    this.konfettiView = konfettiView;
    this.locationCheckbox = locationCheckbox;
    this.saveGameQr = saveGameQr;
    this.score = score;
    this.scoreText = scoreText;
    this.takeQrPhotoButton = takeQrPhotoButton;
    this.takeQrPhotoImageview = takeQrPhotoImageview;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityNewGameQrActivityBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityNewGameQrActivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_new_game_qr_activity, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityNewGameQrActivityBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bottom_nav_view;
      BottomNavigationView bottomNavView = ViewBindings.findChildViewById(rootView, id);
      if (bottomNavView == null) {
        break missingId;
      }

      id = R.id.guideline2;
      Guideline guideline2 = ViewBindings.findChildViewById(rootView, id);
      if (guideline2 == null) {
        break missingId;
      }

      id = R.id.konfettiView;
      KonfettiView konfettiView = ViewBindings.findChildViewById(rootView, id);
      if (konfettiView == null) {
        break missingId;
      }

      id = R.id.location_checkbox;
      CheckBox locationCheckbox = ViewBindings.findChildViewById(rootView, id);
      if (locationCheckbox == null) {
        break missingId;
      }

      id = R.id.save_game_qr;
      Button saveGameQr = ViewBindings.findChildViewById(rootView, id);
      if (saveGameQr == null) {
        break missingId;
      }

      id = R.id.score;
      TextView score = ViewBindings.findChildViewById(rootView, id);
      if (score == null) {
        break missingId;
      }

      id = R.id.score_text;
      TextView scoreText = ViewBindings.findChildViewById(rootView, id);
      if (scoreText == null) {
        break missingId;
      }

      id = R.id.take_qr_photo_button;
      Button takeQrPhotoButton = ViewBindings.findChildViewById(rootView, id);
      if (takeQrPhotoButton == null) {
        break missingId;
      }

      id = R.id.take_qr_photo_imageview;
      ImageView takeQrPhotoImageview = ViewBindings.findChildViewById(rootView, id);
      if (takeQrPhotoImageview == null) {
        break missingId;
      }

      return new ActivityNewGameQrActivityBinding((ConstraintLayout) rootView, bottomNavView,
          guideline2, konfettiView, locationCheckbox, saveGameQr, score, scoreText,
          takeQrPhotoButton, takeQrPhotoImageview);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
