/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.swaas.hari.hidoctor.ExoPlayerView;

import android.os.Looper;
import android.support.annotation.Nullable;

import com.swaas.hari.hidoctor.ExoPlayerView.source.MediaSource;
import com.swaas.hari.hidoctor.ExoPlayerView.source.TrackGroupArray;
import com.swaas.hari.hidoctor.ExoPlayerView.trackselection.TrackSelectionArray;


public interface ExoPlayer {

  /**
   * Listener of changes in player state.
   */
  interface EventListener {

    /**
     * Called when the timeline and/or manifest has been refreshed.
     * <p>
     * Note that if the timeline has changed then a position discontinuity may also have occurred.
     * For example, the current period index may have changed as a result of periods being added or
     * removed from the timeline. This will <em>not</em> be reported via a separate call to
     * {@link #onPositionDiscontinuity()}.
     *
     * @param timeline The latest timeline. Never null, but may be empty.
     * @param manifest The latest manifest. May be null.
     */
    void onTimelineChanged(Timeline timeline, Object manifest);

    /**
     * Called when the available or selected tracks change.
     *
     * @param trackGroups The available tracks. Never null, but may be of length zero.
     * @param trackSelections The track selections for each {@link Renderer}. Never null and always
     *     of length {@link #getRendererCount()}, but may contain null elements.
     */
    void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections);

    /**
     * Called when the player starts or stops loading the source.
     *
     * @param isLoading Whether the source is currently being loaded.
     */
    void onLoadingChanged(boolean isLoading);

    /**
     * Called when the value returned from either {@link #getPlayWhenReady()} or
     * {@link #getPlaybackState()} changes.
     *
     * @param playWhenReady Whether playback will proceed when ready.
     * @param playbackState One of the {@code STATE} constants defined in the {@link ExoPlayer}
     *     interface.
     */
    void onPlayerStateChanged(boolean playWhenReady, int playbackState);

    /**
     * Called when an error occurs. The playback state will transition to {@link #STATE_IDLE}
     * immediately after this method is called. The player instance can still be used, and
     * {@link #release()} must still be called on the player should it no longer be required.
     *
     * @param error The error.
     */
    void onPlayerError(ExoPlaybackException error);

    /**
     * Called when a position discontinuity occurs without a change to the timeline. A position
     * discontinuity occurs when the current window or period index changes (as a result of playback
     * transitioning from one period in the timeline to the next), or when the playback position
     * jumps within the period currently being played (as a result of a seek being performed, or
     * when the source introduces a discontinuity internally).
     * <p>
     * When a position discontinuity occurs as a result of a change to the timeline this method is
     * <em>not</em> called. {@link #onTimelineChanged(Timeline, Object)} is called in this case.
     */
    void onPositionDiscontinuity();

