// IMusicPlayerService.aidl
package com.junmei.mylove;

// Declare any non-default types here with import statements

interface IMusicPlayerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    void openAudio(int position);

   void start();

    void pause();

   void setPlaymode(int playmode);

   int getPlaymode();

   String getArtist();

   String getAudioName();

   int getDuration();

   int getCurrentPosition();

   void seekTo(int position);

  void next();

   void pre();

   boolean isPlaying();






}
