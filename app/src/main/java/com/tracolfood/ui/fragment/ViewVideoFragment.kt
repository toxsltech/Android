/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.fragment

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tracolfood.R
import com.tracolfood.databinding.FragmentViewVideoBinding
import com.tracolfood.model.VideoData
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler


class ViewVideoFragment : BaseFragment(), ViewClickHandler {
    private var binding: FragmentViewVideoBinding? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var player: SimpleExoPlayer? = null
    private var videoData: VideoData? = null
    private var singleHit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoData = it.getParcelable("list")
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        (baseActivity as MainActivity).setToolBar(false)
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_video, container, false)
        }
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(baseActivity!!, "Tracol")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
    }

    private fun initUI() {
        binding!!.handleClick = this
        binding!!.videoPV.setControllerVisibilityListener {
            if (it == 0) {
                binding!!.screenRotationIV.visibility = View.VISIBLE
            } else {
                binding!!.screenRotationIV.visibility = View.GONE
            }
        }

        playVideo()
    }

    private fun playVideo() {
        if (player == null) {
            val trackSelector = DefaultTrackSelector()
            trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd())
            player = ExoPlayerFactory.newSimpleInstance(baseActivity!!, trackSelector)
            val mediaSource: MediaSource = if (videoData!!.videoFile!!.contains("?")) {
                buildMediaSource(Uri.parse(Const.IMAGE_SERVER_URL.plus(videoData!!.videoFile).plus("&autoplay=true")))!!
            } else {
                buildMediaSource(Uri.parse(Const.IMAGE_SERVER_URL.plus(videoData!!.videoFile).plus("?autoplay=true")))!!
            }

            player!!.playWhenReady = playWhenReady
            player!!.seekTo(currentWindow, playbackPosition)
            player!!.prepare(mediaSource, false, false)
        }
        binding!!.videoPV.player = player
        player!!.addListener(object : Player.EventListener {
            override fun onLoadingChanged(isLoading: Boolean) {
                if (!isLoading) {
                    baseActivity!!.stopProgressDialog()
                }

                baseActivity!!.log("onLoadingChanged " + (if (isLoading) " Loading " else " no loading "))
                if (!singleHit) {
                    singleHit = true
                    if (isLoading){
                        baseActivity!!.startProgressDialog()
                    }
                }
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        baseActivity!!.log("onPlayerStateChanged : $playWhenReady state : STATE_BUFFERING")
                        binding!!.progressPB.visibility = View.VISIBLE
                    }
                    Player.STATE_ENDED -> {
                        baseActivity!!.log("onPlayerStateChanged : $playWhenReady state : STATE_ENDED")
                        binding!!.progressPB.visibility = View.GONE
                    }
                    Player.STATE_IDLE -> {
                        baseActivity!!.log("onPlayerStateChanged : $playWhenReady state : STATE_IDLE")
                        binding!!.progressPB.visibility = View.GONE
                    }

                    Player.STATE_READY -> {
                        baseActivity!!.log("onPlayerStateChanged : $playWhenReady state : STATE_READY")
                        binding!!.progressPB.visibility = View.GONE
                        baseActivity!!.stopProgressDialog()
                    }
                    else -> {
                        baseActivity!!.log("onPlayerStateChanged : $playWhenReady state : default")
                        binding!!.progressPB.visibility = View.GONE
                    }
                }
            }

        })
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 || player == null) {
            playVideo()
        }
        if (binding!!.videoPV.isControllerVisible) {
            binding!!.screenRotationIV.visibility = View.VISIBLE
        } else {
            binding!!.screenRotationIV.visibility = View.GONE
        }

    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }


    override fun onDetach() {
        super.onDetach()
        baseActivity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }


    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.screenRotationIV -> {
                if (baseActivity!!.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    binding!!.videoPV.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    baseActivity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } else {
                    binding!!.videoPV.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    baseActivity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
            }
            R.id.crossIV -> {
                (baseActivity as MainActivity).onBackPressed()
            }
        }
    }


    private fun hideSystemUi() {
        binding!!.videoPV.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)


    }


}