    /**
     * Called when the current playback parameters change. The playback parameters may change due to
     * a call to {@link ExoPlayer#setPlaybackParameters(PlaybackParameters)}, or the player itself
     * may change them (for example, if audio playback switches to passthrough mode, where speed
     * adjustment is no longer possible).
     *
     * @param playbackParameters The playback parameters.
     */
    void onPlaybackParametersChanged(PlaybackParameters playbackParameters);

  }

  /**
   * A component of an {@link ExoPlayer} that can receive messages on the playback thread.
   * <p>
   * Messages can be delivered to a component via {@link #sendMessages} and
   * {@link #blockingSendMessages}.
   */
  interface ExoPlayerComponent {

    /**
     * Handles a message delivered to the component. Called on the playback thread.
     *
     * @param messageType The message type.
     * @param message The message.
     * @throws ExoPlaybackException If an error occurred whilst handling the message.
     */
    void handleMessage(int messageType, Object message) throws ExoPlaybackException;

  }

  /**
   * Defines a message and a target {@link ExoPlayerComponent} to receive it.
   */
  final class ExoPlayerMessage {

    /**
     * The target to receive the message.
     */
    public final ExoPlayerComponent target;
    /**
     * The type of the message.
     */
    public final int messageType;
    /**
     * The message.
     */
    public final Object message;

    /**
     * @param target The target of the message.
     * @param messageType The message type.
     * @param message The message.
     */
    public ExoPlayerMessage(ExoPlayerComponent target, int messageType, Object message) {
      this.target = target;
      this.messageType = messageType;
      this.message = message;
    }

  }

  /**
   * The player does not have a source to play, so it is neither buffering nor ready to play.
   */
  int STATE_IDLE = 1;
  /**
   * The player not able to immediately play from the current position. The cause is
   * {@link Renderer} specific, but this state typically occurs when more data needs to be
   * loaded to be ready to play, or more data needs to be buffered for playback to resume.
   */
  int STATE_BUFFERING = 2;
  /**
   * The player is able to immediately play from the current position. The player will be playing if
   * {@link #getPlayWhenReady()} returns true, and paused otherwise.
   */
  int STATE_READY = 3;
  /**
   * The player has finished playing the media.
   */
  int STATE_ENDED = 4;

  /**
   * Register a listener to receive events from the player. The listener's methods will be called on
   * the thread that was used to construct the player. However, if the thread used to construct the
   * player does not have a {@link Looper}, then the listener will be called on the main thread.
   *
   * @param listener The listener to register.
   */
  void addListener(EventListener listener);

  /**
   * Unregister a listener. The listener will no longer receive events from the player.
   *
   * @param listener The listener to unregister.
   */
  void removeListener(EventListener listener);

  /**
   * Returns the current state of the player.
   *
   * @return One of the {@code STATE} constants defined in this interface.
   */
  int getPlaybackState();

  /**
   * Prepares the player to play the provided {@link MediaSource}. Equivalent to
   * {@code prepare(mediaSource, true, true)}.
   */
  void prepare(MediaSource mediaSource);

  /**
   * Prepares the player to play the provided {@link MediaSource}, optionally resetting the playback
   * position the default position in the first {@link Timeline.Window}.
   *
   * @param mediaSource The {@link MediaSource} to play.
   * @param resetPosition Whether the playback position should be reset to the default position in
   *     the first {@link Timeline.Window}. If false, playback will start from the position defined
   *     by {@link #getCurrentWindowIndex()} and {@link #getCurrentPosition()}.
   * @param resetState Whether the timeline, manifest, tracks and track selections should be reset.
   *     Should be true unless the player is being prepared to play the same media as it was playing
   *     previously (e.g. if playback failed and is being retried).
   */
  void prepare(MediaSource mediaSource, boolean resetPosition, boolean resetState);

  /**
   * Sets whether playback should proceed when {@link #getPlaybackState()} == {@link #STATE_READY}.
   * <p>
   * If the player is already in the ready state then this method can be used to pause and resume
   * playback.
   *
   * @param playWhenReady Whether playback should proceed when ready.
   */
  void setPlayWhenReady(boolean playWhenReady);

  /**
   * Whether playback will proceed when {@link #getPlaybackState()} == {@link #STATE_READY}.
   *
   * @return Whether playback will proceed when ready.
   */
  boolean getPlayWhenReady();

  /**
   * Whether the player is currently loading the source.
   *
   * @return Whether the player is currently loading the source.
   */
  boolean isLoading();

  /**
   * Seeks to the default position associated with the current window. The position can depend on
   * the type of source passed to {@link #prepare(MediaSource)}. For live streams it will typically
   * be the live edge of the window. For other streams it will typically be the start of the window.
   */
  void seekToDefaultPosition();

  /**
   * Seeks to the default position associated with the specified window. The position can depend on
   * the type of source passed to {@link #prepare(MediaSource)}. For live streams it will typically
   * be the live edge of the window. For other streams it will typically be the start of the window.
   *
   * @param windowIndex The index of the window whose associated default position should be seeked
   *     to.
   */
  void seekToDefaultPosition(int windowIndex);

  /**
   * Seeks to a position specified in milliseconds in the current window.
   *
   * @param positionMs The seek position in the current window, or {@link C#TIME_UNSET} to seek to
   *     the window's default position.
   */
  void seekTo(long positionMs);

  /**
   * Seeks to a position specified in milliseconds in the specified window.
   *
   * @param windowIndex The index of the window.
   * @param positionMs The seek position in the specified window, or {@link C#TIME_UNSET} to seek to
   *     the window's default position.
   */
  void seekTo(int windowIndex, long positionMs);

  /**
   * Attempts to set the playback parameters. Passing {@code null} sets the parameters to the
   * default, {@link PlaybackParameters#DEFAULT}, which means there is no speed or pitch adjustment.
   * <p>
   * Playback parameters changes may cause the player to buffer.
   * {@link EventListener#onPlaybackParametersChanged(PlaybackParameters)} will be called whenever
   * the currently active playback parameters change. When that listener is called, the parameters
   * passed to it may not match {@code playbackParameters}. For example, the chosen speed or pitch
   * may be out of range, in which case they are constrained to a set of permitted values. If it is
   * not possible to change the playback parameters, the listener will not be invoked.
   *
   * @param playbackParameters The playback parameters, or {@code null} to use the defaults.
   */
  void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters);

  /**
   * Returns the currently active playback parameters.
   *
   * @see EventListener#onPlaybackParametersChanged(PlaybackParameters)
   */
  PlaybackParameters getPlaybackParameters();

  /**
   * Stops playback. Use {@code setPlayWhenReady(false)} rather than this method if the intention
   * is to pause playback.
   * <p>
   * Calling this method will cause the playback state to transition to {@link #STATE_IDLE}. The
   * player instance can still be used, and {@link #release()} must still be called on the player if
   * it's no longer required.
   * <p>
   * Calling this method does not reset the playback position.
   */
  void stop();

  /**
   * Releases the player. This method must be called when the player is no longer required. The
   * player must not be used after calling this method.
   */
  void release();

  /**
   * Sends messages to their target components. The messages are delivered on the playback thread.
   * If a component throws an {@link ExoPlaybackException} then it is propagated out of the player
   * as an error.
   *
   * @param messages The messages to be sent.
   */
  void sendMessages(ExoPlayerMessage... messages);

  /**
   * Variant of {@link #sendMessages(ExoPlayerMessage...)} that blocks until after the messages have
   * been delivered.
   *
   * @param messages The messages to be sent.
   */
  void blockingSendMessages(ExoPlayerMessage... messages);

  /**
   * Returns the number of renderers.
   */
  int getRendererCount();

  /**
   * Returns the track type that the renderer at a given index handles.
   *
   * @see Renderer#getTrackType()
   * @param index The index of the renderer.
   * @return One of the {@code TRACK_TYPE_*} constants defined in {@link C}.
   */
  int getRendererType(int index);

  /**
   * Returns the available track groups.
   */
  TrackGroupArray getCurrentTrackGroups();

  /**
   * Returns the current track selections for each renderer.
   */
  TrackSelectionArray getCurrentTrackSelections();

  /**
   * Returns the current manifest. The type depends on the {@link MediaSource} passed to
   * {@link #prepare}. May be null.
   */
  Object getCurrentManifest();

  /**
   * Returns the current {@link Timeline}. Never null, but may be empty.
   */
  Timeline getCurrentTimeline();

  /**
   * Returns the index of the period currently being played.
   */
  int getCurrentPeriodIndex();

  /**
   * Returns the index of the window currently being played.
   */
  int getCurrentWindowIndex();

  /**
   * Returns the duration of the current window in milliseconds, or {@link C#TIME_UNSET} if the
   * duration is not known.
   */
  long getDuration();

  /**
   * Returns the playback position in the current window, in milliseconds.
   */
  long getCurrentPosition();

  /**
   * Returns an estimate of the position in the current window up to which data is buffered, in
   * milliseconds.
   */
  long getBufferedPosition();

  /**
   * Returns an estimate of the percentage in the current window up to which data is buffered, or 0
   * if no estimate is available.
   */
  int getBufferedPercentage();

  /**
   * Returns whether the current window is dynamic, or {@code false} if the {@link Timeline} is
   * empty.
   *
   * @see Timeline.Window#isDynamic
   */
  boolean isCurrentWindowDynamic();

  /**
   * Returns whether the current window is seekable, or {@code false} if the {@link Timeline} is
   * empty.
   *
   * @see Timeline.Window#isSeekable
   */
  boolean isCurrentWindowSeekable();

}
