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
package com.swaas.hari.hidoctor.ExoPlayerView.extractor.ts;

import com.swaas.hari.hidoctor.ExoPlayerView.C;
import com.swaas.hari.hidoctor.ExoPlayerView.Format;
import com.swaas.hari.hidoctor.ExoPlayerView.extractor.ExtractorOutput;
import com.swaas.hari.hidoctor.ExoPlayerView.extractor.TrackOutput;
import com.swaas.hari.hidoctor.ExoPlayerView.extractor.ts.TsPayloadReader.TrackIdGenerator;
import com.swaas.hari.hidoctor.ExoPlayerView.text.cea.CeaUtil;
import com.swaas.hari.hidoctor.ExoPlayerView.util.Assertions;
import com.swaas.hari.hidoctor.ExoPlayerView.util.MimeTypes;
import com.swaas.hari.hidoctor.ExoPlayerView.util.ParsableByteArray;

import java.util.List;

/**
 * Consumes SEI buffers, outputting contained CEA-608 messages to a {@link TrackOutput}.
 */
/* package */ final class SeiReader {

  private final List<Format> closedCaptionFormats;
  private final TrackOutput[] outputs;

  /**
   * @param closedCaptionFormats A list of formats for the closed caption channels to expose.
   */
  public SeiReader(List<Format> closedCaptionFormats) {
    this.closedCaptionFormats = closedCaptionFormats;
    outputs = new TrackOutput[closedCaptionFormats.size()];
  }

  public void createTracks(ExtractorOutput extractorOutput, TrackIdGenerator idGenerator) {
    for (int i = 0; i < outputs.length; i++) {
      idGenerator.generateNewId();
      TrackOutput output = extractorOutput.track(idGenerator.getTrackId(), C.TRACK_TYPE_TEXT);
      Format channelFormat = closedCaptionFormats.get(i);
      String channelMimeType = channelFormat.sampleMimeType;
      Assertions.checkArgument(MimeTypes.APPLICATION_CEA608.equals(channelMimeType)
          || MimeTypes.APPLICATION_CEA708.equals(channelMimeType),
          "Invalid closed caption mime type provided: " + channelMimeType);
      output.format(Format.createTextSampleFormat(idGenerator.getFormatId(), channelMimeType, null,
          Format.NO_VALUE, channelFormat.selectionFlags, channelFormat.language,
          channelFormat.accessibilityChannel, null));
      outputs[i] = output;
    }
  }

  public void consume(long pesTimeUs, ParsableByteArray seiBuffer) {
    CeaUtil.consume(pesTimeUs, seiBuffer, outputs);
  }

}
