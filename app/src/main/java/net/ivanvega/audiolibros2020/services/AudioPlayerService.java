/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ivanvega.audiolibros2020.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.ivanvega.audiolibros2020.DetalleFragment;
import net.ivanvega.audiolibros2020.Libro;
import net.ivanvega.audiolibros2020.MainActivity;
import net.ivanvega.audiolibros2020.R;

public class AudioPlayerService extends Service {

  public static SimpleExoPlayer player;
  private PlayerNotificationManager playerNotificationManager;
  public static final String PLAYBACK_CHANNEL_ID = "playback_channel";
  public static final int PLAYBACK_NOTIFICATION_ID = 1;
  public static int id;

  @Override
  public void onCreate() {
    super.onCreate();
    final Context context = this;
    player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
    DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
        context, Util.getUserAgent(context, getString(R.string.app_name)));
    ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
    MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
       .createMediaSource(Uri.parse(Libro.ejemploLibros().get(id).urlAudio));
    concatenatingMediaSource.addMediaSource(mediaSource);
    player.prepare(concatenatingMediaSource);
    //player.setPlayWhenReady(true);
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
        context,
        PLAYBACK_CHANNEL_ID,
        R.string.channel_name,
        PLAYBACK_NOTIFICATION_ID,
        new MediaDescriptionAdapter() {
          @Override
          public String getCurrentContentTitle(Player player) {
            return Libro.ejemploLibros().get(id).titulo;
          }

          @Nullable
          @Override
          public PendingIntent createCurrentContentIntent(Player player) {
              Intent inte = new Intent(getApplicationContext(), MainActivity.class);
              return PendingIntent.getActivity(getApplicationContext(),0,inte,PendingIntent.FLAG_UPDATE_CURRENT);
          }

          @Nullable
          @Override
          public String getCurrentContentText(Player player) {
            return Libro.ejemploLibros().get(id).autor;
          }

          @Nullable
          @Override
          public Bitmap getCurrentLargeIcon(Player player, BitmapCallback callback) {
            return Libro.getBitmap(
                context, Libro.ejemploLibros().get(id).recursoImagen);
          }
        }
    );
    playerNotificationManager.setNotificationListener(new NotificationListener() {
      @Override
      public void onNotificationStarted(int notificationId, Notification notification) {
        startForeground(notificationId, notification);
      }

      @Override
      public void onNotificationCancelled(int notificationId) {

          stopSelf();
      }
    });
    playerNotificationManager.setPlayer(player);

  }

  @Override
  public void onDestroy() {
    playerNotificationManager.setPlayer(null);
    player.release();
    player = null;

    super.onDestroy();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

}
