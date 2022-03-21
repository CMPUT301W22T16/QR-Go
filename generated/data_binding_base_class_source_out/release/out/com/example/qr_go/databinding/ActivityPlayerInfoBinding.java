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
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityPlayerInfoBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LinearLayout QRData;

  @NonNull
  public final LinearLayout highestScoreData;

  @NonNull
  public final LinearLayout lowestScoreData;

  @NonNull
  public final TextView numOfQRCodes;

  @NonNull
  public final TextView playerEmail;

  @NonNull
  public final TextView playerHighScore;

  @NonNull
  public final TextView playerLowScore;

  @NonNull
  public final TextView playerNameText;

  @NonNull
  public final TextView playerTotalScore;

  @NonNull
  public final TextView textView2;

  @NonNull
  public final LinearLayout totalScoreData;

  private ActivityPlayerInfoBinding(@NonNull LinearLayout rootView, @NonNull LinearLayout QRData,
      @NonNull LinearLayout highestScoreData, @NonNull LinearLayout lowestScoreData,
      @NonNull TextView numOfQRCodes, @NonNull TextView playerEmail,
      @NonNull TextView playerHighScore, @NonNull TextView playerLowScore,
      @NonNull TextView playerNameText, @NonNull TextView playerTotalScore,
      @NonNull TextView textView2, @NonNull LinearLayout totalScoreData) {
    this.rootView = rootView;
    this.QRData = QRData;
    this.highestScoreData = highestScoreData;
    this.lowestScoreData = lowestScoreData;
    this.numOfQRCodes = numOfQRCodes;
    this.playerEmail = playerEmail;
    this.playerHighScore = playerHighScore;
    this.playerLowScore = playerLowScore;
    this.playerNameText = playerNameText;
    this.playerTotalScore = playerTotalScore;
    this.textView2 = textView2;
    this.totalScoreData = totalScoreData;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityPlayerInfoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityPlayerInfoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_player_info, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityPlayerInfoBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.QRData;
      LinearLayout QRData = ViewBindings.findChildViewById(rootView, id);
      if (QRData == null) {
        break missingId;
      }

      id = R.id.highestScoreData;
      LinearLayout highestScoreData = ViewBindings.findChildViewById(rootView, id);
      if (highestScoreData == null) {
        break missingId;
      }

      id = R.id.lowestScoreData;
      LinearLayout lowestScoreData = ViewBindings.findChildViewById(rootView, id);
      if (lowestScoreData == null) {
        break missingId;
      }

      id = R.id.numOfQRCodes;
      TextView numOfQRCodes = ViewBindings.findChildViewById(rootView, id);
      if (numOfQRCodes == null) {
        break missingId;
      }

      id = R.id.playerEmail;
      TextView playerEmail = ViewBindings.findChildViewById(rootView, id);
      if (playerEmail == null) {
        break missingId;
      }

      id = R.id.playerHighScore;
      TextView playerHighScore = ViewBindings.findChildViewById(rootView, id);
      if (playerHighScore == null) {
        break missingId;
      }

      id = R.id.playerLowScore;
      TextView playerLowScore = ViewBindings.findChildViewById(rootView, id);
      if (playerLowScore == null) {
        break missingId;
      }

      id = R.id.playerNameText;
      TextView playerNameText = ViewBindings.findChildViewById(rootView, id);
      if (playerNameText == null) {
        break missingId;
      }

      id = R.id.playerTotalScore;
      TextView playerTotalScore = ViewBindings.findChildViewById(rootView, id);
      if (playerTotalScore == null) {
        break missingId;
      }

      id = R.id.textView2;
      TextView textView2 = ViewBindings.findChildViewById(rootView, id);
      if (textView2 == null) {
        break missingId;
      }

      id = R.id.totalScoreData;
      LinearLayout totalScoreData = ViewBindings.findChildViewById(rootView, id);
      if (totalScoreData == null) {
        break missingId;
      }

      return new ActivityPlayerInfoBinding((LinearLayout) rootView, QRData, highestScoreData,
          lowestScoreData, numOfQRCodes, playerEmail, playerHighScore, playerLowScore,
          playerNameText, playerTotalScore, textView2, totalScoreData);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
